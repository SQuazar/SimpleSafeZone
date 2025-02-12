package net.nullpointer.simplesafezone.command.cui;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.CommandBase;
import net.nullpointer.simplesafezone.command.CommandBaseZone;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandZoneDesel extends CommandBaseZone {
    public CommandZoneDesel(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "desel", "Deselect safe zone CUI selection", Permissions.SAFE_ZONE_CUI);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        resolveSafeZone(player, safeZone -> plugin.getWorldEditHook().removeCUI(player));
    }

    @Override
    public boolean canTabComplete(CommandSender sender) {
        return super.canTabComplete(sender) && plugin.hasWorldEditSupport();
    }
}
