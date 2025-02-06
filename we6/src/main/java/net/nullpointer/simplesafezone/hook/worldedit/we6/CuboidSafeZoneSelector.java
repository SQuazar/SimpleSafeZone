package net.nullpointer.simplesafezone.hook.worldedit.we6;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.world.World;

import javax.annotation.Nullable;

public class CuboidSafeZoneSelector extends CuboidRegionSelector {
    public CuboidSafeZoneSelector(@Nullable World world, Vector position1, Vector position2) {
        super(world, position1, position2);
    }
}
