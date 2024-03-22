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
import fr.pegasus.papermc.worlds.PegasusWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameManager implements Listener {

    private final PegasusWorld lobbyWorld;
    private final CommonOptions commonOptions;
    private final TeamManager teamManager;
    private final InstancesManager instancesManager;

    private GameManagerStates state = GameManagerStates.CREATED;

    /**
     * Create a new GameManager
     * @param plugin The plugin instance
     * @param lobbyWorld The lobby world
     * @param dataManager The data manager (for the {@link TeamManager})
     * @param optionsBuilder The options builder
     * @param scoreManager The score manager
     */
    public GameManager(
            final @NotNull JavaPlugin plugin,
            final @NotNull PegasusWorld lobbyWorld,
            final @NotNull DataManager dataManager,
            final @NotNull OptionsBuilder optionsBuilder,
            final @NotNull ScoreManager scoreManager
    ) {
        this.lobbyWorld = lobbyWorld;
        this.commonOptions = optionsBuilder.getCommonOptions();
        this.teamManager = new TeamManager(dataManager);
        this.instancesManager = new InstancesManager(plugin, optionsBuilder, scoreManager);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Constructor for test purposes only
     */
    public GameManager(){
        PegasusPlugin.logger.warning("Constructor called for test purposes only");
        this.lobbyWorld = null;
        this.commonOptions = null;
        this.teamManager = null;
        this.instancesManager = null;
    }

    /**
     * Start the game manager
     * @param ignoreBalancedPlayers Ignore the balanced players check
     * @return True if the game manager is started, false otherwise
     */
    public boolean start(final boolean ignoreBalancedPlayers){
        if(state != GameManagerStates.CREATED)
            return false;
        state = GameManagerStates.STARTED;
        Set<Team> teams = this.teamManager.getTeams();
        if(this.isEmptyTeam(teams))
            return false;
        if(!checkMinimumTeams(teams, commonOptions.getGameType()))
            return false;
        if(!ignoreBalancedPlayers && !checkBalancedTeams(teams, commonOptions.getGameType()))
            return false;
        List<List<Team>> instanceTeams = this.generateTeams(teams);
        this.instancesManager.dispatchTeams(instanceTeams);
        return true;
    }

    /**
     * Stop the game manager and all its instances
     */
    public void stop(){
        if(state != GameManagerStates.STARTED)
            return;
        this.instancesManager.stopInstances();
        state = GameManagerStates.ENDED;
    }

    /**
     * Generate the teams for each game type
     * @param teams The teams to generate
     * @return The generated teams
     */
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

    /**
     * Check if there is an empty team
     * @param teams The teams to check
     * @return True if there is an empty team, false otherwise
     */
    private boolean isEmptyTeam(final @NotNull Set<Team> teams){
        for(Team t : teams)
            if(t.players().isEmpty())
                return true;
        return false;
    }

    /**
     * Check if the minimum number of teams is reached for each game type
     * @param teams The teams to check
     * @param gameType The game type
     * @return True if the minimum number of teams is reached, false otherwise
     */
    private boolean checkMinimumTeams(final @NotNull Set<Team> teams, final @NotNull GameType gameType){
        return switch (gameType) {
            case SOLO, TEAM_ONLY, FFA -> !teams.isEmpty();
            case TEAM_VS_TEAM -> teams.size() >= 2;
        };
    }

    /**
     * Check if the teams are balanced for each game type
     * @param teams The teams to check
     * @param gameType The game type
     * @return True if the teams are balanced, false otherwise
     */
    private boolean checkBalancedTeams(final @NotNull Set<Team> teams, final @NotNull GameType gameType){
        return switch (gameType) {
            case SOLO, TEAM_ONLY, FFA -> !teams.isEmpty();
            case TEAM_VS_TEAM -> teams.size() % 2 == 0;
        };
    }

    /**
     * Get the current game manager state
     * @return The {@link GameManagerStates}
     */
    public GameManagerStates getState() {
        return this.state;
    }

    /**
     * Check if the player is in any of the instances
     * @param player The {@link PegasusPlayer} to check
     * @return True if the player is in any of the instances, false otherwise
     */
    public boolean isPlayerInGame(PegasusPlayer player){
        return this.instancesManager.isPlayerInInstances(player);
    }

    /**
     * Handle the instance manager state changed event
     * @param e The {@link InstanceManagerStateChangedEvent}
     */
    @EventHandler
    public void onInstanceManagerStateChanged(InstanceManagerStateChangedEvent e){
        PegasusPlugin.logger.info("Instance manager state changed from %s to %s".formatted(e.getOldState(), e.getNewState()));
        if(e.getNewState() == InstanceManagerStates.ENDED){
            this.commonOptions.getWorld().getWorld().getPlayers().forEach(player -> {
                player.teleport(this.lobbyWorld.getSpawnPoint());
                player.setRespawnLocation(this.lobbyWorld.getSpawnPoint());
            });
            state = GameManagerStates.ENDED;
        }
        if(e.getNewState() != InstanceManagerStates.READY || this.state != GameManagerStates.STARTED)
            return;
        this.instancesManager.startInstances();
    }
}
