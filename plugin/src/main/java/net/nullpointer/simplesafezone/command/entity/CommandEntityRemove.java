package net.nullpointer.simplesafezone.command.entity;

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

public class CommandEntityRemove extends CommandBaseZone {
    public CommandEntityRemove(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "remove", "Remove entities in safe zone",
                Permissions.SAFE_ZONE_ENTITY_REMOVE);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        resolveSafeZone(player, safeZone -> {
            int c = 0;
            for (Entity entity : safeZone.getWorld().getEntities()) {
                if (!(entity instanceof Player) && !(entity instanceof Item) && safeZone.contains(entity)) {
                    entity.remove();
                    c++;
                }

            }

            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTITIES_REMOVED.getKey()))
                    .replace("%count%", String.valueOf(c))
            );
        });
    }
}
