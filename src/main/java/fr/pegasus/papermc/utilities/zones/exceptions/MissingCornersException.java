package fr.pegasus.papermc.utilities.zones.exceptions;

import org.jetbrains.annotations.NotNull;

public class MissingCornersException extends IllegalStateException {
    public MissingCornersException () {
        super();
    }
    public MissingCornersException (final @NotNull String message) {
        super(message);
    }
}
