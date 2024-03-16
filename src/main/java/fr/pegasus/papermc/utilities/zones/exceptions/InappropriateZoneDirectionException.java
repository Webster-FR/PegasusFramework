package fr.pegasus.papermc.utilities.zones.exceptions;

import org.jetbrains.annotations.NotNull;

public class InappropriateZoneDirectionException extends IllegalStateException {
    public InappropriateZoneDirectionException() {
        super();
    }
    public InappropriateZoneDirectionException(final @NotNull String message) {
        super(message);
    }
}
