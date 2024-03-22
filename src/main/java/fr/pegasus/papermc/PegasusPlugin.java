package fr.pegasus.papermc;

import fr.pegasus.papermc.managers.ServerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public class PegasusPlugin extends JavaPlugin {

    public static Logger logger = Logger.getLogger("PegasusFramework");
    private ServerManager serverManager;

    @Override
    public void onLoad() {
        super.onLoad();
        logger.info("Pegasus Framework has been loaded");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        serverManager = new ServerManager(this);
        serverManager.initLobby();
        logger.info("Pegasus Framework has been enabled");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        logger.info("Pegasus Framework has been disabled");
    }

    public ServerManager getServerManager() {
        return serverManager;
    }
}
