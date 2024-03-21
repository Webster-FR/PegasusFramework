package fr.pegasus.papermc.samples.game;

import fr.pegasus.papermc.PegasusPlugin;
import fr.pegasus.papermc.games.GameManager;
import fr.pegasus.papermc.games.instances.GameType;
import fr.pegasus.papermc.games.options.OptionsBuilder;
import fr.pegasus.papermc.worlds.PegasusWorld;
import fr.pegasus.papermc.worlds.WorldBuilder;
import fr.pegasus.papermc.worlds.WorldPreventions;
import fr.pegasus.papermc.worlds.locations.RelativeLocation;
import fr.pegasus.papermc.worlds.schematics.Schematic;
import fr.pegasus.papermc.worlds.schematics.SchematicFlags;
import io.papermc.paper.event.player.ChatEvent;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class GameSamplePlugin extends PegasusPlugin implements Listener {

    private static GameManager gameManager;

    @Override
    public void onEnable() {
        super.onEnable();

        this.getServer().getPluginManager().registerEvents(this, this);

        WorldBuilder gameWorldBuilder = new WorldBuilder("pegasus_sample");
        gameWorldBuilder.setGameMode(GameMode.CREATIVE)
                .setDifficulty(Difficulty.PEACEFUL)
                .addGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
                .addGameRule(GameRule.DO_WEATHER_CYCLE, false)
                .setWorldTime(6000)
                .addPrevention(WorldPreventions.ALL)
                .addPrevention(WorldPreventions.PREVENT_PVP);
        PegasusWorld gameWorld = this.getServerManager().addGameWorld(gameWorldBuilder);

        OptionsBuilder optionsBuilder = new OptionsBuilder()
                .setGameType(GameType.SOLO)
                .setWorld(gameWorld)
                .setInstanceClass(SampleInstance.class)
                .setRoundDurations(List.of(10))
                .setSpawnPoints(List.of(new RelativeLocation(0.5, 0, 0.5, 90, 0)))
                .setSchematic(new Schematic(this, "instances_test", SchematicFlags.COPY_BIOMES))
                .setPreAllocatedInstances(1);
        gameManager = this.getServerManager().createGameManager(new SampleDataManager(), optionsBuilder, new SampleScoreManager());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChatMessage(ChatEvent e){
        gameManager.start(false);
    }
}
