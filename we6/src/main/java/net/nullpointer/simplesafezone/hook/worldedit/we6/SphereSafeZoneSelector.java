package net.nullpointer.simplesafezone.hook.worldedit.we6;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.selector.SphereRegionSelector;
import com.sk89q.worldedit.world.World;

import javax.annotation.Nullable;

public class SphereSafeZoneSelector extends SphereRegionSelector {
    public SphereSafeZoneSelector(@Nullable World world, Vector center, int radius) {
        super(world, center, radius);
    }
}
