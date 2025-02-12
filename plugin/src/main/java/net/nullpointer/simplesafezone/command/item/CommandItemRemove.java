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

public class CommandItemRemove extends CommandBaseZone {
    protected CommandItemRemove(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "remove", "Remove items in safe zone", Permissions.SAFE_ZONE_ITEM_REMOVE);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        resolveSafeZone(player, safeZone -> {
            int c = 0;
            for (Entity entity : safeZone.getWorld().getEntities()) {
                if (entity instanceof Item && safeZone.contains(entity)) {
                    entity.remove();
                    c++;
                }
            }

            player.sendMessage(colorize(plugin.getMessages().getString(Messages.ITEMS_REMOVED.getKey())
                    .replace("%count%", String.valueOf(c)))
            );
        });
    }
}
