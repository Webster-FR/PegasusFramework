package fr.pegasus.papermc.samples;

import fr.pegasus.papermc.samples.events.JoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class PluginStarter extends JavaPlugin {
    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
