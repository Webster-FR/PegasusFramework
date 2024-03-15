package fr.pegasus.papermc.builders.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArmorBuilder extends ItemBuilder {
    // FIELDS
    private final ArmorMeta meta;

    // CONSTRUCTORS
    public ArmorBuilder (final @NotNull ItemStack item) {
        super(item);
        this.meta = (ArmorMeta) super.getMeta();
    }
    public ArmorBuilder (final @NotNull Material part) {
        super(part);
        this.meta = (ArmorMeta) super.getMeta();
    }

    // SETTERS
    public @NotNull ArmorBuilder setColor (final @Nullable Color color) {
        if (!(this.meta instanceof ColorableArmorMeta)) return this;
        ((ColorableArmorMeta) this.meta).setColor(color);
        return this;
    }
    public @NotNull ArmorBuilder setTrim (final @NotNull TrimPattern pattern, final @NotNull TrimMaterial material) {
        this.meta.setTrim(new ArmorTrim(material, pattern));
        return this;
    }

    // BUILD
    @Override
    public @NotNull ItemStack make () {
        return super.make(this.meta);
    }

    // GETTERS
    @Override
    public @NotNull ArmorBuilder clone () {
        return (ArmorBuilder) super.clone();
    }
}
