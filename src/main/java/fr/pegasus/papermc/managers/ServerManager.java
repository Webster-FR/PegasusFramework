package fr.pegasus.papermc.managers;

import fr.pegasus.papermc.worlds.PegasusWorld;
import fr.pegasus.papermc.worlds.WorldBuilder;
import fr.pegasus.papermc.worlds.WorldPreventions;
import fr.pegasus.papermc.worlds.schematics.Schematic;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class ServerManager implements Listener {

    private final JavaPlugin plugin;

    private PegasusWorld lobbyWorld;
    private final List<PegasusWorld> gameWorlds;

    /**
     * Create a new ServerManager
     * @param plugin The plugin instance
     */
    public ServerManager(final @NotNull JavaPlugin plugin){
        this.plugin = plugin;
        this.gameWorlds = new ArrayList<>();
        getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Initialize the lobby world with the default schematic named "lobby"
     */
    public void initLobby(){
        this.initLobby(new Schematic(this.plugin, "lobby"));
    }

    /**
     * Initialize the lobby world with the given schematic
     * @param schematic The schematic to use for the lobby world
     */
    public void initLobby(final @NotNull Schematic schematic){
        // Create lobby world
        this.lobbyWorld = new WorldBuilder("pegasus_lobby")
                .setGameMode(GameMode.SURVIVAL)
                .setDifficulty(Difficulty.PEACEFUL)
                .addPrevention(WorldPreventions.ALL)
                .addGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
                .addGameRule(GameRule.DO_WEATHER_CYCLE, false)
                .setDefaultSchematic(schematic)
                .make(this.plugin);
        // Unload default world
        String defaultWorldName = this.plugin.getServer().getWorlds().getFirst().getName();
        this.plugin.getServer().unloadWorld(defaultWorldName, false);
        String defaultNetherWorldName = defaultWorldName + "_nether";
        this.plugin.getServer().unloadWorld(defaultNetherWorldName, false);
        String defaultEndWorldName = defaultWorldName + "_the_end";
        this.plugin.getServer().unloadWorld(defaultEndWorldName, false);
    }

    /**
     * Add a game world to the server
     * @param worldBuilder The world builder to use to create the game world
     */
    public void addGameWorld(final @NotNull WorldBuilder worldBuilder){
        this.gameWorlds.add(worldBuilder.make(this.plugin));
    }

    /**
     * Get the lobby world
     * @return The lobby world
     */
    public PegasusWorld getLobbyWorld() {
        return this.lobbyWorld;
    }

    /**
     * Get the list of game worlds
     * @return The list of game worlds
     */
    public List<PegasusWorld> getGameWorlds() {
        return this.gameWorlds;
    }

    /**
     * Teleport the player to the lobby world when they join the server
     * @param e The PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        e.getPlayer().teleport(this.getLobbyWorld().getSpawnPoint());
    }
}
