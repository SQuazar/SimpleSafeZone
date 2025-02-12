package net.nullpointer.simplesafezone.command.item;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.command.CommandBaseZone;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandItemTeleport extends CommandBaseZone {
    protected CommandItemTeleport(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "tp", "Teleport items in safe zone to your location",
                Permissions.SAFE_ZONE_ITEM_TP);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        resolveSafeZone(player, safeZone -> {
            int c = 0;
            for (Entity entity : safeZone.getWorld().getEntities()) {
                if (entity instanceof Item && safeZone.contains(entity)) {
                        entity.teleport(player);
                        c++;
                    }

            }
            player.sendMessage(colorize(plugin.getMessages().getString(Messages.ITEMS_TP.getKey())
                    .replace("%count%", String.valueOf(c)))
            );
        });
    }
}
