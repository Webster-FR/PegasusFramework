package fr.pegasus.papermc.worlds;

import fr.pegasus.papermc.worlds.locations.RelativeLocation;
import fr.pegasus.papermc.worlds.schematics.Schematic;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldBuilder {

    private final String worldName;
    private Schematic defaultSchematic;
    private Difficulty difficulty;
    private GameMode gameMode;
    private RelativeLocation spawnLocation;
    private final Set<WorldPreventions> preventions;
    private final Map<GameRule<?>, Object> gameRules;

    /**
     * Create a new world builder with default values
     * @param worldName The name of the world
     */
    public WorldBuilder(final @NotNull String worldName) {
        this(worldName, null, Difficulty.NORMAL, GameMode.SURVIVAL, new RelativeLocation(0.5, 100, 0.5));
    }

    /**
     * Create a new world builder with custom values
     * @param worldName The name of the world
     * @param defaultSchematic The default schematic of the world
     * @param difficulty The difficulty of the world
     * @param gameMode The game mode of the world
     * @param spawnLocation The spawn location of the world
     */
    private WorldBuilder(
            final @NotNull String worldName,
            final @Nullable Schematic defaultSchematic,
            final @NotNull Difficulty difficulty,
            final @NotNull GameMode gameMode,
            final @NotNull RelativeLocation spawnLocation
    ) {
        this.worldName = worldName;
        this.defaultSchematic = defaultSchematic;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.spawnLocation = spawnLocation;
        this.preventions = new HashSet<>();
        this.gameRules = new HashMap<>();
    }

    /**
     * Set the default schematic of the world
     * @param difficulty The difficulty of the world
     * @return The current world builder
     */
    public WorldBuilder setDifficulty(final @NotNull Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    /**
     * Set the game mode of the world
     * @param gameMode The game mode of the world
     * @return The current world builder
     */
    public WorldBuilder setGameMode(final @NotNull GameMode gameMode) {
        this.gameMode = gameMode;
        return this;
    }

    /**
     * Add a game rule to the world
     * @param gameRule The game rule to add
     * @param value The value of the game rule
     * @return The current world builder
     */
    public WorldBuilder addGameRule(final @NotNull GameRule<?> gameRule, final @NotNull Object value) {
        this.gameRules.put(gameRule, value);
        return this;
    }

    /**
     * Add a prevention to the world
     * @param prevention The prevention to add
     * @return The current world builder
     */
    public WorldBuilder addPrevention(final @NotNull WorldPreventions prevention) {
        this.preventions.add(prevention);
        return this;
    }

    /**
     * Set the default schematic of the world
     * @param defaultSchematic The default schematic of the world
     * @return The current world builder
     */
    public WorldBuilder setDefaultSchematic(final @NotNull Schematic defaultSchematic) {
        this.defaultSchematic = defaultSchematic;
        return this;
    }

    /**
     * Set the spawn location of the world
     * @param spawnLocation The spawn location of the world
     */
    public void setSpawnLocation(final @NotNull RelativeLocation spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    /**
     * Create a new {@link PegasusWorld} with the current values
     * @param plugin The plugin to create the world with
     * @return The created world as a {@link PegasusWorld}
     */
    public PegasusWorld make(final @NotNull JavaPlugin plugin) {
        return new PegasusWorld(
                plugin,
                this.worldName,
                this.defaultSchematic,
                this.difficulty,
                this.gameMode,
                this.spawnLocation,
                this.preventions,
                this.gameRules
        );
    }
}
