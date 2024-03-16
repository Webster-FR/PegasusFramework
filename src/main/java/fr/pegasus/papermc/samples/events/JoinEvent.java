package fr.pegasus.papermc.samples.events;

import fr.pegasus.papermc.builders.items.*;
import fr.pegasus.papermc.builders.items.enums.ArmorParts;
import fr.pegasus.papermc.builders.items.enums.PotionTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

public class JoinEvent implements Listener {
    @EventHandler
    public void join (final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        player.getInventory().clear();
        player.getInventory().setItem(
                0,
                new PotionBuilder(PotionTypes.DRINK)
                        .setCustomColor(Color.BLACK)
                        .make()
        );
        player.getInventory().setItem(
                4,
                new SkullBuilder()
                        .setCustomTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg5MTA4NjM0YTZmZjkzZDQyM2YxMDQzZWVhYTA3ZmU3ZTNiZjUzMGEzYmQ5M2I4MThhMjRmNzdlNjMwMWEyNyJ9fX0=")
                        .make()
        );
        player.getInventory().setItem(
                8,
                new BookBuilder()
                        .setTitle(Component.text("Discover the API"))
                        .addPages(
                                Component.text("Bonsoir mon ami, je ne sais pas si tu te souviens de moi ?"),
                                Component.text("Je suis sûr que si... --->"),
                                Component.text("JE SUIS TON PERE !")
                        )
                        .setAuthor(Component.text("Your mama"))
                        .setGeneration(BookMeta.Generation.ORIGINAL)
                        .make()
        );
        player.getInventory().setHelmet(
                new ItemBuilder(Material.BLACK_STAINED_GLASS)
                        .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                        .addFlags(ItemFlag.HIDE_ENCHANTS)
                        .make()
        );
        player.getInventory().setChestplate(
                new ArmorBuilder(ArmorParts.CHEST.netherite())
                        .setTrim(TrimPattern.RAISER, TrimMaterial.DIAMOND)
                        .make()
        );
    }
}
