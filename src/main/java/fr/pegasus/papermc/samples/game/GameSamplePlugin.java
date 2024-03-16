package fr.pegasus.papermc.samples.game;

import fr.pegasus.papermc.PegasusPlugin;
import fr.pegasus.papermc.games.GameManager;
import fr.pegasus.papermc.games.instances.GameType;
import fr.pegasus.papermc.games.options.OptionsBuilder;
import fr.pegasus.papermc.worlds.WorldBuilder;
import fr.pegasus.papermc.worlds.locations.RelativeLocation;
import fr.pegasus.papermc.worlds.schematics.Schematic;
import fr.pegasus.papermc.worlds.schematics.SchematicFlags;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import java.util.List;

public class GameSamplePlugin extends PegasusPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        WorldBuilder gameWorldBuilder = new WorldBuilder("pegasus_sample");
        gameWorldBuilder.setGameMode(GameMode.CREATIVE)
                .setDifficulty(Difficulty.PEACEFUL);
        this.getServerManager().addGameWorld(gameWorldBuilder);

        OptionsBuilder optionsBuilder = new OptionsBuilder()
                .setGameType(GameType.SOLO)
                .setWorld(this.getServerManager().getGameWorlds().getFirst())
                .setInstanceClass(SampleInstance.class)
                .setRoundDurations(List.of(300))
                .setSpawnPoints(List.of(new RelativeLocation(0, 100, 0)))
                .setSchematic(new Schematic(this, "lobby", SchematicFlags.IGNORE_AIR, SchematicFlags.COPY_BIOMES))
                .setPreAllocatedInstances(1);
        GameManager gameManager = this.getServerManager().createGameManager(new SampleDataManager(), optionsBuilder, new SampleScoreManager());
    }
}
