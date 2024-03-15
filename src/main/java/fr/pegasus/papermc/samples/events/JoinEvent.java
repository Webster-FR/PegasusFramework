package fr.pegasus.papermc.samples.events;

import fr.pegasus.papermc.builders.items.ArmorBuilder;
import fr.pegasus.papermc.builders.items.ItemBuilder;
import fr.pegasus.papermc.builders.items.SkullBuilder;
import fr.pegasus.papermc.builders.items.enums.ArmorParts;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

public class JoinEvent implements Listener {
    @EventHandler
    public void join (final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        player.getInventory().clear();
        player.getInventory().setItem(
                4,
                new SkullBuilder()
                        .setCustomTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg5MTA4NjM0YTZmZjkzZDQyM2YxMDQzZWVhYTA3ZmU3ZTNiZjUzMGEzYmQ5M2I4MThhMjRmNzdlNjMwMWEyNyJ9fX0=")
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
