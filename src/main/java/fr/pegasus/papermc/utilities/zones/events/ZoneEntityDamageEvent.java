package fr.pegasus.papermc.utilities.zones.events;

import fr.pegasus.papermc.utilities.zones.Zone;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class ZoneEntityDamageEvent extends ZoneEvent {
    // FIELDS
    private static final HandlerList HANDLERS = new HandlerList();
    private final Entity entity;
    private final EntityDamageEvent.DamageCause cause;
    private final double damage;

    // CONSTRUCTORS
    public ZoneEntityDamageEvent(final @NotNull Zone zone, final @NotNull Entity entity, final @NotNull EntityDamageEvent.DamageCause cause, final double damage) {
        super(zone);
        this.cause  = cause;
        this.entity = entity;
        this.damage = damage;
    }

    // GETTERS
    public static @NotNull HandlerList getHandlerList () {
        return HANDLERS;
    }
    public @NotNull Entity getEntity () {
        return this.entity;
    }
    public @NotNull EntityDamageEvent.DamageCause getCause () {
        return this.cause;
    }
    public double getDamage () {
        return this.damage;
    }

    // OVERRIDE
    @Override
    public @NotNull HandlerList getHandlers () {
        return HANDLERS;
    }
}
