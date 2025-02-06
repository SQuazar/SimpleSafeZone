package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneDelete extends CommandBase {

    protected CommandZoneDelete(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "delete", "Delete safe zone", Permissions.SAFE_ZONE_CREATE);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission(Permissions.SAFE_ZONE_DELETE_OTHER) && args.length == 1) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            Optional<SafeZone> safeZoneOptional = plugin.getSafeZoneManager().findSafeZone(offlinePlayer.getUniqueId());
            if (safeZoneOptional.isPresent()) {
                plugin.getSafeZoneManager().deleteSafeZone(offlinePlayer.getUniqueId());
                sender.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_DELETED.getKey()))
                        .replace("%owner_name%", offlinePlayer.getName())
                );
            } else sender.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_NOT_FOUND.getKey())));
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ONLY_PLAYERS.getKey())));
            return;
        }

        Player player = (Player) sender;
        Optional<SafeZone> safeZoneOptional = plugin.getSafeZoneManager().findSafeZone(player.getUniqueId());
        if (safeZoneOptional.isPresent()) {
            plugin.getSafeZoneManager().deleteSafeZone(player.getUniqueId());
            player.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_DELETED.getKey()))
                    .replace("%owner_name%", player.getName())
            );
            if (plugin.hasWorldEditSupport())
                plugin.getWorldEditHook().removeCUI(player);
            return;
        }
        sender.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_NOT_FOUND.getKey())));
    }

    @Override
    public boolean canTabComplete(CommandSender sender) {
        Player player = (Player) sender;
        return plugin.getSafeZoneManager().findSafeZone(player.getUniqueId()).isPresent();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1 && sender.hasPermission(Permissions.SAFE_ZONE_DELETE_OTHER))
            return plugin.getSafeZoneManager().getSafeZones().stream()
                    .map(safeZone -> Bukkit.getOfflinePlayer(safeZone.getOwnerUuid()).getName())
                    .filter(s -> s.startsWith(args[0]))
                    .collect(Collectors.toList());
        return Collections.emptyList();
    }
}
