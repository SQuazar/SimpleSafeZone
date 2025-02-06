package net.nullpointer.simplesafezone.util.bb;


import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CuboidBoundingBox extends BoundingBox implements ConfigurationSerializable {
    public static BoundingBox of(Location center, double x, double y, double z) {
        Validate.notNull(center, "Center is null!");
        return new CuboidBoundingBox(
                center.getWorld(),
                center.getX() - x,
                center.getY() - y,
                center.getZ() - z,
                center.getX() + x,
                center.getY() + y,
                center.getZ() + z
        );
    }

    public static BoundingBox of(Location center, double radius) {
        return CuboidBoundingBox.of(center, radius, radius, radius);
    }

    private final World world;
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;

    public CuboidBoundingBox(World world, double x1, double y1, double z1, double x2, double y2, double z2) {
        this.world = world;
        this.resize(x1, y1, z1, x2, y2, z2);
    }

    public CuboidBoundingBox resize(double x1, double y1, double z1, double x2, double y2, double z2) {
        NumberConversions.checkFinite(x1, "x1 not finite");
        NumberConversions.checkFinite(y1, "y1 not finite");
        NumberConversions.checkFinite(z1, "z1 not finite");
        NumberConversions.checkFinite(x2, "x2 not finite");
        NumberConversions.checkFinite(y2, "y2 not finite");
        NumberConversions.checkFinite(z2, "z2 not finite");

        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
        return this;
    }

    @Override
    public boolean contains(Location location) {
        Validate.notNull(location, "Location is null!");
        return contains(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return x >= this.minX && x < this.maxX && y >= this.minY && y < this.maxY && z >= this.minZ && z < this.maxZ;
    }

    @Override
    public Location getCenter() {
        return new Location(world, (minX + maxX) / 2., (minY + maxY) / 2., (minZ + maxZ) / 2.);
    }

    public Vector getMin() {
        return new Vector(minX, minY, minZ);
    }

    public Vector getMax() {
        return new Vector(maxX, maxY, maxZ);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", "cuboid");
        map.put("world", world.getUID().toString());
        map.put("minX", minX);
        map.put("minY", minY);
        map.put("minZ", minZ);
        map.put("maxX", maxX);
        map.put("maxY", maxY);
        map.put("maxZ", maxZ);
        return map;
    }

    public static CuboidBoundingBox deserialize(Map<String, Object> map) {
        Validate.notNull(map, "Map is null!");
        World world = Bukkit.getWorld((String) map.get("world"));
        double x = (Double) map.get("minX");
        double y = (Double) map.get("minY");
        double z = (Double) map.get("minZ");
        double x2 = (Double) map.get("maxX");
        double y2 = (Double) map.get("maxY");
        double z2 = (Double) map.get("maxZ");
        return new CuboidBoundingBox(world, x, y, z, x2, y2, z2);
    }
}
