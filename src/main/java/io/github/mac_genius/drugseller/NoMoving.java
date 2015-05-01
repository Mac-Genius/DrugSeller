package io.github.mac_genius.drugseller;

import org.bukkit.ChatColor;
import org.bukkit.entity.IronGolem;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
 * Created by Mac on 4/29/2015.
 */
public class NoMoving implements Runnable {
    private Plugin plugin;
    private ArrayList<IronGolem> ironGolems;

    public NoMoving(Plugin pluginIn, ArrayList<IronGolem> ironGolemsIn) {
        plugin = pluginIn;
        ironGolems = ironGolemsIn;
    }


    @Override
    public synchronized void run() {
        String name = plugin.getConfig().getString("dealer name");
        name = ChatColor.translateAlternateColorCodes('&', name);
        for (IronGolem ironGolem : ironGolems) {
            if (!ironGolem.isPlayerCreated() && ironGolem.getName().equals(name) && ironGolem.isCustomNameVisible()) {
                Vector vector = new Vector(0.0, 0.0, 0.0);
                ironGolem.setVelocity(vector);
            }
        }
    }
}
