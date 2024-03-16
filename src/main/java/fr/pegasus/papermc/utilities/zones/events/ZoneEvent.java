package fr.pegasus.papermc.utilities.zones.events;

import fr.pegasus.papermc.utilities.zones.Zone;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class ZoneEvent extends Event {
    // FIELDS
    private static final HandlerList HANDLERS = new HandlerList();
    private final Zone zone;

    // CONSTRUCTORS
    public ZoneEvent (final @NotNull Zone zone) {
        this.zone = zone;
    }

    // GETTERS
    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }
    public @NotNull Zone getZone () {
        return this.zone;
    }

    // OVERRIDE
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
