package net.nullpointer.simplesafezone.util.bb;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SphereBoundingBox extends BoundingBox implements ConfigurationSerializable {
    public static SphereBoundingBox of(Location location, double radius) {
        Validate.notNull(location, "Location is null!");
        return new SphereBoundingBox(location, radius);
    }

    private final Location center;
    private final double radiusSquared;

    public SphereBoundingBox(Location center, double radius) {
        this.center = center;
        this.radiusSquared = radius * radius;
    }

    @Override
    public boolean contains(Location location) {
        return contains(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public boolean contains(double x, double y, double z) {
        double dx = x - center.getX();
        double dy = y - center.getY();
        double dz = z - center.getZ();
        return (dx * dx + dy * dy + dz * dz) <= radiusSquared;
    }

    @Override
    public Location getCenter() {
        return center;
    }

    public double getRadius() {
        return Math.sqrt(radiusSquared);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", "sphere");
        map.put("center", center.serialize());
        map.put("radius-squared", radiusSquared);
        return map;
    }

    public static SphereBoundingBox deserialize(Map<String, Object> map) {
        Validate.notNull(map, "Map is null!");
        Location center = Location.deserialize((Map<String, Object>) map.get("center"));
        double radius = Double.parseDouble((String) map.get("radius-squared"));
        return new SphereBoundingBox(center, radius);
    }
}
