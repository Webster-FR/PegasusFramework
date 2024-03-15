package fr.pegasus.papermc.worlds;

import com.sk89q.worldedit.WorldEditException;
import fr.pegasus.papermc.worlds.generators.VoidGenerator;
import fr.pegasus.papermc.worlds.locations.RelativeLocation;
import fr.pegasus.papermc.worlds.schematics.Schematic;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unused")
public class PegasusWorld implements Listener {

    private final JavaPlugin plugin;
    private final String worldName;
    private final Schematic defaultSchematic;
    private final GameMode gameMode;
    private final RelativeLocation spawnLocation;
    private final Set<WorldPreventions> preventions;

    private World world;

    /**
     * Create a new PegasusWorld
     * @param plugin The plugin instance
     * @param worldName The name of the world
     * @param defaultSchematic The default schematic to paste when the world is generated if any
     * @param difficulty The difficulty of the world
     * @param gameMode The game mode of the world
     * @param spawnLocation The spawn location of the world
     * @param preventions The preventions of the world
     * @param gameRules The game rules of the world
     */
    public PegasusWorld(
            final @NotNull JavaPlugin plugin,
            final @NotNull String worldName,
            final @Nullable Schematic defaultSchematic,
            final @NotNull Difficulty difficulty,
            final @NotNull GameMode gameMode,
            final @NotNull RelativeLocation spawnLocation,
            final @NotNull Set<WorldPreventions> preventions,
            final @NotNull Map<GameRule<?>, Object> gameRules
    ) {
        this.plugin = plugin;
        this.worldName = worldName;
        this.defaultSchematic = defaultSchematic;
        this.gameMode = gameMode;
        this.spawnLocation = spawnLocation;
        this.preventions = preventions;
        this.world = this.getWorld();
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.applyGameRules(gameRules);
        this.world.setDifficulty(difficulty);
    }

    /**
     * Apply the game rules to the world
     * @param gameRules The game rules to apply
     * @param <T> The type of the game rule
     */
    @SuppressWarnings("unchecked")
    private <T> void applyGameRules(final @NotNull Map<GameRule<?>, Object> gameRules){
        for(Map.Entry<GameRule<?>, Object> gameRule : gameRules.entrySet()){
            this.world.setGameRule((GameRule<T>) gameRule.getKey(), (T) gameRule.getValue());
        }
    }

    /**
     * Generate the world
     * @return The generated world
     */
    private World generateWorld(){
        VoidGenerator worldCreator = new VoidGenerator();
        this.world = worldCreator.generate(this.worldName);
        if(Objects.nonNull(this.defaultSchematic)){
            try {
                this.defaultSchematic.paste(this.getSpawnPoint());
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        }
        return this.world;
    }

    /**
     * Get the world or generate it if it doesn't exist
     * @return The world
     */
    public World getWorld(){
        if(Objects.nonNull(this.world)) return this.world;
        this.plugin.getLogger().info(String.format("Loading world %s...", this.worldName));
        this.world = Bukkit.getWorld(this.worldName);
        if(this.world == null){
            this.plugin.getLogger().info(String.format("Generating world %s...", this.worldName));
            this.world = this.generateWorld();
            this.plugin.getLogger().info(String.format("World %s generated!", this.worldName));
        }else{
            this.plugin.getLogger().info(String.format("World %s loaded!", this.worldName));
        }
        return this.world;
    }

    /**
     * Check if the world has a specific prevention
     * @param prevention The prevention to check
     * @return True if the world has the prevention, false otherwise
     */
    private boolean checkPrevention(final @NotNull WorldPreventions prevention){
        return this.preventions.contains(prevention) ^ this.preventions.contains(WorldPreventions.ALL);
    }

    /**
     * Get the spawn point of the world
     * @return The spawn point
     */
    public Location getSpawnPoint(){
        return this.spawnLocation.toAbsolute(new Location(this.world, 0, 0, 0));
    }

    /**
     * Get the name of the world
     * @return The name of the world
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * Prevent entity damages if the world has the prevention
     * @param e The {@link EntityDamageEvent}
     */
    @EventHandler
    public void onEntityDamaged(EntityDamageEvent e){
        if(e.getEntity().getWorld().equals(this.world)){
            if(this.checkPrevention(WorldPreventions.PREVENT_DAMAGES)){
                e.setCancelled(true);
            }
        }
    }

    /**
     * Prevent PvP and PvE if the world has the prevention
     * @param e The {@link EntityDamageByEntityEvent}
     */
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e){
        if(e.getEntity().getWorld().equals(this.world)){
            if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
                if(this.checkPrevention(WorldPreventions.PREVENT_PVP)){
                    e.setCancelled(true);
                }
            }
        }
    }

    /**
     * Prevent block placement if the world has the prevention
     * @param e The {@link BlockPlaceEvent}
     */
    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e){
        if(e.getPlayer().getWorld().equals(this.world)){
            if(this.checkPrevention(WorldPreventions.PREVENT_BUILD)){
                e.setCancelled(true);
            }
        }
    }

    /**
     * Prevent block breaking if the world has the prevention
     * @param e The {@link BlockBreakEvent}
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(e.getPlayer().getWorld().equals(this.world)){
            if(this.checkPrevention(WorldPreventions.PREVENT_BUILD)){
                e.setCancelled(true);
            }
        }
    }

    /**
     * Prevent food level change if the world has the prevention
     * @param e The {@link FoodLevelChangeEvent}
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e){
        if(e.getEntity() instanceof Player player){
            if(player.getWorld().equals(this.world)){
                if(this.checkPrevention(WorldPreventions.PREVENT_FOOD_LOSS)){
                    e.setCancelled(true);
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                }
            }
        }
    }

    /**
     * Prevent portal use if the world has the prevention
     * @param e The {@link PlayerPortalEvent}
     */
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e){
        Player player = e.getPlayer();
        if (player.getWorld().equals(this.world)) {
            if(this.checkPrevention(WorldPreventions.PREVENT_PORTAL_USE)){
                e.setCancelled(true);
            }
        }
    }

    /**
     * Set the game mode of the player when he joins the world
     * @param e The {@link PlayerJoinEvent}
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if (player.getWorld().equals(this.world)) {
            player.setGameMode(this.gameMode);
        }
    }

    /**
     * Set the game mode of the player when he teleports to the world
     * @param e The {@link PlayerTeleportEvent}
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e){
        Player player = e.getPlayer();
        if(e.getCause() != PlayerTeleportEvent.TeleportCause.SPECTATE){
            if(!e.getFrom().getWorld().equals(this.world)){
                if(e.getTo().getWorld().equals(this.world)){
                    player.setGameMode(this.gameMode);
                }
            }
        }
    }
}
