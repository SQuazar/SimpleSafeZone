package net.nullpointer.simplesafezone.command.entity;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.command.CommandBaseZone;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneEntity extends CommandBaseZone {

    public CommandZoneEntity(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "entity", "Manage of entity in zone", Permissions.SAFE_ZONE_ENTITY);
        registerSubCommand("remove", new CommandEntityRemove(plugin, this));
        registerSubCommand("tp", new CommandEntityTeleport(plugin, this));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTER_ACTION.getKey())));
    }
}
