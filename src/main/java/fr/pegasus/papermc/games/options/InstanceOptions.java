package fr.pegasus.papermc.games.options;

import fr.pegasus.papermc.worlds.locations.RelativeLocation;
import fr.pegasus.papermc.worlds.schematics.Schematic;

import java.util.List;
import java.util.Map;

public class InstanceOptions {
    private List<Integer> roundDurations;
    // TODO: Countdown duration
    private List<RelativeLocation> spawnPoints;
    private Schematic schematic;
    private Map<String, ?> customOptions;

    public List<Integer> getRoundDurations() {
        return roundDurations;
    }

    public void setRoundDurations(List<Integer> roundDurations) {
        this.roundDurations = roundDurations;
    }

    public List<RelativeLocation> getSpawnPoints() {
        return spawnPoints;
    }

    public void setSpawnPoints(List<RelativeLocation> spawnPoints) {
        this.spawnPoints = spawnPoints;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public void setSchematic(Schematic schematic) {
        this.schematic = schematic;
    }

    public Map<String, ?> getCustomOptions() {
        return customOptions;
    }

    public void setCustomOptions(Map<String, ?> customOptions) {
        this.customOptions = customOptions;
    }
}
