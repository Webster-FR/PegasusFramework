package fr.pegasus.papermc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PegasusPlayer {
    private final String name;

    /**
     * Create a new PegasusPlayer
     * @param name The player name
     */
    public PegasusPlayer(final @NotNull String name) {
        this.name = name;
    }

    /**
     * Check if the player is online
     * @return True if the player is online, false otherwise
     */
    public boolean isOnline(){
        return Objects.nonNull(Bukkit.getPlayer(this.name));
    }

    /**
     * Get the player if online, throw a {@link RuntimeException} otherwise
     * @return The player if online
     */
    public Player getPlayer(){
        Player player = Bukkit.getPlayer(this.name);
        if(Objects.isNull(player))
            throw new RuntimeException("Player %s is not online".formatted(this.name));
        return player;
    }

    /**
     * Check if an object is equals to this PegasusPlayer
     * @param obj The object to compare
     * @return True if the object is equals to this PegasusPlayer, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PegasusPlayer))
            return false;
        return ((PegasusPlayer) obj).name.equals(this.name);
    }

    /**
     * Get the hash code of this PegasusPlayer
     * @return The hash code of this PegasusPlayer
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
