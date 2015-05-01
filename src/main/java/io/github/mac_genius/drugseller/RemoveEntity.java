package io.github.mac_genius.drugseller;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/**
 * Created by Mac on 4/29/2015.
 */
public class RemoveEntity implements Listener {
    private Plugin plugin;
    private ArrayList<RegisteredListener> listeners;
    private ArrayList<HandlerList> handlerLists;
    private Player hitter;
    private ArrayList<IronGolem> ironGolems;

    public RemoveEntity(Plugin pluginIn, Player hitterIn, ArrayList<IronGolem> ironGolemsIn) {
        plugin = pluginIn;
        listeners = HandlerList.getRegisteredListeners(plugin);
        handlerLists = HandlerList.getHandlerLists();
        hitter = hitterIn;
        plugin.getServer().getScheduler().runTaskLater(plugin, new CancelAfterTime(plugin, this, hitter), 200);
        ironGolems = ironGolemsIn;
    }

    @EventHandler
    public void removeEntity(EntityDamageByEntityEvent event) {
        String name = plugin.getConfig().getString("dealer name");
        name = ChatColor.translateAlternateColorCodes('&', name);
        if (event.getDamager().getName().equals(hitter.getName())) {
            for (HandlerList l : handlerLists) {
                for (RegisteredListener r : l.getRegisteredListeners()) {
                    if (r.getListener() == this) {
                        l.unregister(this);
                    }
                }
            }
            Entity entity = event.getEntity();
            if (entity instanceof IronGolem) {
                if (!((IronGolem) entity).isPlayerCreated() && entity.getName().equals(name) && entity.isCustomNameVisible()) {
                    ((IronGolem) entity).setHealth(0);
                    event.setCancelled(true);
                    synchronized (ironGolems) {
                        for (IronGolem i : ironGolems) {
                            if ((IronGolem) entity == i) {
                                ironGolems.remove(entity);
                                hitter.sendMessage("Entity Removed");
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
