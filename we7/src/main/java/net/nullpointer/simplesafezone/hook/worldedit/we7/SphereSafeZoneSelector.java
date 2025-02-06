package net.nullpointer.simplesafezone.hook.worldedit.we7;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.selector.SphereRegionSelector;
import com.sk89q.worldedit.world.World;

import javax.annotation.Nullable;

public class SphereSafeZoneSelector extends SphereRegionSelector {
    public SphereSafeZoneSelector(@Nullable World world, BlockVector3 center, int radius) {
        super(world, center, radius);
    }
}
