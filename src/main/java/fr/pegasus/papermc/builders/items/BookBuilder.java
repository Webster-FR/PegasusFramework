package fr.pegasus.papermc.builders.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class BookBuilder extends ItemBuilder {
    // FIELDS
    private final BookMeta meta;

    // CONSTRUCTORS
    public BookBuilder () {
        super(Material.WRITTEN_BOOK);
        this.meta = (BookMeta) super.getMeta();
    }
    public BookBuilder (final @NotNull ItemStack item) {
        super(item);
        this.meta = (BookMeta) super.getMeta();
    }
    public BookBuilder (final int amount) {
        super(Material.WRITTEN_BOOK, amount);
        this.meta = (BookMeta) super.getMeta();
    }

    // SETTERS
    public @NotNull BookBuilder setAuthor (final @Nullable Component author) {
        this.meta.author(author);
        return this;
    }
    public @NotNull BookBuilder addPages (final @NotNull Component... pages) {
        this.meta.addPages(pages);
        return this;
    }
    public @NotNull BookBuilder setGeneration (final @Nullable BookMeta.Generation generation) {
        this.meta.setGeneration(generation);
        return this;
    }
    public @NotNull BookBuilder setTitle (final @Nullable Component title) {
        this.meta.title(title);
        return this;
    }

    // BUILD
    @Override
    public @NotNull ItemStack make () {
        return super.make(this.meta);
    }

    // CLONE
    @Override
    public @NotNull BookBuilder clone () {
        return (BookBuilder) super.clone();
    }
}
