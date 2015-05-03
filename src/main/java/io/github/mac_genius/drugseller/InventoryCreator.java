package io.github.mac_genius.drugseller;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.Inventory;

/**
 * Created by Mac on 4/29/2015.
 */
public class InventoryCreator implements Runnable {
    private Player player;
    private String name;

    public InventoryCreator(Player playerIn, String nameIn) {
        player = playerIn;
        name = nameIn;
    }

    @Override
    public void run() {
        Inventory drugSeller = Bukkit.createInventory(player, 54, name);
        player.openInventory(drugSeller);
    }
}
