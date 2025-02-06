package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.options.CommandOptionRule;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneOptions extends CommandBase {
    public CommandZoneOptions(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "option", "Safe zone options", Permissions.SAFE_ZONE_OPTIONS, true);
        registerSubCommand("rule", new CommandOptionRule(plugin, this));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTER_OPTION.getKey())));
    }

    @Override
    public boolean canTabComplete(CommandSender sender) {
        Player player = (Player) sender;
        return plugin.getSafeZoneManager().findSafeZone(player.getUniqueId()).isPresent();
    }
}
