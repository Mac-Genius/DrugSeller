package io.github.mac_genius.drugseller;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

/**
 * Created by Mac on 5/2/2015.
 */
public class CancelFocus implements Runnable {
    private Plugin plugin;
    private ArrayList<Entity> entities;

    public CancelFocus(Plugin pluginIn, ArrayList<Entity> entitiesIn) {
        plugin = pluginIn;
        entities = entitiesIn;
    }

    @Override
    public synchronized void run() {
        synchronized (entities) {
            for (Entity e : entities) {
                if (e instanceof Creature) {
                    if (((Creature) e).getTarget() instanceof Monster) {
                        ((Creature) e).setTarget(null);
                    }
                }
                ArrayList<Entity> nearby = new ArrayList<>(e.getNearbyEntities(20, 20, 20));
                for (Entity n : nearby) {
                    if (n instanceof Monster) {
                        if (((Monster) n).getTarget() == e) {
                            ((Monster) n).setTarget(null);
                        }
                    }
                }
                ArrayList<Entity> reallyClose = new ArrayList<>(e.getNearbyEntities(1, 1, 1));
                for (Entity b : reallyClose) {
                    if (b instanceof Monster) {
                        if (!(((Monster) b).getTarget() instanceof Player)) {
                            ((Monster) b).remove();
                        }
                    }
                }
            }
        }
    }
}
