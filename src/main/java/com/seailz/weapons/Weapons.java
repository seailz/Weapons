package com.seailz.weapons;

import com.seailz.weapons.commands.CommandCreateWeapon;
import com.seailz.weapons.weapons.Trident;
import games.negative.framework.BasePlugin;

public final class Weapons extends BasePlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerListeners(
                new Trident()
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
