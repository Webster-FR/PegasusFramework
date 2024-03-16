package fr.pegasus.papermc.games;

import fr.pegasus.papermc.games.instances.GameType;
import fr.pegasus.papermc.games.instances.InstancesManager;
import fr.pegasus.papermc.games.options.CommonOptions;
import fr.pegasus.papermc.games.options.GameOptions;
import fr.pegasus.papermc.games.options.InstanceOptions;
import fr.pegasus.papermc.games.options.OptionsBuilder;
import fr.pegasus.papermc.scores.ScoreManager;
import fr.pegasus.papermc.teams.Team;
import fr.pegasus.papermc.teams.TeamManager;
import fr.pegasus.papermc.teams.loaders.DataManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GameManager {

    private final DataManager dataManager;
    private final CommonOptions commonOptions;
    private final TeamManager teamManager;
    private final InstancesManager instancesManager;

    public GameManager(final @NotNull JavaPlugin plugin, final @NotNull DataManager dataManager, final @NotNull OptionsBuilder optionsBuilder, final @NotNull ScoreManager scoreManager) {
        this.dataManager = dataManager;
        this.commonOptions = optionsBuilder.getCommonOptions();
        this.teamManager = new TeamManager(dataManager);
        this.instancesManager = new InstancesManager(plugin, optionsBuilder, scoreManager);
    }

    public boolean start(final boolean ignoreBalancedPlayers){
        Set<Team> teams = this.teamManager.getTeams();
        if(!checkMinimumPlayers(teams, commonOptions.getGameType()))
            return false;
        if(!ignoreBalancedPlayers && !checkBalancedPlayers(teams, commonOptions.getGameType()))
            return false;
        // TODO: Start the game
        return true;
    }

    private boolean checkMinimumPlayers(final @NotNull Set<Team> teams, final @NotNull GameType gameType){
        // TODO: Check if there are enough players to start the game
        return false;
    }

    private boolean checkBalancedPlayers(final @NotNull Set<Team> teams, final @NotNull GameType gameType){
        // TODO: Check if the player count is balanced
        return false;
    }
}
