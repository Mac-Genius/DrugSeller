package io.github.mac_genius.drugseller;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * Created by Mac on 4/28/2015.
 */
public class DrugSeller extends JavaPlugin {
    private Plugin plugin = this;
    private Economy economy;
    private static ArrayList<IronGolem> ironGolems;

    @Override
    public void onEnable() {
        setupIronGolems();
        this.saveDefaultConfig();
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new NoMoving(plugin, ironGolems), 0, 1);
        setupEconomy();
        this.getServer().getPluginManager().registerEvents(new Listeners(economy, plugin), this);
        this.getCommand("dealer").setExecutor(new Commands(plugin, ironGolems));
        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    private void setupIronGolems() {
        ironGolems = new ArrayList<>();
        ArrayList<World> worlds = new ArrayList<>(plugin.getServer().getWorlds());
        for (World w : worlds) {
            ArrayList<Entity> entities = new ArrayList<>(w.getEntities());
            for (Entity e : entities) {
                if (e instanceof IronGolem) {
                    ironGolems.add((IronGolem) e);
                }
            }
        }
    }
}
