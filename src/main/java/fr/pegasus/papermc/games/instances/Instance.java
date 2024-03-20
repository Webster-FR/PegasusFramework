package fr.pegasus.papermc.games.instances;

import com.sk89q.worldedit.WorldEditException;
import fr.pegasus.papermc.PegasusPlugin;
import fr.pegasus.papermc.games.events.InstanceStateChangedEvent;
import fr.pegasus.papermc.games.instances.enums.InstanceStates;
import fr.pegasus.papermc.games.options.CommonOptions;
import fr.pegasus.papermc.games.options.InstanceOptions;
import fr.pegasus.papermc.scores.ScoreManager;
import fr.pegasus.papermc.teams.Team;
import fr.pegasus.papermc.utils.Countdown;
import fr.pegasus.papermc.utils.PegasusPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public abstract class Instance implements Listener {

    private final int id;
    private final JavaPlugin plugin;
    private final CommonOptions commonOptions;
    private final InstanceOptions instanceOptions;
    private final ScoreManager scoreManager;

    private List<Team> teams = null;
    private InstanceStates state;
    private final Location instanceLocation;
    private int currentRound = 1;

    public Instance(int id, JavaPlugin plugin, CommonOptions commonOptions, InstanceOptions instanceOptions, ScoreManager scoreManager) {
        this.id = id;
        this.plugin = plugin;
        this.commonOptions = commonOptions;
        this.instanceOptions = instanceOptions;
        this.scoreManager = scoreManager;
        this.instanceLocation = new Location(commonOptions.getWorld().getWorld(), id * 1000, 100, 0);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        // Paste the schematic
        try {
            this.instanceOptions.getSchematic().paste(this.instanceLocation);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
        this.updateState(InstanceStates.CREATED);
    }

    // LIFECYCLE METHODS

    /**
     * Affect and teleport the teams to this instance
     * @param teams The teams to affect
     */
    public void affect(List<Team> teams){
        this.teams = teams;
        for(Team team : teams){
            for(PegasusPlayer pPlayer : team.players()){
                Player player = pPlayer.getPlayer();
                if(Objects.isNull(player))
                    continue;
                // TODO: Spawn management
                player.teleport(this.instanceOptions.getSpawnPoints().getFirst().toAbsolute(this.instanceLocation));
            }
        }
        this.updateState(InstanceStates.READY);
        this.onReady();
    }

    public void start(){
        if(this.state != InstanceStates.READY)
            throw new IllegalStateException("Instance %d is not ready to start".formatted(this.id));
        this.updateState(InstanceStates.PRE_STARTED);
        this.onPreStart();
        new Countdown(3, i -> this.announceChat("Starting in %d seconds".formatted(i)), () -> {
            this.updateState(InstanceStates.STARTED);
            this.onStart();
            this.startRound();
        }).start(this.plugin);
    }

    public void startRound(){
        if(state != InstanceStates.STARTED && state != InstanceStates.ROUND_ENDED)
            throw new IllegalStateException("Instance %d is not ready to start a round".formatted(this.id));
        this.updateState(InstanceStates.ROUND_PRE_STARTED);
        this.onRoundPreStart();
        new Countdown(3, i -> this.announceChat("Round starting in %d seconds".formatted(i)), () -> {
            this.updateState(InstanceStates.ROUND_STARTED);
            this.onRoundStart();
        }).start(this.plugin);
        new Countdown(this.instanceOptions.getRoundDurations().get(this.currentRound - 1), i -> {}, this::endRound).start(this.plugin);
    }

    public void endRound(){
        if(state != InstanceStates.ROUND_STARTED)
            throw new IllegalStateException("Instance %d is not ready to end a round".formatted(this.id));
        this.updateState(InstanceStates.ROUND_ENDED);
        this.onRoundEnd();
        this.currentRound++;
        if(this.instanceOptions.getRoundDurations().size() < this.currentRound)
            new Countdown(3, i -> this.announceChat("Game end in %d seconds".formatted(i)), () -> this.stop(false)).start(this.plugin);
        else
            new Countdown(3, i -> this.announceChat("Next round starting in %d seconds".formatted(i)), this::startRound).start(this.plugin);
    }

    public void stop(boolean force){
        if(state != InstanceStates.ROUND_ENDED)
            throw new IllegalStateException("Instance %d is not ready to end".formatted(this.id));
        this.updateState(InstanceStates.ENDED);
        this.onEnd();
        if(!force)
            new Countdown(10, i -> this.announceChat("Instance closing in %d seconds".formatted(i)), () -> {
                this.updateState(InstanceStates.CLOSED);
            }).start(this.plugin);
        else
            this.updateState(InstanceStates.CLOSED);
    }

    public abstract void onReady();
    public abstract void onPreStart();
    public abstract void onStart();
    public abstract void onRoundPreStart();
    public abstract void onRoundStart();
    public abstract void onRoundEnd();
    public abstract void onEnd();

    // UTILS

    /**
     * Set the state of the instance
     * @param state The new {@link InstanceStates}
     */
    private void updateState(InstanceStates state){
        InstanceStates oldState = this.state;
        this.state = state;
        new InstanceStateChangedEvent(this, oldState, this.state).callEvent();
    }

    /**
     * Check if the player is in this instance
     * @param pPlayer The player to check
     * @return true if the player is in this instance
     */
    private boolean isPlayerInInstance(PegasusPlayer pPlayer){
        for(Team team : this.teams)
            if(team.players().contains(pPlayer))
                return true;
        return false;
    }

    private boolean isPlayerInInstance(String playerName){
        return this.isPlayerInInstance(new PegasusPlayer(playerName));
    }

    // PUBLIC UTILS

    public void announceChat(String message){
        for(Team team : this.teams)
            for(PegasusPlayer pPlayer : team.players())
                if(Objects.nonNull(pPlayer.getPlayer()))
                    pPlayer.getPlayer().sendMessage(message);
    }

    // GETTERS

    public int getId() {
        return id;
    }
    public InstanceStates getState() {
        return state;
    }
    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    // EVENTS

    /**
     * Handle the player reconnection
     * @param e The {@link PlayerJoinEvent}
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e){
        if(this.state == InstanceStates.CREATED)
            return;
        if(isPlayerInInstance(e.getPlayer().getName())){
            // TODO: Spawn management
            e.getPlayer().teleport(this.instanceOptions.getSpawnPoints().getFirst().toAbsolute(this.instanceLocation));
            // TODO: Need to know if the player has disconnected during this round or not
            PegasusPlugin.logger.info("Player %s reconnected to instance %d".formatted(e.getPlayer().getName(), this.id));
        }
    }
}
