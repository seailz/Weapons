package com.seailz.weapons.weapons.i;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a weapon.
 */
public interface Weapon {

    /**
     * The first effect of the weapon.
     */
    void effect1(Location loc, Player player);

    /**
     * The second effect of the weapon.
     */
    void effect2(Location loc);

    /**
     * Check if the item is the weapon.
     * @param item the item to check
     * @return true if the item is the weapon
     */
    boolean isItem(@NotNull ItemStack item);

    /**
     * Transform the item into a weapon
     * @param item the item to transform
     * @throws IllegalArgumentException if the item is not of the type expected
     */
    @NotNull
    ItemStack create(@NotNull ItemStack item) throws IllegalArgumentException;
}
