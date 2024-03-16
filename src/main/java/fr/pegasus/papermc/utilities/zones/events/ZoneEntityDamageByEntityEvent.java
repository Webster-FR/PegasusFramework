package fr.pegasus.papermc.utilities.zones.events;

import fr.pegasus.papermc.utilities.zones.Zone;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ZoneEntityDamageByEntityEvent extends ZoneEvent {
    // FIELDS
    private static final HandlerList HANDLERS = new HandlerList();
    private final Entity victim, damager;
    private final double damage;

    // CONSTRUCTORS
    public ZoneEntityDamageByEntityEvent(final @NotNull Zone zone, final @NotNull Entity victim, final @NotNull Entity damager, final double damage) {
        super(zone);
        this.damage  = damage;
        this.victim  = victim;
        this.damager = damager;
    }

    // GETTERS
    public static @NotNull HandlerList getHandlerList () {
        return HANDLERS;
    }
    public @NotNull Entity getVictim () {
        return this.victim;
    }
    public @NotNull Entity getDamager () {
        return this.damager;
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
