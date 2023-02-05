package com.seailz.weapons;

import com.seailz.weapons.commands.CommandCreateWeapon;
import com.seailz.weapons.weapons.Netherite;
import com.seailz.weapons.weapons.NetheriteWeapon;
import com.seailz.weapons.weapons.Trident;
import games.negative.framework.BasePlugin;

public final class Weapons extends BasePlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        // Plugin startup logic
        registerListeners(
                new Trident(),
                new Netherite(),
                new NetheriteWeapon()
        );
        registerCommands(
                new CommandCreateWeapon()
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
