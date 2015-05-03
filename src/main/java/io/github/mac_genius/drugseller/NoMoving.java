package io.github.mac_genius.drugseller;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
 * Created by Mac on 4/29/2015.
 */
public class NoMoving implements Runnable {
    private Plugin plugin;
    private ArrayList<Entity> entities;

    public NoMoving(Plugin pluginIn, ArrayList<Entity> entitiesIn) {
        plugin = pluginIn;
        entities = entitiesIn;
    }


    @Override
    public synchronized void run() {
        for (Entity entity : entities) {
            if (entity.getVelocity().getX() != 0 && entity.getVelocity().getZ() != 0) {
                Double directionX = entity.getVelocity().getX() * -1;
                Double directionY = 0.0;
                Double directionZ = entity.getVelocity().getZ() * -1;
                Vector opposite = new Vector(directionX, directionY, directionZ);
                opposite.add(entity.getVelocity());
                entity.setVelocity(opposite);
            }

        }
    }
}
