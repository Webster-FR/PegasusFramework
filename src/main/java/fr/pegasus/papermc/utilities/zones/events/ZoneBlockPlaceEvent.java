package fr.pegasus.papermc.utilities.zones.events;

import fr.pegasus.papermc.utilities.zones.Zone;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ZoneBlockPlaceEvent extends ZoneEvent {
    // FIELDS
    private static final HandlerList HANDLERS = new HandlerList();
    private final Block block;
    private final Player player;

    // CONSTRUCTORS
    public ZoneBlockPlaceEvent(final @NotNull Zone zone, final @NotNull Player player, final @NotNull Block block) {
        super(zone);
        this.block  = block;
        this.player = player;
    }

    // GETTERS
    public static @NotNull HandlerList getHandlerList () {
        return HANDLERS;
    }
    public @NotNull Player getPlayer () {
        return this.player;
    }
    public @NotNull Block getPlacedBlock () {
        return this.block;
    }

    // OVERRIDE
    @Override
    public @NotNull HandlerList getHandlers () {
        return HANDLERS;
    }
}
