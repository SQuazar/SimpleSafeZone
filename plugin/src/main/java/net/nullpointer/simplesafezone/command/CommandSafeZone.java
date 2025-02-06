package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;

public class CommandSafeZone extends CommandBase {
    public CommandSafeZone(SimpleSafeZone plugin) {
        super(plugin, null, "sz", "Safe zone base command", Permissions.SAFE_ZONE_COMMAND);
        registerSubCommand("help", new CommandHelp(plugin, this));
        registerSubCommand("create", new CommandCreateZone(plugin, this));
        registerSubCommand("delete", new CommandZoneDelete(plugin, this));
        registerSubCommand("option", new CommandZoneOptions(plugin, this));
        registerSubCommand("entity", new CommandZoneEntity(plugin, this));
        registerSubCommand("list", new CommandZoneList(plugin, this));
        registerSubCommand("info", new CommandZoneInfo(plugin, this));
        registerSubCommand("extend", new CommandZoneExtend(plugin, this));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) subCommands.get("help").onCommand(sender, args);
    }
}
