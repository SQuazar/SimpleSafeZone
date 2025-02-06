package net.nullpointer.simplesafezone.hook.worldedit;

import net.nullpointer.simplesafezone.util.bb.BoundingBox;
import org.bukkit.entity.Player;

public interface WorldEditHook {
    void displayCUI(Player player, BoundingBox boundingBox);

    void removeCUI(Player player);
}
