package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.function.Consumer;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public abstract class CommandBaseZone extends CommandBase {
    protected CommandBaseZone(SimpleSafeZone plugin, CommandBase parent, String name, String description, String permission) {
        super(plugin, parent, name, description, permission, true);
    }

    protected void resolveSafeZone(Player player, Consumer<SafeZone> callback) {
        Optional<SafeZone> safeZoneOptional = plugin.getSafeZoneManager().findSafeZone(player);
        if (!safeZoneOptional.isPresent()) {
            player.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_NOT_FOUND.getKey())));
            return;
        }
        callback.accept(safeZoneOptional.get());
    }

    @Override
    public boolean canTabComplete(CommandSender sender) {
        Player player = (Player) sender;
        return plugin.getSafeZoneManager().findSafeZone(player.getUniqueId()).isPresent();
    }
}
