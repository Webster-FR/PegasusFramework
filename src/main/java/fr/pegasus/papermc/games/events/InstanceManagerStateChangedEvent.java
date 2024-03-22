package fr.pegasus.papermc.games.events;

import fr.pegasus.papermc.games.enums.InstanceManagerStates;
import fr.pegasus.papermc.games.instances.InstancesManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InstanceManagerStateChangedEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final InstancesManager instanceManager;
    private final InstanceManagerStates oldState;
    private final InstanceManagerStates newState;

    public InstanceManagerStateChangedEvent(
            final @NotNull InstancesManager instanceManager,
            final @Nullable InstanceManagerStates oldState,
            final @NotNull InstanceManagerStates newState
    ){
        this.instanceManager = instanceManager;
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

    public InstancesManager getInstanceManager() {
        return instanceManager;
    }
    @Nullable
    public InstanceManagerStates getOldState() {
        return oldState;
    }
    public InstanceManagerStates getNewState() {
        return newState;
    }
}
