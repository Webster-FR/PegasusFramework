package fr.pegasus.papermc.builders.items;

import fr.pegasus.papermc.builders.items.enums.PotionTypes;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PotionBuilder extends ItemBuilder {
    // FIELDS
    private final PotionMeta meta;

    // CONSTRUCTORS
    public PotionBuilder (final @NotNull ItemStack item) {
        super(item);
        this.meta = (PotionMeta) super.getMeta();
    }
    public PotionBuilder (final @NotNull PotionTypes type) {
        super(type.material());
        this.meta = (PotionMeta) super.getMeta();
    }
    public PotionBuilder (final @NotNull PotionTypes type, final int amount) {
        super(type.material(), amount);
        this.meta = (PotionMeta) super.getMeta();
    }

    // SETTERS
    public @NotNull PotionBuilder addCustomEffect (final @NotNull PotionEffect effect, final boolean particles) {
        this.meta.addCustomEffect(effect, particles);
        return this;
    }
    public @NotNull PotionBuilder removeCustomEffect (final @NotNull PotionEffectType type) {
        this.meta.removeCustomEffect(type);
        return this;
    }
    public @NotNull PotionBuilder setCustomColor (final @Nullable Color color) {
        this.meta.setColor(color);
        return this;
    }

    // BUILD
    @Override
    public @NotNull ItemStack make () {
        return super.make(this.meta);
    }

    // CLONE
    @Override
    public @NotNull PotionBuilder clone () {
        return (PotionBuilder) super.clone();
    }
}
