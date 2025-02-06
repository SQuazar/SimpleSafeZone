package net.nullpointer.simplesafezone.zone;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public final class SafeZoneRule<T extends Event> {
    private boolean enabled;

    public SafeZoneRule(boolean enabled) {
        this.enabled = enabled;
    }

    public SafeZoneRule() {
        this.enabled = true;
    }

    public void handleEvent(T event) {
        if (!enabled) return;
        if (event instanceof Cancellable) {
            Cancellable cancellable = (Cancellable) event;
            cancellable.setCancelled(true);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
