package com.seailz.weapons.commands;

import com.seailz.weapons.Weapons;
import com.seailz.weapons.utils.C;
import com.seailz.weapons.weapons.Trident;
import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "createweapon",
        description = "Create a weapon",
        aliases = {"cw"},
        permission = "weapons.createweapon",
        playerOnly = true,
        args = "name"
)
public class CommandCreateWeapon extends Command {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "trident":
                try {
                    ((Player) sender).getInventory().setItem(((Player) sender).getInventory().getHeldItemSlot(),
                    new Trident().create(((Player) sender).getInventory().getItemInMainHand()));
                    sender.sendMessage(C.t("&aWeapon created successfully"));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(C.t("&cInvalid item. Please make sure you are holding a trident"));
                }
                break;
            default:
                sender.sendMessage(C.t("&cInvalid weapon name"));
                break;
        }
    }
}
