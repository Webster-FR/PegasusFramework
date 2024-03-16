package fr.pegasus.papermc.utilities.zones;

import fr.pegasus.papermc.utilities.commons.ObjectOperation;
import fr.pegasus.papermc.utilities.commons.enums.Directions;
import fr.pegasus.papermc.utilities.zones.events.*;
import fr.pegasus.papermc.utilities.zones.exceptions.MissingCornersException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class SharpZone implements Zone {
    // ABSTRACT
    public abstract Block[] corners ();
    public abstract Set<Block> vertices ();
    public abstract Set<Block> edges ();
    public abstract Map<Directions, Set<Block>> faces ();

    // GETTERS
    public @Nullable Block getMinCorner () {
        if (this.corners().length < 2) throw new MissingCornersException();

        final Block b1 = this.corners()[0];
        final Block b2 = this.corners()[1];

        return new Location(
                b1.getWorld(),
                Math.min(b1.getX(), b2.getX()),
                Math.min(b1.getY(), b2.getY()),
                Math.min(b1.getZ(), b2.getZ())
                ).getBlock();
    }
    public @Nullable Block getMaxCorner () {
        if (this.corners().length < 2) throw new MissingCornersException();

        final Block b1 = this.corners()[0];
        final Block b2 = this.corners()[1];

        return new Location(
                b1.getWorld(),
                Math.max(b1.getX(), b2.getX()),
                Math.max(b1.getY(), b2.getY()),
                Math.max(b1.getZ(), b2.getZ())
        ).getBlock();
    }

    // OVERRIDE
    @Override
    public boolean isIn (final @NotNull Location location) {
        final World world = location.getWorld();
        final double x    = location.getX();
        final double y    = location.getY();
        final double z    = location.getZ();

        assert Objects.nonNull(this.getMinCorner());
        assert Objects.nonNull(this.getMaxCorner());

        final int minX = this.getMinCorner().getX();
        final int minY = this.getMinCorner().getY();
        final int minZ = this.getMinCorner().getZ();
        final int maxX = this.getMaxCorner().getX();
        final int maxY = this.getMaxCorner().getY();
        final int maxZ = this.getMaxCorner().getZ();

        return ObjectOperation.equals(world, this.getMinCorner().getWorld(), this.getMaxCorner().getWorld())
                && maxX > x && x > minX
                && maxY > y && y > minY
                && maxZ > z && z > minZ;
    }
    // EVENTS
    @Override
    public void onEnter (final @NotNull Entity entity) {
        Bukkit.getServer().getPluginManager().callEvent(new ZoneEnterEvent(this, entity));
    }
    @Override
    public void onLeave (final @NotNull Entity entity) {
        Bukkit.getServer().getPluginManager().callEvent(new ZoneLeaveEvent(this, entity));
    }
    @Override
    public void onBlockPlace (final @NotNull Player player, final @NotNull Block block) {
        Bukkit.getServer().getPluginManager().callEvent(new ZoneBlockPlaceEvent(this, player, block));
    }
    @Override
    public void onBlockBreak (final @NotNull Player player, final @NotNull Block block) {
        Bukkit.getServer().getPluginManager().callEvent(new ZoneBlockBreakEvent(this, player, block));
    }
    @Override
    public void onEntityMove (final @NotNull Entity entity, final @NotNull Location from, final @NotNull Location to) {
        Bukkit.getServer().getPluginManager().callEvent(new ZoneEntityMoveEvent(this, entity, from, to));
    }
    @Override
    public void onEntityDamage (final @NotNull Entity entity, final @NotNull EntityDamageEvent.DamageCause cause, final double damage) {
        Bukkit.getServer().getPluginManager().callEvent(new ZoneEntityDamageEvent(this, entity, cause, damage));
    }
    @Override
    public void onEntityDamageByEntity (final @NotNull Entity victim, final @NotNull Entity damager, final double damage) {
        Bukkit.getServer().getPluginManager().callEvent(new ZoneEntityDamageByEntityEvent(this, victim, damager, damage));
    }
}
