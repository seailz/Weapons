package com.seailz.weapons.weapons;

import com.seailz.weapons.utils.C;
import com.seailz.weapons.weapons.i.Weapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Netherite implements Weapon, Listener {
    @Override
    public void effect1(Location loc, Player player) {
        // no effect.
    }

    @Override
    public void effect2(Location loc, Player player) {
        // no effect.
    }

    @Override
    public boolean isItem(@NotNull ItemStack item) {
        return checkItemType(item) && searchItemLoreForId(item);
    }

    @Override
    public int itemId() {
        return 1;
    }

    private boolean checkItemType(ItemStack item) {
        return item.getType() == Material.NETHERITE_HELMET ||
                item.getType() == Material.NETHERITE_CHESTPLATE ||
                item.getType() == Material.NETHERITE_LEGGINGS ||
                item.getType() == Material.NETHERITE_BOOTS;
    }

    @EventHandler
    public void removeDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        boolean isWearingArmor = false;
        for (ItemStack armorContent : ((Player) e.getEntity()).getInventory().getArmorContents()) {
            if (isItem(armorContent)) isWearingArmor = true;
        }
        if (!isWearingArmor) return;
        // damager and damaged are players, and damaged is wearing one of the armor pieces
        e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, e.getDamage(EntityDamageEvent.DamageModifier.ARMOR) - 4);
    }

    @EventHandler
    public void toughness(PlayerItemDamageEvent e) {
        if (!isItem(e.getItem())) return;
        Random random = new Random();
        e.setCancelled(random.nextInt(2) == 0);
    }

    @Override
    public @NotNull ItemStack create(@NotNull ItemStack item) throws IllegalArgumentException {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(C.t("&7Item: " + itemId()));
        lore.add(C.t("&d&lEnhanced Netherite"));
        lore.add(C.t("&7Offers more protection"));
        meta.setDisplayName(C.t("&dEnhanced Netherite"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
