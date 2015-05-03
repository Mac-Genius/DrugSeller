package io.github.mac_genius.drugseller;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * Created by Mac on 4/29/2015.
 */
public class Listeners implements Listener {
    private Economy economy;
    private Plugin plugin;
    private ArrayList<Entity> entities;

    public Listeners(Economy economyIn, Plugin pluginIn, ArrayList<Entity> entitiesIn) {
        plugin = pluginIn;
        economy = economyIn;
        entities = entitiesIn;
    }

    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent event) {
        String inventoryName = plugin.getConfig().getString("inventory name");
        inventoryName = ChatColor.translateAlternateColorCodes('&', inventoryName);
        Entity entity = event.getRightClicked();
        for (Entity e : entities) {
            if (entity == e) {
                plugin.getServer().getScheduler().runTaskLater(plugin, new InventoryCreator(event.getPlayer(), inventoryName), 1);
            }
        }
    }


    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        DecimalFormat decimal = new DecimalFormat("#,##0.00");
        String inventoryName = plugin.getConfig().getString("inventory name");
        ArrayList<String> customItems = new ArrayList<>(plugin.getConfig().getStringList("products"));
        inventoryName = ChatColor.translateAlternateColorCodes('&', inventoryName);
        String moneyMessage = plugin.getConfig().getString("messages.moneyReceived");
        moneyMessage = ChatColor.translateAlternateColorCodes('&', moneyMessage);
        Player player = (Player) event.getPlayer();
        if (player.getOpenInventory().getTopInventory().getTitle().equals(inventoryName)) {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(player.getOpenInventory().getTopInventory().getContents()));
            double money = 0;
            for (ItemStack s : items) {
                for (String item : customItems) {
                    Scanner scan = new Scanner(item);
                    String itemName = scan.next();
                    double itemPrice = Double.parseDouble(scan.next());
                    String customName = "";
                    if (scan.hasNext()) {
                        customName = scan.next();
                    }
                    if (customName.equals("")) {
                        if (s != null && s.getType() != null && s.getType() == Material.getMaterial(itemName)) {
                            int amount = s.getAmount();
                            money += amount * itemPrice;
                        }
                    }
                    else if (s != null && s.getType() != null && s.getType() == Material.getMaterial(itemName) && s.getItemMeta() != null && s.getItemMeta().getDisplayName().equals(customName)) {
                        int amount = s.getAmount();
                        money += amount * itemPrice;
                    }
                }
            }
            if (money > 0) {
                economy.depositPlayer((OfflinePlayer) player, money);
                moneyMessage = moneyMessage.replaceAll("%money%", decimal.format(money) + "");
                player.sendMessage(moneyMessage);
            }
        }
    }

    @EventHandler
    public void noDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        for (Entity e : entities) {
            if (e == entity) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void wrongItemClick(InventoryClickEvent event) {
        String inventoryName = plugin.getConfig().getString("inventory name");
        inventoryName = ChatColor.translateAlternateColorCodes('&', inventoryName);
        if (event.getWhoClicked().getOpenInventory().getTopInventory().getTitle().equals(inventoryName)) {
            ArrayList<String> customItems = new ArrayList<>(plugin.getConfig().getStringList("products"));
            for (String item : customItems) {
                Scanner scan = new Scanner(item);
                Material itemName = Material.getMaterial(scan.next());
                double itemPrice = Double.parseDouble(scan.next());
                String customName = "";
                if (scan.hasNext()) {
                    customName = scan.next();
                }
                if (customName.equals("")) {
                    if (event.getCurrentItem() != null && event.getCurrentItem().getType() != null && event.getCurrentItem().getType() == itemName) {
                        return;
                    }
                }
                else if (event.getCurrentItem() != null && event.getCurrentItem().getType() != null
                        && event.getCurrentItem().getType() == itemName && event.getCurrentItem().getItemMeta() != null
                        && event.getCurrentItem().getItemMeta().getDisplayName() != null
                        && event.getCurrentItem().getItemMeta().getDisplayName().equals(customName)) {
                    return;
                }
                else if (event.getCurrentItem() != null && event.getCurrentItem().getType() != null && event.getCurrentItem().getType() == Material.AIR) {
                    return;
                }
            }
            event.setCancelled(true);
        }
    }
}
