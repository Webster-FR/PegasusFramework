package fr.pegasus.papermc.builders.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class SkullBuilder extends ItemBuilder {
    // FIELDS
    private final SkullMeta meta;

    // CONSTRUCTORS
    public SkullBuilder () {
        super (Material.PLAYER_HEAD);
        this.meta = (SkullMeta) super.getMeta();
    }
    public SkullBuilder(final @NotNull ItemStack item) {
        super(item);
        this.meta = (SkullMeta) super.getMeta();
    }
    public SkullBuilder(final int amount) {
        super(Material.PLAYER_HEAD, amount);
        this.meta = (SkullMeta) super.getMeta();
    }

    // SETTERS
    public @NotNull SkullBuilder setOwningPlayer (final @Nullable OfflinePlayer player) {
        this.meta.setOwningPlayer(player);
        return this;
    }
    public @NotNull SkullBuilder setCustomTexture (final @NotNull String texture) {
        PlayerProfile profile = this.meta.getPlayerProfile();
        if (Objects.isNull(profile)) profile = Bukkit.createProfile(UUID.randomUUID());

        profile.getProperties().add(new ProfileProperty("textures", texture));
        this.meta.setPlayerProfile(profile);
        return this;
    }

    // BUILD
    @Override
    public @NotNull ItemStack make () {
        return super.make(this.meta);
    }

    // CLONE
    @Override
    public @NotNull SkullBuilder clone () {
        return (SkullBuilder) super.clone();
    }
}
