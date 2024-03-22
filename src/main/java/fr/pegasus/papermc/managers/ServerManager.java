package fr.pegasus.papermc.managers;

import fr.pegasus.papermc.PegasusPlugin;
import fr.pegasus.papermc.games.GameManager;
import fr.pegasus.papermc.games.options.OptionsBuilder;
import fr.pegasus.papermc.scores.ScoreManager;
import fr.pegasus.papermc.teams.loaders.DataManager;
import fr.pegasus.papermc.utils.PegasusPlayer;
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
    private final List<GameManager> gameManagers;

    /**
     * Create a new ServerManager
     * @param plugin The plugin instance
     */
    public ServerManager(final @NotNull JavaPlugin plugin){
        this.plugin = plugin;
        this.gameWorlds = new ArrayList<>();
        this.gameManagers = new ArrayList<>();
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
        PegasusPlugin.logger.info("Initializing lobby world");
        // Create lobby world
        this.lobbyWorld = new WorldBuilder("pegasus_lobby")
                .setGameMode(GameMode.SURVIVAL)
                .setDifficulty(Difficulty.PEACEFUL)
                .addPrevention(WorldPreventions.ALL)
                .addGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
                .addGameRule(GameRule.DO_WEATHER_CYCLE, false)
                .setDefaultSchematic(schematic)
                .setWorldTime(6000)
                .make(this.plugin);
        // Unload default world
        PegasusPlugin.logger.info("Unloading default world");
        String defaultWorldName = this.plugin.getServer().getWorlds().getFirst().getName();
        this.plugin.getServer().unloadWorld(defaultWorldName, false);
        String defaultNetherWorldName = defaultWorldName + "_nether";
        this.plugin.getServer().unloadWorld(defaultNetherWorldName, false);
        String defaultEndWorldName = defaultWorldName + "_the_end";
        this.plugin.getServer().unloadWorld(defaultEndWorldName, false);
        PegasusPlugin.logger.info("Default world unloaded");
    }

    /**
     * Add a game world to the server
     * @param worldBuilder The world builder to use to create the game world
     */
    public PegasusWorld addGameWorld(final @NotNull WorldBuilder worldBuilder){
        PegasusPlugin.logger.info("Adding game world %s".formatted(worldBuilder.getWorldName()));
        PegasusWorld world = worldBuilder.make(this.plugin);
        this.gameWorlds.add(world);
        PegasusPlugin.logger.info("Game world %s added".formatted(worldBuilder.getWorldName()));
        return world;
    }

    /**
     * Create a new GameManager
     * @param dataManager The data manager to use
     * @param optionsBuilder The game options to use
     * @param scoreManager The score manager to use
     * @return The new GameManager
     */
    public GameManager createGameManager(
            final @NotNull DataManager dataManager,
            final @NotNull OptionsBuilder optionsBuilder,
            final @NotNull ScoreManager scoreManager
    ){
        PegasusPlugin.logger.info("Creating game manager");
        GameManager gameManager = new GameManager(this.plugin, this.lobbyWorld, dataManager, optionsBuilder, scoreManager);
        this.gameManagers.add(gameManager);
        PegasusPlugin.logger.info("Game manager created");
        return gameManager;
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
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(@NotNull final PlayerJoinEvent e){
        PegasusPlayer pPlayer = new PegasusPlayer(e.getPlayer().getName());
        for(GameManager gameManager : this.gameManagers)
            if(gameManager.isPlayerInGame(pPlayer))
                return;
        e.getPlayer().teleport(this.getLobbyWorld().getSpawnPoint());
        e.getPlayer().getInventory().clear();
        // Setup spectating hot bar
    }
}
