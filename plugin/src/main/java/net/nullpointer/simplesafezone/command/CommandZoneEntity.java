package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.command.entity.CommandEntityRemove;
import net.nullpointer.simplesafezone.command.entity.CommandEntityTeleport;
import net.nullpointer.simplesafezone.util.Messages;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandZoneEntity extends CommandBase {

    protected CommandZoneEntity(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "entity", "Manage of entity in zone", Permissions.SAFE_ZONE_ENTITY, true);
        registerSubCommand("remove", new CommandEntityRemove(plugin, this));
        registerSubCommand("tp", new CommandEntityTeleport(plugin, this));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0)
            sender.sendMessage(colorize(plugin.getMessages().getString(Messages.ENTER_ACTION.getKey())));
    }

    @Override
    public boolean canTabComplete(CommandSender sender) {
        Player player = (Player) sender;
        return plugin.getSafeZoneManager().findSafeZone(player.getUniqueId()).isPresent();
    }
}
