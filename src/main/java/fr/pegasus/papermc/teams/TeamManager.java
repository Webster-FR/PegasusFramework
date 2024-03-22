package fr.pegasus.papermc.teams;

import fr.pegasus.papermc.PegasusPlugin;
import fr.pegasus.papermc.teams.loaders.DataManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TeamManager {

    private final DataManager dataManager;
    private final Set<Team> teams;

    public TeamManager(final @NotNull DataManager dataManager) {
        this.dataManager = dataManager;
        this.teams = new HashSet<>();
    }

    private void loadTeams(){
        PegasusPlugin.logger.info("Loading teams from %s data manager".formatted(this.dataManager.getClass().getSimpleName()));
        this.teams.addAll(this.dataManager.loadTeams());
        PegasusPlugin.logger.info("Loaded %d teams".formatted(this.teams.size()));
    }

    public void reloadTeams() {
        this.teams.clear();
        this.loadTeams();
    }

    public Set<Team> getTeams() {
        if(!this.teams.isEmpty())
            return this.teams;
        this.loadTeams();
        return teams;
    }
}
