package net.nullpointer.simplesafezone.listener;

import net.nullpointer.simplesafezone.manager.SafeZoneManager;
import net.nullpointer.simplesafezone.util.Permissions;
import net.nullpointer.simplesafezone.zone.SafeZone;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class SafeZoneListener implements Listener {
    private final SafeZoneManager safeZoneManager;

    public SafeZoneListener(SafeZoneManager safeZoneManager) {
        this.safeZoneManager = safeZoneManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().hasPermission(Permissions.SAFE_ZONE_BYPASS))
            return;
        handleSafeZone(event.getBlock().getLocation(), SafeZone.Rule.BLOCK_BREAK, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().hasPermission(Permissions.SAFE_ZONE_BYPASS))
            return;
        handleSafeZone(event.getBlock().getLocation(), SafeZone.Rule.BLOCK_PLACE, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        handleSafeZone(event.getBlock().getLocation(), SafeZone.Rule.BLOCK_PHYSICS, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().hasPermission(Permissions.SAFE_ZONE_BYPASS)) return;
        handleSafeZone(event.getItem().getLocation(), SafeZone.Rule.PICKUP_ITEM, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().hasPermission(Permissions.SAFE_ZONE_BYPASS))
            return;
        handleSafeZone(event.getPlayer().getLocation(), SafeZone.Rule.DROP_ITEM, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDespawn(ItemDespawnEvent event) {
        handleSafeZone(event.getLocation(), SafeZone.Rule.ITEM_DESPAWN, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if (e.getDamager() instanceof Player && e.getDamager().hasPermission(Permissions.SAFE_ZONE_BYPASS))
                return;
        }
        handleSafeZone(event.getEntity().getLocation(), SafeZone.Rule.DAMAGE, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        handleSafeZone(event.getLocation(), SafeZone.Rule.EXPLODE, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockExplode(BlockExplodeEvent event) {
        handleSafeZone(event.getBlock().getLocation(), SafeZone.Rule.EXPLODE, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(BlockIgniteEvent event) {
        handleSafeZone(event.getBlock().getLocation(), SafeZone.Rule.IGNITE, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucket(PlayerBucketFillEvent event) {
        if (event.getPlayer().hasPermission(Permissions.SAFE_ZONE_BYPASS))
            return;
        handleSafeZone(event.getBlockClicked().getLocation(), SafeZone.Rule.BUCKET, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucket(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().hasPermission(Permissions.SAFE_ZONE_BYPASS))
            return;
        handleSafeZone(event.getBlockClicked().getLocation(), SafeZone.Rule.BUCKET, event);
    }

    private void handleSafeZone(Location location, SafeZone.Rule rule, Event event) {
        safeZoneManager.findSafeZone(location).ifPresent(zone ->
                zone.handleRule(rule, event)
        );
    }
}
