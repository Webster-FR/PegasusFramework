package fr.pegasus.papermc.games.options;

import fr.pegasus.papermc.games.instances.GameType;
import fr.pegasus.papermc.games.instances.Instance;
import fr.pegasus.papermc.worlds.PegasusWorld;
import fr.pegasus.papermc.worlds.locations.RelativeLocation;
import fr.pegasus.papermc.worlds.schematics.Schematic;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OptionsBuilder {

    private final GameOptions gameOptions;
    private final InstanceOptions instanceOptions;
    private final CommonOptions commonOptions;

    public OptionsBuilder() {
        this.gameOptions = new GameOptions();
        this.instanceOptions = new InstanceOptions();
        this.commonOptions = new CommonOptions();
    }

    public OptionsBuilder setInstanceClass(Class<? extends Instance> instanceClass){
        gameOptions.setInstanceClass(instanceClass);
        return this;
    }
    public OptionsBuilder setPreAllocatedInstances(int preAllocatedInstances){
        gameOptions.setPreAllocatedInstances(preAllocatedInstances);
        return this;
    }
    public OptionsBuilder setRoundDurations(List<Integer> roundDurations){
        instanceOptions.setRoundDurations(roundDurations);
        return this;
    }
    public OptionsBuilder setSpawnPoints(List<RelativeLocation> spawnPoints){
        instanceOptions.setSpawnPoints(spawnPoints);
        return this;
    }
    public OptionsBuilder setSchematic(Schematic schematic){
        instanceOptions.setSchematic(schematic);
        return this;
    }
    public OptionsBuilder setCustomOptions(Map<String, ?> customOptions){
        instanceOptions.setCustomOptions(customOptions);
        return this;
    }
    public OptionsBuilder setGameType(GameType gameType){
        commonOptions.setGameType(gameType);
        return this;
    }
    public OptionsBuilder setWorld(PegasusWorld world){
        commonOptions.setWorld(world);
        return this;
    }

    public GameOptions getGameOptions(){
        if(
                Objects.nonNull(gameOptions.getInstanceClass())
        )
            return gameOptions;
        throw new IllegalStateException("GameOptions are not fully initialized");
    }
    public InstanceOptions getInstanceOptions(){
        if(
                Objects.nonNull(instanceOptions.getRoundDurations()) && !instanceOptions.getRoundDurations().isEmpty() &&
                Objects.nonNull(instanceOptions.getSpawnPoints()) && !instanceOptions.getSpawnPoints().isEmpty() &&
                Objects.nonNull(instanceOptions.getSchematic())
        )
            return instanceOptions;
        throw new IllegalStateException("InstanceOptions are not fully initialized");
    }
    public CommonOptions getCommonOptions(){
        if(
                Objects.nonNull(commonOptions.getGameType()) &&
                Objects.nonNull(commonOptions.getWorld())
        )
            return commonOptions;
        throw new IllegalStateException("CommonOptions are not fully initialized");
    }
}
