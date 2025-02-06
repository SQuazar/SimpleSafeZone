package net.nullpointer.simplesafezone.util.bb;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public abstract class BoundingBox implements ConfigurationSerializable {
    public abstract boolean contains(Location location);

    public abstract boolean contains(double x, double y, double z);

    public final boolean overlaps(BoundingBox other) {
        if (this instanceof CuboidBoundingBox && other instanceof CuboidBoundingBox) {
            CuboidBoundingBox otherCuboid = (CuboidBoundingBox) other;
            CuboidBoundingBox current = (CuboidBoundingBox) this;
            return current.getMax().getX() > otherCuboid.getMin().getX() && current.getMin().getX() < otherCuboid.getMax().getX()
                    && current.getMax().getY() > otherCuboid.getMin().getY() && current.getMin().getY() < otherCuboid.getMax().getY()
                    && current.getMax().getZ() > otherCuboid.getMin().getZ() && current.getMin().getZ() < otherCuboid.getMax().getZ();
        }

        else if (this instanceof SphereBoundingBox && other instanceof CuboidBoundingBox) {
            return sphereIntersectsCuboid((SphereBoundingBox) this, (CuboidBoundingBox) other);
        }

        else if (this instanceof CuboidBoundingBox && other instanceof SphereBoundingBox) {
            return sphereIntersectsCuboid((SphereBoundingBox) other, (CuboidBoundingBox) this);
        }

        else if (this instanceof SphereBoundingBox && other instanceof SphereBoundingBox) {
            SphereBoundingBox otherSphere = (SphereBoundingBox) other;
            SphereBoundingBox current = (SphereBoundingBox) this;

            double distanceSquared =
                    (current.getCenter().getX() - otherSphere.getCenter().getX()) * (current.getCenter().getX() - otherSphere.getCenter().getX()) +
                            (current.getCenter().getY() - otherSphere.getCenter().getY()) * (current.getCenter().getY() - otherSphere.getCenter().getY()) +
                            (current.getCenter().getZ() - otherSphere.getCenter().getZ()) * (current.getCenter().getZ() - otherSphere.getCenter().getZ());

            double radiusSum = current.getRadius() + otherSphere.getRadius();
            return distanceSquared <= (radiusSum * radiusSum);
        }

        return false;
    }

    private static boolean sphereIntersectsCuboid(SphereBoundingBox sphere, CuboidBoundingBox cuboid) {
        double closestX = Math.max(cuboid.getMin().getX(), Math.min(sphere.getCenter().getX(), cuboid.getMax().getX()));
        double closestY = Math.max(cuboid.getMin().getY(), Math.min(sphere.getCenter().getY(), cuboid.getMax().getY()));
        double closestZ = Math.max(cuboid.getMin().getZ(), Math.min(sphere.getCenter().getZ(), cuboid.getMax().getZ()));

        double distanceSquared = (closestX - sphere.getCenter().getX()) * (closestX - sphere.getCenter().getX()) +
                (closestY - sphere.getCenter().getY()) * (closestY - sphere.getCenter().getY()) +
                (closestZ - sphere.getCenter().getZ()) * (closestZ - sphere.getCenter().getZ());

        return distanceSquared <= (sphere.getRadius() * sphere.getRadius());
    }

    public abstract Location getCenter();
}
