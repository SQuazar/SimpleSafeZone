package net.nullpointer.simplesafezone.hook.worldedit.we6;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.world.World;
import net.nullpointer.simplesafezone.hook.worldedit.WorldEditHook;
import net.nullpointer.simplesafezone.util.bb.BoundingBox;
import net.nullpointer.simplesafezone.util.bb.CuboidBoundingBox;
import net.nullpointer.simplesafezone.util.bb.SphereBoundingBox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WEHook6 implements WorldEditHook {
    private final WorldEditPlugin worldEdit;

    public WEHook6() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (plugin == null) throw new NullPointerException("WorldEdit plugin not found");
        worldEdit = (WorldEditPlugin) plugin;
    }

    @Override
    public void displayCUI(Player player, BoundingBox boundingBox) {
        LocalSession session = worldEdit.getSession(player);
        World world = session.getSelectionWorld();
        Vector center = new Vector(
                boundingBox.getCenter().getX(),
                boundingBox.getCenter().getY(),
                boundingBox.getCenter().getZ()
        );
        RegionSelector regionSelector;
        if (boundingBox instanceof SphereBoundingBox) {
            SphereBoundingBox box = (SphereBoundingBox) boundingBox;
            regionSelector = new SphereSafeZoneSelector(world, center, (int) box.getRadius());
        } else {
            CuboidBoundingBox box = (CuboidBoundingBox) boundingBox;
            regionSelector = new CuboidSafeZoneSelector(world,
                    new Vector(box.getMin().getX(), box.getMin().getY(), box.getMin().getZ()),
                    new Vector(box.getMax().getX(), box.getMax().getY(), box.getMax().getZ())
            );
        }

        session.setRegionSelector(world, regionSelector);
        session.dispatchCUISelection(worldEdit.wrapPlayer(player));
    }

    @Override
    public void removeCUI(Player player) {
        LocalSession session = worldEdit.getSession(player);
        if (session.getRegionSelector(session.getSelectionWorld()) instanceof SphereSafeZoneSelector
                || session.getRegionSelector(session.getSelectionWorld()) instanceof CuboidSafeZoneSelector) {
            session.setRegionSelector(session.getSelectionWorld(), new CuboidRegionSelector());
            session.dispatchCUISelection(worldEdit.wrapPlayer(player));
        }
    }
}
