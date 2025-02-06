package net.nullpointer.simplesafezone.command.cui;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneDesel extends CommandBase {
    public CommandZoneDesel(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "desel", "Deselect safe zone CUI selection", Permissions.SAFE_ZONE_CUI);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Optional<SafeZone> zoneOptional = plugin.getSafeZoneManager().findSafeZone(player);
        if (!zoneOptional.isPresent()) {
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.SAFE_ZONE_NOT_FOUND.getKey())));
            return;
        }
        SafeZone safeZone = zoneOptional.get();
        plugin.getWorldEditHook().removeCUI(player);
    }

    @Override
    public boolean canTabComplete(CommandSender sender) {
        Player player = (Player) sender;
        return plugin.getSafeZoneManager().findSafeZone(player.getUniqueId()).isPresent() && plugin.hasWorldEditSupport();
    }
}
