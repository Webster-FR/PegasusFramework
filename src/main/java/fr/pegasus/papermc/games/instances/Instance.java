package fr.pegasus.papermc.games.instances;

import com.sk89q.worldedit.WorldEditException;
import fr.pegasus.papermc.PegasusPlugin;
import fr.pegasus.papermc.games.events.InstanceStateChangedEvent;
import fr.pegasus.papermc.games.instances.enums.InstanceStates;
import fr.pegasus.papermc.games.options.CommonOptions;
import fr.pegasus.papermc.games.options.InstanceOptions;
import fr.pegasus.papermc.scores.ScoreManager;
import fr.pegasus.papermc.teams.PlayerManager;
import fr.pegasus.papermc.teams.Team;
import fr.pegasus.papermc.utils.Countdown;
import fr.pegasus.papermc.utils.PegasusPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public abstract class Instance implements Listener {

    private final int id;
    private final JavaPlugin plugin;
    private final CommonOptions commonOptions;
    private final InstanceOptions instanceOptions;
    private final ScoreManager scoreManager;

    private PlayerManager playerManager;
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
    public final void affect(List<Team> teams){
        this.playerManager = new PlayerManager(this.plugin, teams);
        this.playerManager.setFrozenAll(true);
        // Affect spawns and teleport players to it
        this.playerManager.affectSpawns(this.instanceOptions.getSpawnPoints(), this.commonOptions.getGameType());
        for(PegasusPlayer pPlayer : this.playerManager.getPlayerSpawns().keySet()){
            if(!pPlayer.isOnline())
                this.playerManager.playerDisconnect(pPlayer, this.state);
            Player player = pPlayer.getPlayer();
            player.setRespawnLocation(this.playerManager.getPlayerSpawns().get(pPlayer).toAbsolute(this.instanceLocation));
            player.teleport(this.playerManager.getPlayerSpawns().get(pPlayer).toAbsolute(this.instanceLocation));
        }
        this.updateState(InstanceStates.READY);
        this.onReady();
    }

    public final void start(){
        if(this.state != InstanceStates.READY)
            throw new IllegalStateException("Instance %d is not ready to start".formatted(this.id));
        this.updateState(InstanceStates.PRE_STARTED);
        // Pre-start code
        this.playerManager.setGameModeAll(GameMode.SPECTATOR);
        this.playerManager.setFrozenAll(false);
        this.onPreStart();
        new Countdown(10, i -> this.announceChat("Starting in %d seconds".formatted(i)), () -> {
            this.updateState(InstanceStates.STARTED);
            // Start code
            this.onStart();
            this.startRound();
        }).start(this.plugin);
    }

    public final void startRound(){
        if(state != InstanceStates.STARTED && state != InstanceStates.ROUND_ENDED)
            throw new IllegalStateException("Instance %d is not ready to start a round".formatted(this.id));
        this.updateState(InstanceStates.ROUND_PRE_STARTED);
        // Round pre-start code
        this.playerManager.teleportAllToSpawns(this.instanceLocation);
        this.playerManager.setGameModeAll(GameMode.ADVENTURE);
        this.playerManager.setFrozenAll(true);
        this.onRoundPreStart();
        new Countdown(5, i -> this.announceChat("Round starting in %d seconds".formatted(i)), () -> {
            this.updateState(InstanceStates.ROUND_STARTED);
            // Round start code
            this.playerManager.setFrozenAll(false);
            this.playerManager.setGameModeAll(GameMode.SURVIVAL);
            this.onRoundStart();
            new Countdown(this.instanceOptions.getRoundDurations().get(this.currentRound - 1), i -> {}, this::endRound).start(this.plugin);
        }).start(this.plugin);
    }

    public final void endRound(){
        if(state != InstanceStates.ROUND_STARTED)
            throw new IllegalStateException("Instance %d is not ready to end a round".formatted(this.id));
        this.updateState(InstanceStates.ROUND_ENDED);
        // End code
        this.playerManager.setGameModeAll(GameMode.SPECTATOR);
        //
        for(Map.Entry<PegasusPlayer, InstanceStates> keySet : this.playerManager.getDisconnectedPlayers().entrySet()){
            if(keySet.getValue() == InstanceStates.ROUND_STARTED)
                this.playerManager.getDisconnectedPlayers().put(keySet.getKey(), InstanceStates.ROUND_PRE_STARTED);
        }
        this.onRoundEnd();
        this.currentRound++;
        if(this.isGameEnd())
            new Countdown(3, i -> this.announceChat("Game end in %d seconds".formatted(i)), () -> this.stop(false)).start(this.plugin);
        else
            new Countdown(3, i -> this.announceChat("Next round starting in %d seconds".formatted(i)), this::startRound).start(this.plugin);
    }

    public final void stop(boolean force){
        if(state != InstanceStates.ROUND_ENDED)
            throw new IllegalStateException("Instance %d is not ready to end".formatted(this.id));
        this.updateState(InstanceStates.ENDED);
        this.onEnd();
        this.unregisterEvents();
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
    public abstract void onPlayerReconnect(Player player, InstanceStates disconnectState, InstanceStates reconnectState);

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

    private boolean isGameEnd(){
        return this.instanceOptions.getRoundDurations().size() < this.currentRound;
    }

    /**
     * Check if the player is in this instance
     * @param pPlayer The player to check
     * @return true if the player is in this instance
     */
    private boolean isPlayerInInstance(PegasusPlayer pPlayer){
        for(PegasusPlayer pPlayerInInstance : this.playerManager.getPlayers())
            if(pPlayerInInstance.equals(pPlayer))
                return true;
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isPlayerInInstance(String playerName){
        return this.isPlayerInInstance(new PegasusPlayer(playerName));
    }

    private void unregisterEvents(){
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

    // PUBLIC UTILS

    public void announceChat(String message){
        for(PegasusPlayer pPlayer : this.playerManager.getPlayers())
            if(pPlayer.isOnline())
                pPlayer.getPlayer().sendMessage(message);
    }

    // GETTERS

    public final int getId() {
        return id;
    }
    public final InstanceStates getState() {
        return state;
    }
    public final ScoreManager getScoreManager() {
        return scoreManager;
    }
    public final PlayerManager getPlayerManager() {
        return playerManager;
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
        if(!isPlayerInInstance(e.getPlayer().getName()))
            return;
        PegasusPlayer pPlayer = new PegasusPlayer(e.getPlayer().getName());
        Location teleportLocation = this.playerManager.getPlayerSpawns().get(pPlayer).toAbsolute(this.instanceLocation);
        e.getPlayer().teleport(teleportLocation);
        switch (this.state){
            case ROUND_PRE_STARTED -> e.getPlayer().setGameMode(GameMode.ADVENTURE);
            case ROUND_STARTED -> e.getPlayer().setGameMode(GameMode.SURVIVAL);
            default -> e.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
        this.onPlayerReconnect(e.getPlayer(), this.playerManager.playerReconnect(pPlayer), this.state);
        PegasusPlugin.logger.info("Player %s reconnected to instance %d".formatted(e.getPlayer().getName(), this.id));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if(this.state == InstanceStates.CREATED)
            return;
        if(!this.isPlayerInInstance(e.getPlayer().getName()))
            return;
        this.playerManager.playerDisconnect(new PegasusPlayer(e.getPlayer().getName()), this.state);
    }
}
