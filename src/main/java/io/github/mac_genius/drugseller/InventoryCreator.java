package io.github.mac_genius.drugseller;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.Inventory;

/**
 * Created by Mac on 4/29/2015.
 */
public class InventoryCreator {

    public Inventory setup(Player playerIn, String nameIn) {
        Inventory drugSeller = Bukkit.createInventory(playerIn, 54, nameIn);
        return drugSeller;
    }
}
