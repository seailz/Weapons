package com.seailz.weapons.weapons;

import com.seailz.weapons.Weapons;
import com.seailz.weapons.utils.C;
import com.seailz.weapons.utils.Sphere;
import com.seailz.weapons.weapons.i.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
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
    private boolean effect1IsOnCooldown = false;
    private boolean effect2IsOnCooldown = false;

    private static final List<Player> explosionDamagePrevented = new ArrayList<>();

    /**
     * Effect 1 for this weapon is
     * <pre>
     * Smash the ground and launch blocks in a 10 block radius and 5 blocks down and also deals 10 block kb horizontal and vertical
     * </pre>
     * Activated by right-clicking.
     */
    @Override
    public void effect1(Location location, Player player) {
        if (effect1IsOnCooldown) {
            player.sendMessage(C.t("&7This effect is currently on &ccooldown&7."));
            return;
        }

        for (Player p : location.getWorld().getPlayers()) {
            if (p.getLocation().distance(location) <= 10) {
                if (!p.equals(player)) p.setVelocity(new Vector(1, 1, 1));
                if (!p.equals(player)) p.setHealth(p.getHealth() - 10);
            }
        }
        // make player invincible to explosions

        explosionDamagePrevented.add(player);
        player.getWorld().createExplosion(location, 4, true);
        Bukkit.getScheduler().runTaskLater(Weapons.getInst(), () -> {
            explosionDamagePrevented.remove(player);
        }, 10L);

        effect1IsOnCooldown = true;
        Bukkit.getScheduler().runTaskLater(Weapons.getInst(), () -> {
            effect1IsOnCooldown = false;
            player.sendMessage(C.t("&c&lGround Explosion&7 is recharged!"));
        }, 100L);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player)  e.getEntity();
        if (explosionDamagePrevented.contains(player) && (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosion(BlockExplodeEvent e) {
        List<Block> blocks = e.blockList();

        double x = 0;
        double y = 0;
        double z = 0;
        World w = e.getBlock().getWorld();
        for (int i = 0; i < e.blockList().size();i++){
            Block b = e.blockList().get(i);
            Location bLoc = b.getLocation();
            x = bLoc.getX() -  e.getBlock().getX();
            y = bLoc.getY() -  e.getBlock().getY() + 0.5;
            z = bLoc.getZ() -  e.getBlock().getZ();
            FallingBlock fb = w.spawnFallingBlock(bLoc, b.getType(), (byte)b.getData());
            fb.setDropItem(false);
            fb.setVelocity(new Vector(x,y,z));
        }

    }

    @Override
    public void effect2(Location loc, Player player) {
        if (effect2IsOnCooldown) {
            player.sendMessage(C.t("&7This effect is currently on &ccooldown&7."));
            return;
        }
        List<Location> locations = Sphere.generateSphere(loc, Material.ICE, 5, true);
        // after 5 seconds, get rid of the sphere
        Bukkit.getScheduler().runTaskLater(Weapons.getInst(), () -> {
            for (Location location : locations) {
                Bukkit.getScheduler().runTaskLater(Weapons.getInst(), () -> {
                    location.getBlock().setType(Material.AIR);
                }, 20L);
            }
        }, 100L);
        effect2IsOnCooldown = true;
        Bukkit.getScheduler().runTaskLater(Weapons.getInst(), () -> {
            effect2IsOnCooldown = false;
            player.sendMessage(C.t("&b&lIce Bubble&7 is recharged!"));
        }, 200L);
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if (!isItem(event.getItem())) return;
        if (event.getEnchantsToAdd().containsKey(Enchantment.CHANNELING)) {
            event.setCancelled(true);
            event.getEnchanter().sendMessage(C.t("&7You cannot enchant this item with &cchanneling&7!"));
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
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getState().hasMetadata("isBreakable1")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(C.t("&b&lIce Bubble&7 can't be broken!"));
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!isItem(event.getPlayer().getInventory().getItemInMainHand())) return;
        if (!event.isSneaking()) return;
        effect1(event.getPlayer().getLocation(), event.getPlayer());
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!isItem(event.getPlayer().getInventory().getItemInMainHand())) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        effect2(event.getPlayer().getLocation(), event.getPlayer());
    }

    @Override
    public boolean isItem(@NotNull ItemStack item) {
        return searchItemLoreForId(item);
    }

    @Override
    public int itemId() {
        return ITEM_ID;
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
