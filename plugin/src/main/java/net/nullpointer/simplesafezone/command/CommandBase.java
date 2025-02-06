package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public abstract class CommandBase implements TabExecutor {
    protected final SimpleSafeZone plugin;
    protected final CommandBase parent;
    protected final String name;
    protected final String description;
    protected final String permission;
    protected final Map<String, CommandBase> subCommands = new HashMap<>();
    private boolean onlyPlayers;

    protected CommandBase(SimpleSafeZone plugin, CommandBase parent, String name, String description, String permission) {
        this.plugin = plugin;
        this.parent = parent;
        this.name = name;
        this.description = description;
        this.permission = permission;
    }

    protected CommandBase(SimpleSafeZone plugin, CommandBase parent, String name, String description, String permission, boolean onlyPlayers) {
        this.plugin = plugin;
        this.parent = parent;
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.onlyPlayers = onlyPlayers;
    }

    public abstract void onCommand(CommandSender sender, String[] args);

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return subCommands.values().stream()
                    .filter(c -> sender.hasPermission(c.permission) && c.canTabComplete(sender))
                    .map(c -> c.name)
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        return Collections.emptyList();
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.NO_PERMISSION.getKey())));
            return true;
        }

        if (onlyPlayers && !(sender instanceof Player)) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ONLY_PLAYERS.getKey())));
            return true;
        }

        if (args.length == 0) {
            onCommand(sender, args);
            return true;
        }

        String arg = args[0];
        CommandBase subCommand = subCommands.get(arg);
        if (subCommand == null) {
            onCommand(sender, args);
            return true;
        }

        args = Arrays.copyOfRange(args, 1, args.length);
        subCommand.onCommand(sender, command, s, args);
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) return onTabComplete(sender, args);
        String arg = args[0];
        CommandBase subCommand = subCommands.get(arg);
        if (subCommand == null) return onTabComplete(sender, args);
        args = Arrays.copyOfRange(args, 1, args.length);
        if (subCommand.canTabComplete(sender))
            return subCommand.onTabComplete(sender, command, s, args);
        return Collections.emptyList();
    }

    public boolean canTabComplete(CommandSender sender) {
        return true;
    }

    public final void registerSubCommand(String name, CommandBase subCommand) {
        subCommands.put(name, subCommand);
    }
}
