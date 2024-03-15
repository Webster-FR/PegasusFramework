package fr.pegasus.papermc;

import fr.pegasus.papermc.managers.ServerManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class PegasusPlugin extends JavaPlugin {

    private ServerManager serverManager;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        serverManager = new ServerManager(this);
        serverManager.initLobby();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public ServerManager getServerManager() {
        return serverManager;
    }
}
