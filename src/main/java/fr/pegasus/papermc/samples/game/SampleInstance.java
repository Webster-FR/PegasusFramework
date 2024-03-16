package fr.pegasus.papermc.samples.game;

import fr.pegasus.papermc.games.instances.Instance;
import fr.pegasus.papermc.games.options.CommonOptions;
import fr.pegasus.papermc.games.options.InstanceOptions;
import fr.pegasus.papermc.scores.ScoreManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SampleInstance extends Instance {
    public SampleInstance(int id, JavaPlugin plugin, CommonOptions commonOptions, InstanceOptions instanceOptions, ScoreManager scoreManager) {
        super(id, plugin, commonOptions, instanceOptions, scoreManager);
    }
}
