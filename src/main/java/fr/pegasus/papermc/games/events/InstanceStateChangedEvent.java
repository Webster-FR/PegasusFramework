package fr.pegasus.papermc.games.events;

import fr.pegasus.papermc.games.instances.Instance;
import fr.pegasus.papermc.games.instances.enums.InstanceStates;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InstanceStateChangedEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Instance instance;
    private final InstanceStates oldState;
    private final InstanceStates newState;

    public InstanceStateChangedEvent(final @NotNull Instance instance, final @Nullable InstanceStates oldState, final @NotNull InstanceStates newState){
        this.instance = instance;
        this.oldState = oldState;
        this.newState = newState;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public Instance getInstance() {
        return instance;
    }
    @Nullable
    public InstanceStates getOldState() {
        return oldState;
    }
    public InstanceStates getNewState() {
        return newState;
    }
}
