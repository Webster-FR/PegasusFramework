package fr.pegasus.papermc.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Announcer {

    private Set<PegasusPlayer> players;

    /**
     * Create a new Announcer
     * @param players The players to announce to
     */
    public Announcer(@NotNull final Set<PegasusPlayer> players) {
        this.players = players;
    }

    public Announcer(){
        this.players = new HashSet<>();
    }

    public void setPlayers(@NotNull final Set<PegasusPlayer> players){
        this.players = players;
    }

    /**
     * Announce a chat message to all players
     * @param component The message to announce
     */
    public void announceChat(@NotNull final TextComponent component){
        for(PegasusPlayer player : this.players){
            if(!player.isOnline())
                continue;
            player.getPlayer().sendMessage(component);
        }
    }

    /**
     * Announce a chat message to all players
     * @param message The message to announce
     */
    public void announceChat(@NotNull final String message){
        this.announceChat(Component.text(message));
    }

    /**
     * Announce a title to all players
     * @param title The title to announce
     */
    public void announceTitle(@NotNull final Title title){
        for(PegasusPlayer player : this.players){
            if(!player.isOnline())
                continue;
            player.getPlayer().showTitle(title);
        }
    }

    /**
     * Announce an action bar to all players
     * @param component The message to display on action bar
     */
    public void announceActionBar(@NotNull final TextComponent component){
        for(PegasusPlayer player : this.players){
            if(!player.isOnline())
                continue;
            player.getPlayer().sendActionBar(component);
        }
    }

    /**
     * Announce an action bar to all players
     * @param message The message to display on action bar
     */
    public void announceActionBar(@NotNull final String message){
        this.announceActionBar(Component.text(message));
    }
}
