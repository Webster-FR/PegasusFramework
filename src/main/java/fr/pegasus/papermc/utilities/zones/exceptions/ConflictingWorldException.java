package fr.pegasus.papermc.utilities.zones.exceptions;

import org.jetbrains.annotations.NotNull;

public class ConflictingWorldException extends IllegalStateException {
    public ConflictingWorldException () {
        super();
    }
    public ConflictingWorldException (final @NotNull String message) {
        super(message);
    }
}
