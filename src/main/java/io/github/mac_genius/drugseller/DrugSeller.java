package io.github.mac_genius.drugseller;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Created by Mac on 4/28/2015.
 */
public class DrugSeller extends JavaPlugin {
    private Plugin plugin = this;
    private Economy economy;
    private static ArrayList<Entity> dealers;

    @Override
    public void onEnable() {
        try {
            configSetup();
        } catch (IOException e) {
            getLogger().warning("Couldn't setup the config!");
        }
        setupEntities();
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new CancelFocus(plugin, dealers), 0, 1);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new NoMoving(plugin, dealers), 0, 1);
        setupEconomy();
        this.getServer().getPluginManager().registerEvents(new Listeners(economy, plugin, dealers), this);
        this.getCommand("dealer").setExecutor(new Commands(plugin, dealers));
        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        plugin.saveConfig();
        getLogger().info("Plugin disabled.");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    private void setupEntities() {
        ArrayList<String> uuid = new ArrayList<>(plugin.getConfig().getStringList("dealers"));
        dealers = new ArrayList<>();
        ArrayList<World> worlds = new ArrayList<>(plugin.getServer().getWorlds());
        for (World w : worlds) {
            ArrayList<Entity> entities = new ArrayList<>(w.getEntities());
            for (Entity e : entities) {
                for (String uuids : uuid) {
                    if (e.getUniqueId().toString().equals(uuids)) {
                        dealers.add(e);
                    }
                }
            }
        }
    }

    private void configSetup() throws IOException {
        plugin.saveDefaultConfig();
        if (!plugin.getConfig().getString("version").equals("1.1")) {
            getLogger().info("Config outdated. Updating now.");
            ArrayList<String> products = new ArrayList<>(plugin.getConfig().getStringList("products"));
            ArrayList<String> entities = new ArrayList<>(plugin.getConfig().getStringList("dealers"));
            String moneyReceived = plugin.getConfig().getString("messages.moneyReceived");
            String inventoryName = plugin.getConfig().getString("inventory name");
            File config = new File(this.getDataFolder(), "config.yml");
            Files.delete(config.toPath());
            plugin.reloadConfig();
            plugin.saveDefaultConfig();
            plugin.getConfig().set("version", "1.1");
            plugin.getConfig().set("messages.moneyReceived", moneyReceived);
            plugin.getConfig().set("inventory name", inventoryName);
            plugin.getConfig().set("products", products);
            plugin.getConfig().set("dealers", entities);
            plugin.saveConfig();
        }
    }
}
