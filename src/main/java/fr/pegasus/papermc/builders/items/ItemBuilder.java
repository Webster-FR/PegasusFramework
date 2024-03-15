package fr.pegasus.papermc.builders.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ItemBuilder implements Cloneable {
    // FIELDS
    private final ItemStack item;
    private final ItemMeta meta;

    // CONSTRUCTORS
    public ItemBuilder (final @NotNull ItemStack item) {
        this.item = item;
        this.meta = this.item.getItemMeta();
    }
    public ItemBuilder (final @NotNull Material material, final int amount) {
        this(new ItemStack(material, amount));
    }
    public ItemBuilder (final @NotNull Material material) {
        this(material, 1);
    }

    // METHODS
    private void applyMeta (final @NotNull ItemMeta meta) {
        this.item.setItemMeta(meta);
    }

    // SETTERS
    public @NotNull ItemBuilder setDisplayName (final @Nullable Component name) {
        this.meta.displayName(name);
        return this;
    }
    public @NotNull ItemBuilder setLore (final @NotNull Component... lines) {
        this.meta.lore(List.of(lines));
        return this;
    }
    public @NotNull ItemBuilder addEnchantment (final @NotNull Enchantment enchantment, final int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }
    public @NotNull ItemBuilder addEnchantments (final @NotNull Map<Enchantment, Integer> enchantments) {
        enchantments.forEach(this::addEnchantment);
        return this;
    }
    public @NotNull ItemBuilder removeEnchantment (final @NotNull Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }
    public @NotNull ItemBuilder removeEnchantments (final @NotNull Enchantment... enchantments) {
        List.of(enchantments).forEach(this::removeEnchantment);
        return this;
    }
    public @NotNull ItemBuilder addFlags (final @NotNull ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }
    public @NotNull ItemBuilder removeFlags (final @NotNull ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }
    public @NotNull ItemBuilder setUnbreakable (final boolean state, final boolean hide) {
        this.meta.setUnbreakable(state);
        if (hide) return this.addFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }
    public @NotNull ItemBuilder setCustomModelData (final int data) {
        this.meta.setCustomModelData(data);
        return this;
    }

    // GETTERS
    protected @NotNull ItemMeta getMeta () {
        return this.meta;
    }

    // BUILD
    public @NotNull ItemStack make () {
        return this.make(this.meta);
    }
    protected @NotNull ItemStack make (final @NotNull ItemMeta meta) {
        this.applyMeta(meta);
        return this.item;
    }

    // CLONE
    @Override
    public @NotNull ItemBuilder clone () {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
