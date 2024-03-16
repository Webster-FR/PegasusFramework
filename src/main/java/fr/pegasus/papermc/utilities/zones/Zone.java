package fr.pegasus.papermc.utilities.zones;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public interface Zone {
    boolean isIn (final @NotNull Location location);
    void onEnter (final @NotNull Entity entity);
    void onLeave (final @NotNull Entity entity);
    void onBlockPlace (final @NotNull Player player, final @NotNull Block block);
    void onBlockBreak (final @NotNull Player player, final @NotNull Block block);
    void onEntityMove (final @NotNull Entity entity, final @NotNull Location from, final @NotNull Location to);
    void onEntityDamage (final @NotNull Entity entity, final @NotNull EntityDamageEvent.DamageCause cause, final double damage);
    void onEntityDamageByEntity (final @NotNull Entity victim, final @NotNull Entity damager, final double damage);
}
