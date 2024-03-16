package fr.pegasus.papermc.games.instances;

import fr.pegasus.papermc.PegasusPlugin;
import fr.pegasus.papermc.games.events.InstanceStateChangedEvent;
import fr.pegasus.papermc.games.options.OptionsBuilder;
import fr.pegasus.papermc.scores.ScoreManager;
import fr.pegasus.papermc.teams.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class InstancesManager implements Listener {

    private final JavaPlugin plugin;
    private final OptionsBuilder optionsBuilder;
    private final ScoreManager scoreManager;
    private final List<Instance> instances;

    public InstancesManager(final @NotNull JavaPlugin plugin, final @NotNull OptionsBuilder optionsBuilder, final @NotNull ScoreManager scoreManager){
        this.plugin = plugin;
        this.optionsBuilder = optionsBuilder;
        this.scoreManager = scoreManager;
        this.instances = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.preAllocateInstances();
    }

    public void preAllocateInstances(){
        int count = this.optionsBuilder.getGameOptions().getPreAllocatedInstances();
        if(count == 0){
            PegasusPlugin.logger.info("No instances to pre-allocate");
            return;
        }
        PegasusPlugin.logger.info("Pre-allocating %s instances".formatted(count));
        for(int i = 0; i < count; i++){
            Instance instance = createInstance();
            instance.generate();
        }
        PegasusPlugin.logger.info("Pre-allocated %s instances".formatted(count));
    }

    private Instance createInstance(){
        try {
            Instance instance = (Instance) this.optionsBuilder.getGameOptions().getInstanceClass().getConstructors()[0].newInstance(
                    this.instances.size(),
                    this.plugin,
                    this.optionsBuilder.getCommonOptions(),
                    this.optionsBuilder.getInstanceOptions(),
                    this.scoreManager);
            this.instances.add(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void dispatchTeams(List<List<Team>> teams){
        // Dispatch given teams into instances
        // The teams are already balanced
        // TODO
    }

    public void startInstances(){
        // TODO
    }

    public void stopInstances(){
        // TODO
    }

    public List<Instance> getInstances() {
        return this.instances;
    }

    @EventHandler
    public void onInstanceStateChanged(InstanceStateChangedEvent event){
        PegasusPlugin.logger.info("Instance %d changed state from %s to %s".formatted(event.getInstance().getId(), event.getOldState(), event.getNewState()));
    }
}
