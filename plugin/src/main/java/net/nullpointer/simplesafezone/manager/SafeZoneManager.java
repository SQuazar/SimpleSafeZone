package net.nullpointer.simplesafezone.manager;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.util.Config;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.bb.BoundingBox;
import net.nullpointer.simplesafezone.util.bb.CuboidBoundingBox;
import net.nullpointer.simplesafezone.util.bb.SphereBoundingBox;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class SafeZoneManager {
    private final Map<UUID, SafeZone> safeZones = new ConcurrentHashMap<>();

    private final SimpleSafeZone plugin;
    private final File zonesDir;

    public SafeZoneManager(SimpleSafeZone plugin) {
        this.plugin = plugin;
        this.zonesDir = new File(plugin.getDataFolder(), "zones");
        if (plugin.getConfig().getBoolean(Config.STORE_ZONES.getKey()))
            loadSafeZones();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::removeExpired, 20L, 20L);
    }

    public boolean createSafeZone(Player player, ZoneType zoneType, int radius) {
        if (safeZones.containsKey(player.getUniqueId())) {
            player.sendMessage(colorize(plugin.getMessages().getString(Messages.ALREADY_EXISTS.getKey())));
            return false;
        }

        if (radius < plugin.getConfig().getInt(Config.SAFE_ZONE_MIN_RADIUS.getKey())) {
            player.sendMessage(colorize(plugin.getMessages().getString(Messages.MIN_RADIUS_RESTRICTION.getKey())));
            return false;
        }

        if (radius > plugin.getConfig().getInt(Config.SAFE_ZONE_MAX_RADIUS.getKey())) {
            player.sendMessage(colorize(plugin.getMessages().getString(Messages.MAX_RADIUS_RESTRICTION.getKey())));
            return false;
        }

        BoundingBox box = zoneType == ZoneType.SPHERE ? SphereBoundingBox.of(player.getLocation(), radius)
                : CuboidBoundingBox.of(player.getLocation(), radius);
        SafeZone safeZone = new SafeZone(player.getUniqueId(), box,
                Instant.now().plus(plugin.getConfig().getInt(Config.LIFE_TIME.getKey()), ChronoUnit.MINUTES));
        if (safeZones.values().stream().anyMatch(safeZone::overlaps)) {
            player.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_OVERLAPS.getKey())));
            return false;
        }

        safeZones.put(player.getUniqueId(), safeZone);
        if (plugin.getConfig().getBoolean(Config.STORE_ZONES.getKey()))
            saveSafeZone(safeZone);
        return true;
    }

    public void deleteSafeZone(UUID ownerUuid) {
        SafeZone safeZone = safeZones.remove(ownerUuid);
        if (safeZone != null && plugin.getConfig().getBoolean(Config.STORE_ZONES.getKey()))
            removeSafeZoneFile(safeZone);
        if (plugin.hasWorldEditSupport()) {
            Player player;
            if ((player = Bukkit.getPlayer(ownerUuid)) != null)
                plugin.getWorldEditHook().removeCUI(player);
        }
    }

    public void deleteAllSafeZones() {
        if (plugin.hasWorldEditSupport())
            safeZones.values().forEach(safeZone -> {
                Player player;
                if ((player = Bukkit.getPlayer(safeZone.getOwnerUuid())) != null)
                    plugin.getWorldEditHook().removeCUI(player);
            });
        safeZones.clear();
    }

    public Collection<SafeZone> getSafeZones() {
        return Collections.unmodifiableCollection(safeZones.values());
    }

    private synchronized void removeExpired() {
        safeZones.values().removeIf(safeZone -> {
            if (safeZone.isExpired()) {
                Player player;
                if ((player = Bukkit.getPlayer(safeZone.getOwnerUuid())) != null)
                    player.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_EXPIRED.getKey())));
                if (plugin.getConfig().getBoolean(Config.STORE_ZONES.getKey()))
                    removeSafeZoneFile(safeZone);
                if (plugin.hasWorldEditSupport())
                    plugin.getWorldEditHook().removeCUI(player);
                return true;
            }
            return false;
        });
    }

    public Optional<SafeZone> findSafeZone(Location location) {
        return safeZones.values().stream().filter(safeZone -> safeZone.contains(location)).findFirst();
    }

    public Optional<SafeZone> findSafeZone(Player player) {
        return Optional.ofNullable(safeZones.get(player.getUniqueId()));
    }

    public Optional<SafeZone> findSafeZone(UUID ownerUuid) {
        return Optional.ofNullable(safeZones.get(ownerUuid));
    }

    private void saveSafeZone(SafeZone safeZone) {
        if (!zonesDir.exists())
            zonesDir.mkdirs();
        YamlConfiguration zoneConfig = YamlConfiguration
                .loadConfiguration(new File(zonesDir, safeZone.getOwnerUuid().toString() + ".yml"));
        zoneConfig.set("safeZone", safeZone);
        try {
            zoneConfig.save(new File(zonesDir, safeZone.getOwnerUuid().toString() + ".yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot save safe zone to config file");
        }
    }

    public void saveSafeZones() {
        if (!zonesDir.exists())
            zonesDir.mkdirs();
        for (SafeZone safeZone : safeZones.values())
            saveSafeZone(safeZone);
    }

    private void loadSafeZones() {
        if (!zonesDir.exists()) return;
        File[] files = zonesDir.listFiles();
        if (files == null) return;
        for (File file : files) {
            YamlConfiguration zoneConfig = YamlConfiguration.loadConfiguration(file);
            SafeZone zone = zoneConfig.getSerializable("safeZone", SafeZone.class);
            safeZones.put(zone.getOwnerUuid(), zone);
        }
    }

    private void removeSafeZoneFile(SafeZone safeZone) {
        File safeZoneFile = new File(zonesDir, safeZone.getOwnerUuid().toString() + ".yml");
        if (safeZoneFile.exists())
            safeZoneFile.delete();
    }

    public enum ZoneType {
        CUBOID,
        SPHERE
    }
}
