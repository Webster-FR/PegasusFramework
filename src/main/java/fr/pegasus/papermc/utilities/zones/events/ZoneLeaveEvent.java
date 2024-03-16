package fr.pegasus.papermc.utilities.zones.events;

import fr.pegasus.papermc.utilities.zones.Zone;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ZoneLeaveEvent extends ZoneEvent {
    // FIELDS
    private static final HandlerList HANDLERS = new HandlerList();
    private final Entity entity;

    // CONSTRUCTORS
    public ZoneLeaveEvent(final @NotNull Zone zone, final @NotNull Entity entity) {
        super(zone);
        this.entity = entity;
    }

    // GETTERS
    public static @NotNull HandlerList getHandlerList () {
        return HANDLERS;
    }
    public @NotNull Entity getEntity () {
        return this.entity;
    }

    // OVERRIDE
    @Override
    public @NotNull HandlerList getHandlers () {
        return HANDLERS;
    }
}
