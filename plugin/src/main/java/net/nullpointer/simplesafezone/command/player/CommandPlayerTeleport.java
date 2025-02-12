package net.nullpointer.simplesafezone.command.player;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.command.CommandBaseZone;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandPlayerTeleport extends CommandBaseZone {
    protected CommandPlayerTeleport(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "tp", "Teleport players in safe zone to your location",
                Permissions.SAFE_ZONE_PLAYER_TP);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        resolveSafeZone(player, safeZone -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (safeZone.contains(p))
                    p.teleport(player);
            }
            player.sendMessage(colorize(plugin.getMessages().getString(Messages.PLAYERS_TP.getKey())));
        });
    }
}
