package net.nullpointer.simplesafezone.zone;

import net.nullpointer.simplesafezone.util.bb.BoundingBox;
import net.nullpointer.simplesafezone.util.bb.CuboidBoundingBox;
import net.nullpointer.simplesafezone.util.bb.SphereBoundingBox;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class SafeZone implements ConfigurationSerializable {
    private final UUID ownerUuid;
    private final BoundingBox boundingBox;
    private final World world;
    private final Map<Rule, SafeZoneRule<? extends Event>> safeZoneRules = new HashMap<>();
    private Instant expireAt;

    public SafeZone(UUID owner, BoundingBox boundingBox, Instant expireAt) {
        this.ownerUuid = owner;
        this.boundingBox = boundingBox;
        this.expireAt = expireAt;
        this.world = boundingBox.getCenter().getWorld();
        this.setupRules();
    }

    public void setExpireAt(Instant extend) {
        this.expireAt = extend;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expireAt);
    }

    public Instant getExpireAt() {
        return expireAt;
    }

    public void enableRule(Rule rule) {
        Optional.ofNullable(safeZoneRules.get(rule))
                .ifPresent(r -> r.setEnabled(true));
    }

    public void disableRule(Rule rule) {
        Optional.ofNullable(safeZoneRules.get(rule))
                .ifPresent(r -> r.setEnabled(false));
    }

    public boolean isRuleEnabled(Rule rule) {
        return Optional.ofNullable(safeZoneRules.get(rule))
                .map(SafeZoneRule::isEnabled)
                .orElse(false);
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public World getWorld() {
        return world;
    }

    public boolean contains(Entity entity) {
        return boundingBox.contains(entity.getLocation());
    }

    public boolean contains(Location location) {
        return boundingBox.contains(location);
    }

    public boolean overlaps(SafeZone other) {
        return boundingBox.overlaps(other.boundingBox);
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void handleRule(Rule rule, T event) {
        if (isExpired()) return;
        Optional.ofNullable(safeZoneRules.get(rule))
                .ifPresent(safeZoneRule ->
                        ((SafeZoneRule<T>) safeZoneRule).handleEvent(event)
                );
    }

    private void setupRules() {
        safeZoneRules.put(Rule.BLOCK_BREAK, new SafeZoneRule<>());
        safeZoneRules.put(Rule.BLOCK_PLACE, new SafeZoneRule<>());
        safeZoneRules.put(Rule.BLOCK_PHYSICS, new SafeZoneRule<>());
        safeZoneRules.put(Rule.PICKUP_ITEM, new SafeZoneRule<>());
        safeZoneRules.put(Rule.DROP_ITEM, new SafeZoneRule<>());
        safeZoneRules.put(Rule.ITEM_DESPAWN, new SafeZoneRule<>());
        safeZoneRules.put(Rule.DAMAGE, new SafeZoneRule<>());
        safeZoneRules.put(Rule.EXPLODE, new SafeZoneRule<>());
        safeZoneRules.put(Rule.IGNITE, new SafeZoneRule<>());
        safeZoneRules.put(Rule.BUCKET, new SafeZoneRule<>());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("owner", ownerUuid.toString());
        map.put("bounding-box", boundingBox.serialize());
        map.put("expire-at", expireAt.getEpochSecond());
        map.put("rules", safeZoneRules.entrySet().stream()
                .collect(Collectors.toMap(
                        a -> a.getKey().name().toLowerCase(),
                        entry -> entry.getValue().isEnabled())
                ));
        return map;
    }

    public static SafeZone deserialize(Map<String, Object> map) {
        UUID owner = UUID.fromString((String) map.get("owner"));
        Map<String, Object> bbOptions = (Map<String, Object>) map.get("bounding-box");
        String bbType = (String) (bbOptions).get("type");
        BoundingBox box;
        if (bbType.equals("cuboid"))
            box = CuboidBoundingBox.deserialize(bbOptions);
        else if (bbType.equals("sphere"))
            box = SphereBoundingBox.deserialize(bbOptions);
        else throw new IllegalArgumentException("Unknown box type: " + bbType);
        Instant expireAt = Instant.ofEpochSecond(((Integer) map.get("expire-at")).longValue());
        SafeZone safeZone = new SafeZone(owner, box, expireAt);
        Map<String, Object> rules = (Map<String, Object>) map.get("rules");
        safeZone.safeZoneRules.putAll(rules.entrySet().stream()
                .collect(Collectors.toMap(
                        a -> Rule.valueOf(a.getKey().toUpperCase()),
                        a -> new SafeZoneRule<>((Boolean) a.getValue())
                )));
        return safeZone;
    }

    public enum Rule {
        BLOCK_BREAK,
        BLOCK_PLACE,
        BLOCK_PHYSICS,
        PICKUP_ITEM,
        DROP_ITEM,
        ITEM_DESPAWN,
        DAMAGE,
        EXPLODE,
        IGNITE,
        BUCKET,
    }
}
