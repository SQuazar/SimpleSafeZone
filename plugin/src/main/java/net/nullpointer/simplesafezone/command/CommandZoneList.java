package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.manager.SafeZoneManager;
import net.nullpointer.simplesafezone.util.ColorUtils;
import net.nullpointer.simplesafezone.util.Permissions;
import net.nullpointer.simplesafezone.util.bb.SphereBoundingBox;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneList extends CommandBase {
    protected CommandZoneList(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "list", "Gets safe zones", Permissions.SAFE_ZONE_LIST);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (plugin.getSafeZoneManager().getSafeZones().isEmpty()) {
            sender.sendMessage(colorize("&3[SimpleSafeZone] &7No active safe zones"));
            return;
        }
        sender.sendMessage(colorize("&3[SimpleSafeZone] &7Active safe zones:\n")
                + plugin.getSafeZoneManager().getSafeZones().stream()
                .map(this::formatSafeZone)
                .map(ColorUtils::colorize)
                .collect(Collectors.joining("\n"))
        );
    }

    private String formatSafeZone(SafeZone safeZone) {
        Duration remaining = Duration.between(Instant.now(), safeZone.getExpireAt());
        String expireColor = "&a";
        if ((double) remaining.toMinutes() / plugin.getConfig().getInt("life-time") < 0.25)
            expireColor = "&c";
        else if ((double) remaining.toMinutes() / plugin.getConfig().getInt("life-time") < 0.5)
            expireColor = "&e";
        return String.format("  &b%s &7[&bx=&b%d&7, &by=%d&7, &bz=%d&7] &7(&6%s&7) %s(%02d:%02dm)",
                Bukkit.getOfflinePlayer(safeZone.getOwnerUuid()).getName(),
                safeZone.getBoundingBox().getCenter().getBlockX(),
                safeZone.getBoundingBox().getCenter().getBlockY(),
                safeZone.getBoundingBox().getCenter().getBlockZ(),
                safeZone.getBoundingBox() instanceof SphereBoundingBox ?
                        SafeZoneManager.ZoneType.SPHERE.name()
                        : SafeZoneManager.ZoneType.CUBOID.name(),
                expireColor,
                remaining.toMinutes(),
                remaining.minusMinutes(remaining.toMinutes()).getSeconds()
        );
    }
}
