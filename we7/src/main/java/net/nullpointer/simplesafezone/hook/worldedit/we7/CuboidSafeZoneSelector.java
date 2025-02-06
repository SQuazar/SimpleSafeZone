package net.nullpointer.simplesafezone.hook.worldedit.we7;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.world.World;

import javax.annotation.Nullable;

public class CuboidSafeZoneSelector extends CuboidRegionSelector {
    public CuboidSafeZoneSelector(@Nullable World world, BlockVector3 position1, BlockVector3 position2) {
        super(world, position1, position2);
    }
}
