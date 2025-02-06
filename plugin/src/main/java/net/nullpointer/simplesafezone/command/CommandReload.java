package net.nullpointer.simplesafezone.command;

import net.nullpointer.simplesafezone.SimpleSafeZone;
import net.nullpointer.simplesafezone.util.Permissions;
import org.bukkit.command.CommandSender;

import static net.nullpointer.simplesafezone.util.ColorUtils.colorize;

public class CommandReload extends CommandBase {
    protected CommandReload(SimpleSafeZone plugin, CommandBase parent) {
        super(plugin, parent, "reload", "Reload plugin", Permissions.RELOAD);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        plugin.reload();
        sender.sendMessage(colorize("&d[SimpleSafeZone] &aPlugin successfully reloaded"));
    }
}
