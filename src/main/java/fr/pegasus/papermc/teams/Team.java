package fr.pegasus.papermc.teams;

import fr.pegasus.papermc.utils.PegasusPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record Team(@NotNull String teamTag, @NotNull String teamName, @NotNull Set<PegasusPlayer> players) {

}
