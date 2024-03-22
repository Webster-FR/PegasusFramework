package fr.pegasus.papermc.samples.game;

import fr.pegasus.papermc.games.instances.Instance;
import fr.pegasus.papermc.games.instances.enums.InstanceStates;
import fr.pegasus.papermc.games.options.CommonOptions;
import fr.pegasus.papermc.games.options.InstanceOptions;
import fr.pegasus.papermc.scores.ScoreManager;
import fr.pegasus.papermc.utils.PegasusPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SampleInstance extends Instance {
    public SampleInstance(int id, JavaPlugin plugin, CommonOptions commonOptions, InstanceOptions instanceOptions, ScoreManager scoreManager) {
        super(id, plugin, commonOptions, instanceOptions, scoreManager);
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onPreStart() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRoundPreStart() {

    }

    @Override
    public void onRoundStart() {
        for(PegasusPlayer pPlayer : this.getPlayerManager().getPlayers()){
            if(!pPlayer.isOnline())
                continue;
            Player player = pPlayer.getPlayer();
            player.getInventory().addItem(new ItemStack(Material.BEDROCK, 1));
        }
    }

    @Override
    public void onRoundEnd() {

    }

    @Override
    public void onEnd() {

    }

    @Override
    public void onTick(int remainingTime) {

    }

    @Override
    public void onPlayerReconnect(Player player, InstanceStates disconnectState, InstanceStates reconnectState) {
        if(disconnectState != InstanceStates.ROUND_STARTED && reconnectState == InstanceStates.ROUND_STARTED)
            player.getInventory().addItem(new ItemStack(Material.BEDROCK, 1));
    }
}
