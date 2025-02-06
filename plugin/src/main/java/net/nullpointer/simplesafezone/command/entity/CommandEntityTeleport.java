package net.nullpointer.simplesafezone.command.entity;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandEntityTeleport extends CommandBase {
    public CommandEntityTeleport(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "tp", "Teleport entities in safe zone to your location",
                Permissions.SAFE_ZONE_ENTITY_TP, true);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Optional<SafeZone> safeZoneOptional = plugin.getSafeZoneManager().findSafeZone(player);
        if (!safeZoneOptional.isPresent()) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_NOT_FOUND.getKey())));
            return;
        }
        SafeZone safeZone = safeZoneOptional.get();
        int c = 0;
        for (Entity entity : safeZone.getWorld().getEntities()) {
            if (!(entity instanceof Player) && !(entity instanceof Item) && safeZone.contains(entity)) {
                entity.teleport(player);
                c++;
            }
        }

        sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTITIES_TP.getKey()))
                .replace("%count%", String.valueOf(c))
        );
    }
}
