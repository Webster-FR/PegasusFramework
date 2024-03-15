package fr.pegasus.papermc.builders.items.enums;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public enum PotionTypes {
    DRINK (Material.POTION),
    LINGERING (Material.LINGERING_POTION),
    SPLASH (Material.SPLASH_POTION);

    private final Material material;

    PotionTypes (final @NotNull Material material) {
        this.material = material;
    }

    public @NotNull Material material () {
        return this.material;
    }
}
