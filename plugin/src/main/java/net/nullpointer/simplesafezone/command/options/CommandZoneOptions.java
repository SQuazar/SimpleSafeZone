package net.nullpointer.simplesafezone.command.options;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.command.CommandBaseZone;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneOptions extends CommandBaseZone {
    public CommandZoneOptions(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "option", "Safe zone options", Permissions.SAFE_ZONE_OPTIONS);
        registerSubCommand("rule", new CommandOptionRule(plugin, this));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTER_OPTION.getKey())));
    }
}
