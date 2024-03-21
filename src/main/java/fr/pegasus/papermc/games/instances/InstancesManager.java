package fr.pegasus.papermc.games.instances;

import fr.pegasus.papermc.PegasusPlugin;
import fr.pegasus.papermc.games.enums.InstanceManagerStates;
import fr.pegasus.papermc.games.events.InstanceManagerStateChangedEvent;
import fr.pegasus.papermc.games.events.InstanceStateChangedEvent;
import fr.pegasus.papermc.games.instances.enums.InstanceStates;
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
    private InstanceManagerStates state;

    /**
     * Create a new InstancesManager
     * @param plugin The plugin instance
     * @param optionsBuilder The options builder
     * @param scoreManager The score manager
     */
    public InstancesManager(
            final @NotNull JavaPlugin plugin,
            final @NotNull OptionsBuilder optionsBuilder,
            final @NotNull ScoreManager scoreManager
    ){
        this.plugin = plugin;
        this.optionsBuilder = optionsBuilder;
        this.scoreManager = scoreManager;
        this.instances = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        // Pre allocate instances
        this.allocateInstances(this.optionsBuilder.getGameOptions().getPreAllocatedInstances());
    }

    /**
     * Allocate instances if needed
     * @param count The number of instances to allocate
     */
    public void allocateInstances(int count){
        if(count == 0){
            PegasusPlugin.logger.info("No instances to allocate");
            return;
        }
        // If the count is less than the current instances count, unallocate the instances
        if(count < this.instances.size()){
            PegasusPlugin.logger.info("Too many instances allocated, unallocating %s instances".formatted(this.instances.size() - count));
            this.instances.subList(count, this.instances.size()).clear();
            PegasusPlugin.logger.info("Unallocated %s instances".formatted(this.instances.size() - count));
            return;
        }
        // If the count is greater than the current instances count, allocate the missing instances
        int toAllocate = count - this.instances.size();
        for(int i = this.instances.size(); i < count; i++){
            PegasusPlugin.logger.info("Allocating instance %d of %d".formatted(i + 1, count));
            Instance instance = createInstance();
            this.instances.add(instance);
        }
        PegasusPlugin.logger.info("Allocated %s instances".formatted(toAllocate));
    }

    /**
     * Create a new instance and add it to the instances list
     * @return The created {@link Instance}
     */
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

    /**
     * Dispatch teams to instances
     * @param teams The teams to dispatch (created in the GameManager)
     */
    public void dispatchTeams(List<List<Team>> teams){
        // Allocate instances if needed
        this.allocateInstances(teams.size());
        for(int i = 0; i < teams.size(); i++){
            this.instances.get(i).affect(teams.get(i));
        }
    }

    /**
     * Start all instances
     */
    public void startInstances(){
        this.instances.forEach(Instance::start);
    }

    /**
     * Stop all instances
     */
    public void stopInstances(){
        this.instances.forEach(instance -> instance.stop(true));
    }

    /**
     * Get the list of created instances
     * @return The list of created {@link Instance}
     */
    public List<Instance> getInstances() {
        return this.instances;
    }

    /**
     * Check if all instances have the target state
     * @param targetState The target {@link InstanceStates}
     * @return True if all instances have the target state, false otherwise
     */
    private boolean isInstancesHasState(InstanceStates targetState){
        for(Instance instance : this.instances){
            if(instance.getState() != targetState){
                System.out.println("Instance " + instance.getId() + " has state " + instance.getState());
                return false;
            }
        }
        return true;
    }

    /**
     * Update the state of the instance manager
     * @param newState The new {@link InstanceManagerStates}
     */
    private void updateState(InstanceManagerStates newState){
        InstanceManagerStates oldState = this.state;
        this.state = newState;
        new InstanceManagerStateChangedEvent(this, oldState, this.state).callEvent();
    }

    /**
     * Handle instance state changed event
     * @param e The {@link InstanceStateChangedEvent}
     */
    @EventHandler
    public void onInstanceStateChanged(InstanceStateChangedEvent e){
        PegasusPlugin.logger.info("Instance %d changed state from %s to %s".formatted(e.getInstance().getId(), e.getOldState(), e.getNewState()));
        switch (e.getNewState()){
            case READY -> {
                if(this.isInstancesHasState(InstanceStates.READY))
                    this.updateState(InstanceManagerStates.READY);
            }
            case STARTED -> {
                if(this.isInstancesHasState(InstanceStates.STARTED))
                    this.updateState(InstanceManagerStates.STARTED);
            }
            case CLOSED -> {
                if(this.isInstancesHasState(InstanceStates.CLOSED)){
                    this.instances.clear();
                    this.updateState(InstanceManagerStates.ENDED);
                }
            }
        }
    }
}
