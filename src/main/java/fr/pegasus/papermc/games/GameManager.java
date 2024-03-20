package fr.pegasus.papermc.games;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.pegasus.papermc.PegasusPlugin;
import fr.pegasus.papermc.games.enums.GameManagerStates;
import fr.pegasus.papermc.games.enums.InstanceManagerStates;
import fr.pegasus.papermc.games.events.InstanceManagerStateChangedEvent;
import fr.pegasus.papermc.games.instances.GameType;
import fr.pegasus.papermc.games.instances.InstancesManager;
import fr.pegasus.papermc.games.options.CommonOptions;
import fr.pegasus.papermc.games.options.OptionsBuilder;
import fr.pegasus.papermc.scores.ScoreManager;
import fr.pegasus.papermc.teams.Team;
import fr.pegasus.papermc.teams.TeamManager;
import fr.pegasus.papermc.teams.loaders.DataManager;
import fr.pegasus.papermc.utils.PegasusPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameManager implements Listener {

    private final CommonOptions commonOptions;
    private final TeamManager teamManager;
    private final InstancesManager instancesManager;

    private GameManagerStates state = GameManagerStates.CREATED;

    public GameManager(
            final @NotNull JavaPlugin plugin,
            final @NotNull DataManager dataManager,
            final @NotNull OptionsBuilder optionsBuilder,
            final @NotNull ScoreManager scoreManager
    ) {
        this.commonOptions = optionsBuilder.getCommonOptions();
        this.teamManager = new TeamManager(dataManager);
        this.instancesManager = new InstancesManager(plugin, optionsBuilder, scoreManager);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public boolean start(final boolean ignoreBalancedPlayers){
        if(state != GameManagerStates.CREATED)
            return false;
        state = GameManagerStates.STARTED;
        Set<Team> teams = this.teamManager.getTeams();
        if(!checkMinimumTeams(teams, commonOptions.getGameType()))
            return false;
        if(!ignoreBalancedPlayers && !checkBalancedTeams(teams, commonOptions.getGameType()))
            return false;
        if(this.isEmptyTeam(teams))
            return false;
        List<List<Team>> instanceTeams = this.generateTeams(teams);
        this.instancesManager.dispatchTeams(instanceTeams);
        return true;
    }

    public void stop(){
        if(state != GameManagerStates.STARTED)
            return;
        this.instancesManager.stopInstances();
        state = GameManagerStates.ENDED;
    }

    public List<List<Team>> generateTeams(Set<Team> teams){
        List<List<Team>> instanceTeams = new ArrayList<>();
        switch (this.commonOptions.getGameType()){
            case SOLO -> {
                for(Team team : teams)
                    for(PegasusPlayer pPlayer : team.players())
                        instanceTeams.add(Lists.newArrayList(new Team(team.teamTag(), team.teamName(), Sets.newHashSet(pPlayer))));
            }
            case TEAM_ONLY -> throw new UnsupportedOperationException("Team only game type is not supported yet");
            case TEAM_VS_TEAM -> throw new UnsupportedOperationException("Team vs team game type is not supported yet");
            case FFA -> throw new UnsupportedOperationException("FFA game type is not supported yet");
        }
        return instanceTeams;
    }

    private boolean isEmptyTeam(final @NotNull Set<Team> team){
        for(Team t : team)
            if(!t.players().isEmpty())
                return false;
        return true;
    }

    private boolean checkMinimumTeams(final @NotNull Set<Team> teams, final @NotNull GameType gameType){
        return switch (gameType) {
            case SOLO, TEAM_ONLY, FFA -> !teams.isEmpty();
            case TEAM_VS_TEAM -> teams.size() >= 2;
        };
    }

    private boolean checkBalancedTeams(final @NotNull Set<Team> teams, final @NotNull GameType gameType){
        return switch (gameType) {
            case SOLO, TEAM_ONLY, FFA -> !teams.isEmpty();
            case TEAM_VS_TEAM -> teams.size() % 2 == 0;
        };
    }

    @EventHandler
    public void onInstanceManagerStateChanged(InstanceManagerStateChangedEvent e){
        PegasusPlugin.logger.info("Instance manager state changed from %s to %s".formatted(e.getOldState(), e.getNewState()));
        if(e.getNewState() != InstanceManagerStates.READY || this.state != GameManagerStates.STARTED)
            return;
        this.instancesManager.startInstances();
    }
}
