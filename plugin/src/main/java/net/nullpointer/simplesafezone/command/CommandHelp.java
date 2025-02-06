package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandHelp extends CommandBase {
    public CommandHelp(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "help", "Gets plugin help", Permissions.SAFE_ZONE_COMMAND);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelp(sender, parent);
            return;
        }

        String arg = args[0];
        CommandBase find = findSubCommand(parent, arg);
        if (find == null) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.UNKNOWN_COMMAND.getKey())));
            return;
        }

        sendHelp(sender, find);
    }

    private void sendHelp(CommandSender sender, CommandBase command) {
        String help = "&b[SimpleSafeZone] &7Help for &d/" + command.name + "&7&r\n" +
                String.format("  &b/%s &7- %s", String.join(" ", getCommandPath(command)), command.description) +
                '\n' +
                resolveCommands(command).stream()
                        .map(cmd -> {
                            List<String> path = getCommandPath(cmd);
                            return "  &b/" + String.join(" ", path) + " &7- " + cmd.description;
                        })
                        .collect(Collectors.joining("\n"));
        sender.sendMessage(colorize(help));
    }

    private List<String> getCommandPath(CommandBase command) {
        List<String> path = new ArrayList<>();
        while (command != null) {
            path.add(command.name);
            command = command.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private Collection<CommandBase> resolveCommands(CommandBase commandBase) {
        return resolveCommands(new ArrayList<>(), commandBase);
    }

    private Collection<CommandBase> resolveCommands(Collection<CommandBase> dump, CommandBase commandBase) {
        if (commandBase.subCommands.isEmpty()) return dump;
        for (CommandBase base : commandBase.subCommands.values()) {
            dump.add(base);
            resolveCommands(new ArrayList<>(), base);
        }
        return dump;
    }

    private CommandBase findSubCommand(CommandBase parent, String name) {
        if (parent.subCommands.containsKey(name)) return parent.subCommands.get(name);
        for (CommandBase subCommand : parent.subCommands.values()) {
            CommandBase found = findSubCommand(subCommand, name);
            if (found != null) return found;
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return parent.subCommands.values().stream()
                    .map(c -> c.name)
                    .filter(c -> c.startsWith(args[0]))
                    .collect(Collectors.toList());
        return Collections.emptyList();
    }
}
