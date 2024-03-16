package fr.pegasus.papermc.games.instances;

import fr.pegasus.papermc.games.events.InstanceStateChangedEvent;
import fr.pegasus.papermc.games.instances.enums.InstanceState;
import fr.pegasus.papermc.games.options.CommonOptions;
import fr.pegasus.papermc.games.options.InstanceOptions;
import fr.pegasus.papermc.scores.ScoreManager;
import fr.pegasus.papermc.teams.Team;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class Instance implements Listener {

    private final int id;
    private final CommonOptions commonOptions;
    private final InstanceOptions instanceOptions;
    private final ScoreManager scoreManager;

    private List<Team> teams = null;
    private InstanceState state = null;

    public Instance(int id, JavaPlugin plugin, CommonOptions commonOptions, InstanceOptions instanceOptions, ScoreManager scoreManager) {
        this.id = id;
        this.commonOptions = commonOptions;
        this.instanceOptions = instanceOptions;
        this.scoreManager = scoreManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.setState(InstanceState.CREATED);
    }

    private void setState(InstanceState state){
        new InstanceStateChangedEvent(this, this.state, state).callEvent();
        this.state = state;
    }

    /**
     * Generate the instance by placing the schematic
     */
    public void generate(){
        // TODO: Past schematic
        this.setState(InstanceState.GENERATED);
    }

    public int getId() {
        return id;
    }
}
