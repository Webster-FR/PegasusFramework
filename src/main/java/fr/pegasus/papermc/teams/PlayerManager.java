package fr.pegasus.papermc.teams;

import fr.pegasus.papermc.games.instances.GameType;
import fr.pegasus.papermc.games.instances.enums.InstanceStates;
import fr.pegasus.papermc.tools.dispatcher.Dispatcher;
import fr.pegasus.papermc.tools.dispatcher.DispatcherAlgorithm;
import fr.pegasus.papermc.utils.PegasusPlayer;
import fr.pegasus.papermc.worlds.locations.RelativeLocation;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerManager implements Listener {

    private List<Team> teams;
    private Map<PegasusPlayer, RelativeLocation> playerSpawns;
    private final List<PegasusPlayer> frozenPlayers;
    private final Map<PegasusPlayer, InstanceStates> disconnectedPlayers;

    // TODO: Announcer management

    /**
     * Create a new PlayerManager
     * @param teams The teams to manage
     */
    public PlayerManager(@NotNull final JavaPlugin plugin, @NotNull final List<Team> teams) {
        this.teams = teams;
        this.playerSpawns = new HashMap<>();
        this.frozenPlayers = new ArrayList<>();
        this.disconnectedPlayers = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Create a new PlayerManager
     * @param plugin The plugin instance
     */
    public PlayerManager(@NotNull final JavaPlugin plugin) {
        this.teams = new ArrayList<>();
        this.playerSpawns = new HashMap<>();
        this.frozenPlayers = new ArrayList<>();
        this.disconnectedPlayers = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Set the teams managed by this PlayerManager
     * @param teams The teams to manage
     */
    public void setTeams(@NotNull final List<Team> teams){
        this.teams = teams;
    }

    /**
     * Affect the spawns to the players
     * @param spawns The spawns to affect
     * @param gameType The {@link GameType} of the game
     */
    public void affectSpawns(@NotNull final List<RelativeLocation> spawns, @NotNull final GameType gameType){
        Dispatcher dispatcher = new Dispatcher(DispatcherAlgorithm.ROUND_ROBIN);
        switch (gameType){
            case SOLO -> {
                this.playerSpawns = dispatcher.dispatch(this.getPlayers(), spawns);
            }
            default -> throw new UnsupportedOperationException("Not implemented yet");
        }
    }

    /**
     * Get the players managed by this PlayerManager
     * @return The players
     */
    public List<PegasusPlayer> getPlayers(){
        List<PegasusPlayer> players = new ArrayList<>();
        for(Team team : this.teams)
            players.addAll(team.players());
        return players;
    }

    /**
     * Get the teams managed by this PlayerManager
     * @return The teams
     */
    public List<Team> getTeams() {
        return this.teams;
    }

    public Map<PegasusPlayer, RelativeLocation> getPlayerSpawns() {
        return playerSpawns;
    }

    public boolean isFrozen(@NotNull final PegasusPlayer player) {
        return this.frozenPlayers.contains(player);
    }

    public void setFrozen(@NotNull final PegasusPlayer player, boolean frozen) {
        if(frozen)
            this.frozenPlayers.add(player);
        else
            this.frozenPlayers.remove(player);
    }

    public void setFrozenAll(boolean frozen) {
        for(PegasusPlayer player : this.getPlayers())
            this.setFrozen(player, frozen);
    }

    public void setGameMode(@NotNull final PegasusPlayer player, @NotNull final GameMode gameMode){
        if(!player.isOnline())
            return;
        player.getPlayer().setGameMode(gameMode);
    }

    public void setGameModeAll(@NotNull final GameMode gameMode){
        for(PegasusPlayer player : this.getPlayers())
            this.setGameMode(player, gameMode);
    }

    public void playerDisconnect(@NotNull final PegasusPlayer player, @NotNull final InstanceStates currentState){
        this.disconnectedPlayers.put(player, currentState);
    }

    public InstanceStates playerReconnect(@NotNull final PegasusPlayer player){
        InstanceStates state = this.disconnectedPlayers.get(player);
        this.disconnectedPlayers.remove(player);
        return state;
    }

    public Map<PegasusPlayer, InstanceStates> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }

    @EventHandler
    public void onPlayerMove(@NotNull final PlayerMoveEvent e){
        PegasusPlayer pPlayer = new PegasusPlayer(e.getPlayer());
        if(this.isFrozen(pPlayer) && e.getFrom().distance(e.getTo()) > 0)
            e.setCancelled(true);
    }

    public void teleportAllToSpawns(@NotNull final Location instanceLocation) {
        for(PegasusPlayer pPlayer : this.playerSpawns.keySet()){
            if(!pPlayer.isOnline())
                continue;
            pPlayer.getPlayer().teleport(this.playerSpawns.get(pPlayer).toAbsolute(instanceLocation));
        }
    }
}
