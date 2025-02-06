package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.manager.SafeZoneManager;
import net.nullpointer.simplesafezone.util.Config;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandCreateZone extends CommandBase {
    public CommandCreateZone(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "create", "Create safe zone", Permissions.SAFE_ZONE_CREATE, true);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        int radius = plugin.getConfig().getInt(Config.SAFE_ZONE_DEFAULT_RADIUS.getKey());

        if (args.length > 0) {
            try {
                radius = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(colorize(plugin.getMessages().getString(Messages.INVALID_RADIUS.getKey())));
                return;
            }
        }
        SafeZoneManager.ZoneType type =
                SafeZoneManager.ZoneType.valueOf(plugin.getConfig().getString(Config.SAFE_ZONE_DEFAULT_TYPE.getKey()).toUpperCase());
        if (args.length > 1) {
            try {
                type = SafeZoneManager.ZoneType.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(colorize(plugin.getMessages().getString(Messages.TYPE_NOT_FOUND.getKey())));
                return;
            }
        }

        if (plugin.getSafeZoneManager().createSafeZone(player, type, radius)) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_CREATED.getKey())));

            if (plugin.hasWorldEditSupport())
                plugin.getSafeZoneManager().findSafeZone(player).ifPresent(zone ->
                        plugin.getWorldEditHook().displayCUI(player, zone.getBoundingBox())
                );
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2)
            return Arrays.stream(SafeZoneManager.ZoneType.values()).
                    map(type -> type.name().toLowerCase())
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        return Collections.emptyList();
    }
}
