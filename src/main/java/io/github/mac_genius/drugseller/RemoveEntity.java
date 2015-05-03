package io.github.mac_genius.drugseller;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;

/**
 * Created by Mac on 4/29/2015.
 */
public class RemoveEntity implements Listener {
    private Plugin plugin;
    private ArrayList<RegisteredListener> listeners;
    private ArrayList<HandlerList> handlerLists;
    private Player hitter;
    private ArrayList<Entity> entities;

    public RemoveEntity(Plugin pluginIn, Player hitterIn, ArrayList<Entity> entitiesIn) {
        plugin = pluginIn;
        listeners = HandlerList.getRegisteredListeners(plugin);
        handlerLists = HandlerList.getHandlerLists();
        hitter = hitterIn;
        plugin.getServer().getScheduler().runTaskLater(plugin, new CancelAfterTime(plugin, this, hitter), 200);
        entities = entitiesIn;
    }

    @EventHandler
    public void removeEntity(EntityDamageByEntityEvent event) {
        ArrayList<String> uuid = new ArrayList<>(plugin.getConfig().getStringList("dealers"));
        if (event.getDamager().getName().equals(hitter.getName())) {
            for (HandlerList l : handlerLists) {
                for (RegisteredListener r : l.getRegisteredListeners()) {
                    if (r.getListener() == this) {
                        l.unregister(this);
                    }
                }
            }
            Entity entity = event.getEntity();
            entity.remove();
            event.setCancelled(true);
            synchronized (entities) {
                for (Entity i : entities) {
                    if (entity == i) {
                        for (String uuids : uuid) {
                            if (entity.getUniqueId().toString().equals(uuids)) {
                                entities.remove(entity);
                                hitter.sendMessage("Entity Removed");
                                uuid.remove(uuids);
                                plugin.getConfig().set("dealers", uuid);
                                plugin.saveConfig();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
