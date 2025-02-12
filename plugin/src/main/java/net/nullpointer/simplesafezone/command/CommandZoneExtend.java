package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.util.Config;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CommandZoneExtend extends CommandBaseZone {
    protected CommandZoneExtend(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "extend", "Extend safe zone time", Permissions.SAFE_ZONE_EXTEND);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        resolveSafeZone(player, safeZone ->
                safeZone.setExpireAt(Instant.now().plus(plugin.getConfig().getInt(Config.LIFE_TIME.getKey()), ChronoUnit.MINUTES)));
    }
}
