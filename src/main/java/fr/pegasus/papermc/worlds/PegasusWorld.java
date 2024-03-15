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

    @SuppressWarnings("unchecked")
    private <T> void applyGameRules(final @NotNull Map<GameRule<?>, Object> gameRules){
        for(Map.Entry<GameRule<?>, Object> gameRule : gameRules.entrySet()){
            this.world.setGameRule((GameRule<T>) gameRule.getKey(), (T) gameRule.getValue());
        }
    }

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

    private boolean checkPrevention(final @NotNull WorldPreventions prevention){
        return this.preventions.contains(prevention) ^ this.preventions.contains(WorldPreventions.ALL);
    }

    public Location getSpawnPoint(){
        return this.spawnLocation.toAbsolute(new Location(this.world, 0, 0, 0));
    }

    public String getWorldName() {
        return worldName;
    }

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent e){
        if(e.getEntity().getWorld().equals(this.world)){
            if(this.checkPrevention(WorldPreventions.PREVENT_DAMAGES)){
                e.setCancelled(true);
            }
        }
    }

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

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e){
        if(e.getPlayer().getWorld().equals(this.world)){
            if(this.checkPrevention(WorldPreventions.PREVENT_BUILD)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(e.getPlayer().getWorld().equals(this.world)){
            if(this.checkPrevention(WorldPreventions.PREVENT_BUILD)){
                e.setCancelled(true);
            }
        }
    }

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
     * @param e The event
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
     * @param e The event
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
