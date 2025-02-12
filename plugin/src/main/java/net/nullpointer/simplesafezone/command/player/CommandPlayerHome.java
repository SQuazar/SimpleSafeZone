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

public class CommandPlayerHome extends CommandBaseZone {
    protected CommandPlayerHome(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "home", "Teleport players to home", Permissions.SAFE_ZONE_PLAYER_HOME);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!plugin.hasEssentialsSupport()) return;
        Player player = (Player) sender;
        resolveSafeZone(player, safeZone -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p == player) continue;
                if (safeZone.contains(p))
                    plugin.getEssentialsHook().home(p);
            }

            player.sendMessage(colorize(plugin.getMessages().getString(Messages.PLAYERS_HOME.getKey())));
        });
    }

    @Override
    public boolean canTabComplete(CommandSender sender) {
        return super.canTabComplete(sender) && plugin.hasEssentialsSupport();
    }
}
