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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetheriteWeapon implements Weapon, Listener {
    @Override
    public void effect1(Location loc, Player player) {
        // No effect.
    }

    @Override
    public void effect2(Location loc, Player player) {
        // No effect.
    }

    @Override
    public boolean isItem(@NotNull ItemStack item) {
        return checkItemType(item) && searchItemLoreForId(item);
    }

    @EventHandler
    public void addDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        if (!isItem(((Player) e.getDamager()).getInventory().getItemInMainHand())) return;
        e.setDamage(e.getDamage() + 2);
    }

    private boolean checkItemType(ItemStack item) {
        return item.getType() == Material.NETHERITE_SWORD ||
                item.getType() == Material.NETHERITE_AXE;
    }

    @Override
    public int itemId() {
        return 2;
    }

    @Override
    public @NotNull ItemStack create(@NotNull ItemStack item) throws IllegalArgumentException {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(C.t("&7Item: " + itemId()));
        lore.add(C.t("&d&lEnhanced Netherite"));
        lore.add(C.t("&7Offers more attack damage"));
        meta.setDisplayName(C.t("&dEnhanced Netherite"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
