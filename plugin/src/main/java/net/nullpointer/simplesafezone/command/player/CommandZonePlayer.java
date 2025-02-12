package net.nullpointer.simplesafezone.command.player;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.command.CommandBaseZone;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZonePlayer extends CommandBaseZone {
    public CommandZonePlayer(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "player", "Manage of players in zone", Permissions.SAFE_ZONE_PLAYER);
        registerSubCommand("tp", new CommandPlayerTeleport(plugin, this));
        if (plugin.hasEssentialsSupport())
            registerSubCommand("home", new CommandPlayerHome(plugin, this));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTER_ACTION.getKey())));
    }
}
