package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.manager.SafeZoneManager;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import net.nullpointer.simplesafezone.util.bb.SphereBoundingBox;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneInfo extends CommandBase {
    protected CommandZoneInfo(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "info", "Gets info of your safe zone", Permissions.SAFE_ZONE_COMMAND, true);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Optional<SafeZone> zoneOptional = plugin.getSafeZoneManager().findSafeZone(player);
        if (!zoneOptional.isPresent()) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_NOT_FOUND.getKey())));
            return;
        }
        SafeZone safeZone = zoneOptional.get();
        StringBuilder info = new StringBuilder("&b[SimpleSafeZone] &7Safe zone info:\n");
        info.append("  &7Owner: &b").append(player.getName()).append("\n&r");
        info.append("  &7Type: &b").append(safeZone.getBoundingBox() instanceof SphereBoundingBox ?
                        SafeZoneManager.ZoneType.SPHERE.name() : SafeZoneManager.ZoneType.CUBOID.name())
                .append("\n&r");

        Duration remaining = Duration.between(Instant.now(), safeZone.getExpireAt());
        info.append("  &7Expire at: &b").append(String.format("%02d:%02dm",
                        remaining.toMinutes(), remaining.minusMinutes(remaining.toMinutes()).getSeconds()))
                .append("\n&r");
        info.append("  &7Rules:").append("\n&r");
        info.append(Arrays.stream(SafeZone.Rule.values())
                .map(rule -> String.format("   &7- &b%s&7: %s%s",
                        rule.name().toLowerCase(),
                        safeZone.isRuleEnabled(rule) ? "&a" : "&c",
                        safeZone.isRuleEnabled(rule) ? "true" : "false"))
                .collect(Collectors.joining("\n"))
        );
        sender.sendMessage(colorize(info.toString()));
    }

    @Override
    public boolean canTabComplete(CommandSender sender) {
        Player player = (Player) sender;
        return plugin.getSafeZoneManager().findSafeZone(player.getUniqueId()).isPresent();
    }
}
