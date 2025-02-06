package net.nullpointer.simplesafezone.command.options;

import com.google.common.collect.Lists;
import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandOptionRule extends CommandBase {
    public CommandOptionRule(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "rule", "Change rule option", Permissions.SAFE_ZONE_OPTIONS_RULE, true);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Optional<SafeZone> zoneOptional = plugin.getSafeZoneManager().findSafeZone(player);
        if (!zoneOptional.isPresent()) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_NOT_FOUND.getKey())));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTER_RULE.getKey())));
            return;
        }

        SafeZone.Rule rule;
        try {
            rule = SafeZone.Rule.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.RULE_NOT_FOUND.getKey())));
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTER_BOOLEAN.getKey())));
            return;
        }

        boolean enabled = BooleanUtils.toBoolean(args[1]);
        SafeZone safeZone = zoneOptional.get();
        if (enabled) safeZone.enableRule(rule);
        else safeZone.disableRule(rule);

        sender.sendMessage(colorize(plugin.getMessages().getString(Messages.RULE_STATE_CHANGED.getKey()))
                .replace("%rule%", rule.name().toLowerCase())
                .replace("%state%", enabled ? "true" : "false")
        );
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return Arrays.stream(SafeZone.Rule.values())
                    .map(rule -> rule.name().toLowerCase())
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        if (args.length == 2)
            return Lists.newArrayList("true", "false").stream()
                    .filter(s -> s.startsWith(args[1]))
                    .collect(Collectors.toList());
        return Collections.emptyList();
    }
}
