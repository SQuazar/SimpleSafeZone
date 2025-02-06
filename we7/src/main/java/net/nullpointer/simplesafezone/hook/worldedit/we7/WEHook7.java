package net.nullpointer.simplesafezone.hook.worldedit.we7;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.regions.selector.SphereRegionSelector;
import com.sk89q.worldedit.world.World;
import net.nullpointer.simplesafezone.hook.worldedit.WorldEditHook;
import net.nullpointer.simplesafezone.util.bb.BoundingBox;
import net.nullpointer.simplesafezone.util.bb.CuboidBoundingBox;
import net.nullpointer.simplesafezone.util.bb.SphereBoundingBox;
import org.bukkit.entity.Player;

public class WEHook7 implements WorldEditHook {
    @Override
    public void displayCUI(Player player, BoundingBox boundingBox) {
        RegionSelector regionSelector;
        World world = BukkitAdapter.adapt(player.getWorld());
        BlockVector3 center = BukkitAdapter.asBlockVector(boundingBox.getCenter());
        if (boundingBox instanceof SphereBoundingBox) {
            SphereBoundingBox box = (SphereBoundingBox) boundingBox;
            regionSelector = new SphereSafeZoneSelector(world, center, (int) box.getRadius());
        } else {
            CuboidBoundingBox box = (CuboidBoundingBox) boundingBox;
            regionSelector = new CuboidSafeZoneSelector(world,
                    BlockVector3.at(box.getMin().getX(), box.getMin().getY(), box.getMin().getZ()),
                    BlockVector3.at(box.getMax().getX(), box.getMax().getY(), box.getMax().getZ())
            );
        }

        com.sk89q.worldedit.entity.Player worldEditPlayer = BukkitAdapter.adapt(player);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(worldEditPlayer);
        session.setRegionSelector(world, regionSelector);
        session.dispatchCUISelection(worldEditPlayer);
    }

    @Override
    public void removeCUI(Player player) {
        World world = BukkitAdapter.adapt(player.getWorld());
        com.sk89q.worldedit.entity.Player worldEditPlayer = BukkitAdapter.adapt(player);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(worldEditPlayer);
        if (session.getRegionSelector(world) instanceof CuboidRegionSelector
                || session.getRegionSelector(world) instanceof SphereRegionSelector) {
            session.setRegionSelector(world, new CuboidRegionSelector());
            session.dispatchCUISelection(worldEditPlayer);
        }
    }
}
