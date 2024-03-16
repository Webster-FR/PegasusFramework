package fr.pegasus.papermc.builders.items.enums;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public enum ArmorParts {
    // VALUES
    HEAD (Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET),
    CHEST (Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE),
    LEGS (Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS),
    FOOT (Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS);

    // FIELDS
    private final Material leather, chainmail, iron, gold, diamond, netherite;

    // CONSTRUCTOR
    ArmorParts(
            final @NotNull Material leather,
            final @NotNull Material chainmail,
            final @NotNull Material iron,
            final @NotNull Material gold,
            final @NotNull Material diamond,
            final @NotNull Material netherite
    ) {
        this.leather   = leather;
        this.chainmail = chainmail;
        this.iron      = iron;
        this.gold      = gold;
        this.diamond   = diamond;
        this.netherite = netherite;
    }

    // GETTERS
    public @NotNull Material leather () {
        return this.leather;
    }
    public @NotNull Material chainmail () {
        return this.chainmail;
    }
    public @NotNull Material iron () {
        return this.iron;
    }
    public @NotNull Material gold () {
        return this.gold;
    }
    public @NotNull Material diamond () {
        return this.diamond;
    }
    public @NotNull Material netherite () {
        return this.netherite;
    }
}
