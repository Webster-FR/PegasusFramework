package fr.pegasus.papermc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PegasusPlayer {
    private final String name;

    public PegasusPlayer(final @NotNull String name) {
        this.name = name;
    }

    public boolean isOnline(){
        return Objects.nonNull(Bukkit.getPlayer(this.name));
    }

    public Player getPlayer(){
        Player player = Bukkit.getPlayer(this.name);
        if(Objects.isNull(player))
            throw new RuntimeException("Player %s is not online".formatted(this.name));
        return player;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PegasusPlayer))
            return false;
        return ((PegasusPlayer) obj).name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
