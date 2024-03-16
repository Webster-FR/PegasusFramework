package fr.pegasus.papermc.utilities.zones.shapes;

import fr.pegasus.papermc.utilities.locations.Locations;
import fr.pegasus.papermc.utilities.commons.enums.Directions;
import fr.pegasus.papermc.utilities.zones.SharpZone;
import fr.pegasus.papermc.utilities.zones.exceptions.ConflictingWorldException;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CuboidZone extends SharpZone implements Listener {
    // FIELDS
    private final World world;
    private final Block corner1, corner2;
    private Set<Block> _vertices;
    private Set<Block> _edges;
    private Map<Directions, Set<Block>> _faces;

    // CONSTRUCTORS
    public CuboidZone (final Plugin plugin, final @NotNull Block corner1, final @NotNull Block corner2) {
        if (!Objects.equals(corner1.getWorld(), corner2.getWorld())) throw new ConflictingWorldException();

        this.corner1 = corner1;
        this.corner2 = corner2;
        this.world   = corner1.getWorld();

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // METHODS
    public void drawVertices (final @Nullable Material material) {
        this.vertices().forEach(b -> b.setType(Objects.isNull(material) ? Material.AIR : material));
    }
    public void drawEdges (final @Nullable Material material) {
        this.edges().forEach(b -> b.setType(Objects.isNull(material) ? Material.AIR : material));
    }
    public void drawFace (final @NotNull Directions direction, final @Nullable Material material) {
        this.faces().get(direction).forEach(b -> b.setType(Objects.isNull(material) ? Material.AIR : material));
    }
    public void drawFaces (final @Nullable Material material) {
        this.faces().keySet().forEach(k -> this.drawFace(k, material));
    }

    // OVERRIDE
    @Override
    public Block[] corners() {
        return new Block[] { corner1, corner2 };
    }

    @Override
    public Set<Block> vertices() {
        if (Objects.isNull(this._vertices) || this._vertices.isEmpty()) this.computeVertices();
        return this._vertices;
    }
    private void computeVertices () {
        assert Objects.nonNull(super.getMinCorner());
        assert Objects.nonNull(super.getMaxCorner());

        final int minX = super.getMinCorner().getX();
        final int minY = super.getMinCorner().getY();
        final int minZ = super.getMinCorner().getZ();
        final int maxX = super.getMaxCorner().getX();
        final int maxY = super.getMaxCorner().getY();
        final int maxZ = super.getMaxCorner().getZ();

        this._vertices = Set.of(
                Locations.blockFromCoordinates(this.world, minX, minY, minZ),
                Locations.blockFromCoordinates(this.world, maxX, maxY, maxZ),
                Locations.blockFromCoordinates(this.world, minX, minY, maxZ),
                Locations.blockFromCoordinates(this.world, minX, maxY, minZ),
                Locations.blockFromCoordinates(this.world, maxX, minY, minZ),
                Locations.blockFromCoordinates(this.world, maxX, maxY, minZ),
                Locations.blockFromCoordinates(this.world, maxX, minY, maxZ),
                Locations.blockFromCoordinates(this.world, minX, maxY, maxZ)
        );
    }

    @Override
    public Set<Block> edges() {
        if (Objects.isNull(this._edges) || this._edges.isEmpty()) this.computeEdges();
        return this._edges;
    }
    private void computeEdges () {
        this._edges = new HashSet<Block>();

        assert Objects.nonNull(super.getMinCorner());
        assert Objects.nonNull(super.getMaxCorner());

        final int minX = super.getMinCorner().getX();
        final int minY = super.getMinCorner().getY();
        final int minZ = super.getMinCorner().getZ();
        final int maxX = super.getMaxCorner().getX();
        final int maxY = super.getMaxCorner().getY();
        final int maxZ = super.getMaxCorner().getZ();

        /*
            E***********F
          * *         * *
        D***********C   *
        *   H***********G
        * *         * *
        A***********B
         */

        // A -> B, D -> C, E -> F, H -> G
        for (int x = minX; x < maxX; ++x) {
            this._edges.add(Locations.blockFromCoordinates(this.world, x, minY, minZ));
            this._edges.add(Locations.blockFromCoordinates(this.world, x, maxY, minZ));
            this._edges.add(Locations.blockFromCoordinates(this.world, x, minY, maxZ));
            this._edges.add(Locations.blockFromCoordinates(this.world, x, maxY, maxZ));
        }
        // A -> D, B -> C, G -> F, H -> E
        for (int y = minY; y < maxY; ++y) {
            this._edges.add(Locations.blockFromCoordinates(this.world, minX, y, minZ));
            this._edges.add(Locations.blockFromCoordinates(this.world, maxX, y, minZ));
            this._edges.add(Locations.blockFromCoordinates(this.world, minX, y, maxZ));
            this._edges.add(Locations.blockFromCoordinates(this.world, maxX, y, maxZ));
        }
        // B -> G, A -> H, D -> E, C -> F
        for (int z = minZ; z < maxZ; ++z) {
            this._edges.add(Locations.blockFromCoordinates(this.world, minX, minY, z));
            this._edges.add(Locations.blockFromCoordinates(this.world, minX, maxY, z));
            this._edges.add(Locations.blockFromCoordinates(this.world, maxX, minY, z));
            this._edges.add(Locations.blockFromCoordinates(this.world, maxX, maxY, z));
        }
    }

    @Override
    public Map<Directions, Set<Block>> faces() {
        if (Objects.isNull(this._faces) || this._faces.isEmpty()) this.computeFaces();
        return this._faces;
    }
    private void computeFaces () {
        assert Objects.nonNull(super.getMinCorner());
        assert Objects.nonNull(super.getMaxCorner());

        final int minX = super.getMinCorner().getX();
        final int minY = super.getMinCorner().getY();
        final int minZ = super.getMinCorner().getZ();
        final int maxX = super.getMaxCorner().getX();
        final int maxY = super.getMaxCorner().getY();
        final int maxZ = super.getMaxCorner().getZ();

        final Set<Block> top = new HashSet<Block>();
        final Set<Block> bottom = new HashSet<Block>();
        final Set<Block> north = new HashSet<Block>();
        final Set<Block> south = new HashSet<Block>();
        final Set<Block> east = new HashSet<Block>();
        final Set<Block> west = new HashSet<Block>();

        // TOP, BOTTOM
        for (int x = minX; x < maxX; ++x)
            for (int z = minZ; z < maxZ; ++z) {
                top.add(Locations.blockFromCoordinates(this.world, x, maxY, z));
                bottom.add(Locations.blockFromCoordinates(this.world, x, minY, z));
            }
        // NORTH, SOUTH
        for (int x = minX; x < maxX; ++x)
            for (int y = minY; y < maxY; ++y) {
                north.add(Locations.blockFromCoordinates(this.world, x, y, maxZ));
                south.add(Locations.blockFromCoordinates(this.world, x, y, minZ));
            }
        // EAST, WEST
        for (int y = minY; y < maxY; ++y)
            for (int z = minZ; z < maxZ; ++z) {
                east.add(Locations.blockFromCoordinates(this.world, maxX, y, z));
                west.add(Locations.blockFromCoordinates(this.world, minX, y, z));
            }

        this._faces = Map.of(
                Directions.TOP, top,
                Directions.BOTTOM, bottom,
                Directions.NORTH, north,
                Directions.SOUTH, south,
                Directions.EAST, east,
                Directions.WEST, west
        );
    }

    // EVENTS
    @EventHandler(priority = EventPriority.LOW)
    public final void move (final @NotNull EntityMoveEvent event) {
        final Entity entity = event.getEntity();
        final Location from = event.getFrom();
        final Location to   = event.getTo();

        if (!super.isIn(from) && !super.isIn(to)) return;

        if (!super.isIn(from) && super.isIn(to)) super.onEnter(entity);
        else if (super.isIn(from) && !super.isIn(to)) super.onLeave(entity);
        else super.onEntityMove(entity, from, to);
    }
    @EventHandler(priority = EventPriority.LOW)
    public final void blockBreak (final @NotNull BlockBreakEvent event) {
        final Block block   = event.getBlock();
        final Player player = event.getPlayer();

        if (!super.isIn(block.getLocation())) return;
        super.onBlockBreak(player, block);
    }
    @EventHandler(priority = EventPriority.LOW)
    public final void blockPlace (final @NotNull BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block   = event.getBlockPlaced();

        if (!super.isIn(block.getLocation())) return;
        super.onBlockPlace(player, block);
    }
    @EventHandler(priority = EventPriority.LOW)
    public final void entityDamage (final @NotNull EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        final double damage = event.getDamage();
        final EntityDamageEvent.DamageCause cause = event.getCause();

        if (!super.isIn(entity.getLocation())) return;
        super.onEntityDamage(entity, cause, damage);
    }
    @EventHandler(priority = EventPriority.LOW)
    public final void entityDamageByEntity (final @NotNull EntityDamageByEntityEvent event) {
        final Entity victim = event.getEntity();
        final double damage = event.getDamage();
        final Entity damager = event.getDamager();

        if (!super.isIn(victim.getLocation())) return;
        super.onEntityDamageByEntity(victim, damager, damage);
    }
}
