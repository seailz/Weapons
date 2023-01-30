package com.seailz.weapons.weapons;

import com.seailz.weapons.utils.C;
import com.seailz.weapons.weapons.i.Weapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Trident weapon.
 * Effects:
 * <ul>
 *     <li>
 *         Smash the ground and launch blocks in a 10 block radius and 5 blocks down and also deals 10 block kb horizontal and vertical
 *     </li>
 *     <li>
 *         Encases in a ball of ice thats 5x5x5 and melts after 5 seconds
 *     </li>
 * </ul>
 *
 * <br>
 * Hidden effects:
 * <ul>
 *     <li>
 *         riptide charges 2x faster and loyalty charges 2x faster and they are able to be applied together but it cant have channeling
 *     </li>
 * </ul>
 */
public class Trident implements Weapon, Listener {
    private final int ITEM_ID = 0;
    private final Material ITEM_MATERIAL = Material.TRIDENT;

    /**
     * Effect 1 for this weapon is
     * <pre>
     * Smash the ground and launch blocks in a 10 block radius and 5 blocks down and also deals 10 block kb horizontal and vertical
     * </pre>
     * Activated by right-clicking.
     */
    @Override
    public void effect1(Location location, Player player) {
        List<Block> blocks = new ArrayList<>();
        int radius = 10;
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        double x = 10;
        double y = 10;
        double z = 10;
        World w = location.getWorld();
        for (Block b : blocks) {
            Location bLoc = b.getLocation();
            x = bLoc.getX() - location.getX();
            y = bLoc.getY() - location.getY() + 0.5;
            z = bLoc.getZ() - location.getZ();

            FallingBlock fb = w.spawnFallingBlock(bLoc, b.getType(), (byte) b.getData());
            fb.setDropItem(false);
            fb.setVelocity(new Vector(x, y, z));
        }

        for (Player p : location.getWorld().getPlayers()) {
            if (p.getLocation().distance(location) <= 10) {
                p.setVelocity(new Vector(10, 10, 10));
            }
        }
    }

    @Override
    public void effect2() {

    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if (!isItem(event.getItem())) return;
        if (event.getEnchantsToAdd().containsKey(Enchantment.CHANNELING)) {
            event.setCancelled(true);
            event.getEnchanter().sendMessage(C.t("&cYou cannot enchant this item with channeling!"));
            return;
        }

        if (event.getEnchantsToAdd().containsKey(Enchantment.LOYALTY)) {
            event.getEnchantsToAdd().put(Enchantment.LOYALTY, event.getEnchantsToAdd().get(Enchantment.LOYALTY) * 2);
        }

        if (event.getEnchantsToAdd().containsKey(Enchantment.RIPTIDE)) {
            event.getEnchantsToAdd().put(Enchantment.RIPTIDE, event.getEnchantsToAdd().get(Enchantment.RIPTIDE) * 2);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!isItem(event.getPlayer().getInventory().getItemInMainHand())) return;
        if (!event.isSneaking()) return;
        effect1(event.getPlayer().getLocation(), event.getPlayer());
    }

    @Override
    public boolean isItem(@NotNull ItemStack item) {
        if (item.getItemMeta() == null) return false;
        if (item.getItemMeta().getLore() == null) return false;
        return item.getItemMeta().getLore()
                .get(0)
                .split("Item: ")[1]
                .equals(ITEM_ID + "");
    }

    @Override
    public @NotNull ItemStack create(@NotNull ItemStack item) throws IllegalArgumentException {
        if (item.getType() != ITEM_MATERIAL) throw new IllegalArgumentException("Item is not a " + ITEM_MATERIAL.name() + "!");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(C.t("&b&lTrident"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(C.t("&7Item: " + ITEM_ID));
        lore.add(C.t("&bRight Click &7to use effect 1"));
        lore.add(C.t("&bSneak &7to use effect 2"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
