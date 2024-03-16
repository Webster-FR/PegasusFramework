package fr.pegasus.papermc.utilities.zones.events;

import fr.pegasus.papermc.utilities.zones.Zone;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ZoneEntityMoveEvent extends ZoneEvent {
    // FIELDS
    private static final HandlerList HANDLERS = new HandlerList();
    private final Entity entity;
    private final Location from, to;

    // CONSTRUCTORS
    public ZoneEntityMoveEvent(final @NotNull Zone zone, final @NotNull Entity entity, final @NotNull Location from, final @NotNull Location to) {
        super(zone);
        this.to     = to;
        this.from   = from;
        this.entity = entity;
    }

    // GETTERS
    public static @NotNull HandlerList getHandlerList () {
        return HANDLERS;
    }
    public @NotNull Entity getEntity () {
        return this.entity;
    }
    public @NotNull Location getFrom () {
        return this.from;
    }
    public @NotNull Location getTo () {
        return this.to;
    }

    // OVERRIDE
    @Override
    public @NotNull HandlerList getHandlers () {
        return HANDLERS;
    }
}
