package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.util.Config;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneExtend extends CommandBase {
    protected CommandZoneExtend(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "extend", "Extend safe zone time", Permissions.SAFE_ZONE_EXTEND);
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
        safeZone.setExpireAt(Instant.now().plus(plugin.getConfig().getInt(Config.LIFE_TIME.getKey()), ChronoUnit.MINUTES));
    }
}
