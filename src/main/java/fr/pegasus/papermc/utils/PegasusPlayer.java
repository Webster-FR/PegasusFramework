package fr.pegasus.papermc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PegasusPlayer {
    private final String name;

    public PegasusPlayer(final @NotNull String name) {
        this.name = name;
    }

    @Nullable
    public Player getPlayer(){
        return Bukkit.getPlayer(name);
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
