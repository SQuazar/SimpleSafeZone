package net.nullpointer.simplesafezone;

import net.nullpointer.simplesafezone.command.CommandSafeZone;
import net.nullpointer.simplesafezone.hook.worldedit.we6.WEHook6;
import net.nullpointer.simplesafezone.hook.worldedit.we7.WEHook7;
import net.nullpointer.simplesafezone.hook.worldedit.WorldEditHook;
import net.nullpointer.simplesafezone.listener.SafeZoneListener;
import net.nullpointer.simplesafezone.manager.SafeZoneManager;
import net.nullpointer.simplesafezone.util.Config;
import net.nullpointer.simplesafezone.util.bb.CuboidBoundingBox;
import net.nullpointer.simplesafezone.util.bb.SphereBoundingBox;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SimpleSafeZone extends JavaPlugin {
    private YamlConfiguration messages;
    private WorldEditHook worldEditHook;

    private SafeZoneManager safeZoneManager;

    @Override
    public void onEnable() {
        setupConfig();

        if (!setupWorldEditHook()) {
            getLogger().warning("WorldEdit is missing");
        }

        safeZoneManager = new SafeZoneManager(this);

        Bukkit.getPluginCommand("safezone").setExecutor(new CommandSafeZone(this));
        Bukkit.getPluginManager().registerEvents(new SafeZoneListener(safeZoneManager), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        if (getConfig().getBoolean(Config.STORE_ZONES.getKey()))
            safeZoneManager.saveSafeZones();
        safeZoneManager.deleteAllSafeZones();
    }

    public void reload() {
        reloadConfig();
        try {
            messages.load(new File(getDataFolder(), "messages.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("Cannot load messages.yml");
        }
    }

    public SafeZoneManager getSafeZoneManager() {
        return safeZoneManager;
    }

    public YamlConfiguration getMessages() {
        return messages;
    }

    public WorldEditHook getWorldEditHook() {
        return worldEditHook;
    }

    public boolean hasWorldEditSupport() {
        return worldEditHook != null;
    }

    private void setupConfig() {
        saveDefaultConfig();

        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists())
            saveResource("messages.yml", false);

        messages = YamlConfiguration.loadConfiguration(messagesFile);

        ConfigurationSerialization.registerClass(SafeZone.class);
        ConfigurationSerialization.registerClass(SphereBoundingBox.class);
        ConfigurationSerialization.registerClass(CuboidBoundingBox.class);
    }

    private boolean setupWorldEditHook() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null) return false;
        String version = plugin.getDescription().getVersion();
        if (version.startsWith("7")) {
            worldEditHook = new WEHook7();
            return true;
        }
        if (version.startsWith("6")) {
            worldEditHook = new WEHook6();
            return true;
        }
        return false;
    }
}
