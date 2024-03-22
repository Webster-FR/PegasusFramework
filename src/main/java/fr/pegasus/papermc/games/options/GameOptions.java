package fr.pegasus.papermc.games.options;

import fr.pegasus.papermc.games.instances.Instance;

public class GameOptions {

    private Class<? extends Instance> instanceClass;
    private int preAllocatedInstances = 0;

    public Class<? extends Instance> getInstanceClass() {
        return instanceClass;
    }

    public void setInstanceClass(Class<? extends Instance> instanceClass) {
        this.instanceClass = instanceClass;
    }

    public int getPreAllocatedInstances() {
        return preAllocatedInstances;
    }

    public void setPreAllocatedInstances(int preAllocatedInstances) {
        this.preAllocatedInstances = preAllocatedInstances;
    }
}
