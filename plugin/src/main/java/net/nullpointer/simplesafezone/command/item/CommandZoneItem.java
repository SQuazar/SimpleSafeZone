package net.nullpointer.simplesafezone.command.item;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.command.CommandBaseZone;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneItem extends CommandBaseZone {
    public CommandZoneItem(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "item", "Manage items in safe zone", Permissions.SAFE_ZONE_ITEM);
        registerSubCommand("tp", new CommandItemTeleport(plugin, this));
        registerSubCommand("remove", new CommandItemRemove(plugin, this));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTER_ACTION.getKey())));
    }
}
