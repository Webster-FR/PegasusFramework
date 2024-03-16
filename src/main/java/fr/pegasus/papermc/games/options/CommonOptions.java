package fr.pegasus.papermc.games.options;

import fr.pegasus.papermc.games.instances.GameType;
import fr.pegasus.papermc.worlds.PegasusWorld;

public class CommonOptions {

    private GameType gameType;
    private PegasusWorld world;

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public PegasusWorld getWorld() {
        return world;
    }

    public void setWorld(PegasusWorld world) {
        this.world = world;
    }
}
