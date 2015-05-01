package io.github.mac_genius.drugseller;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;

/**
 * Created by Mac on 4/29/2015.
 */
public class CancelAfterTime implements Runnable {
    private Listener listener;
    private Player player;
    private ArrayList<RegisteredListener> listeners;
    private ArrayList<HandlerList> handlerLists;
    private Plugin plugin;

    public CancelAfterTime(Plugin pluginIn, Listener listenerIn, Player playerIn) {
        plugin = pluginIn;
        listeners = HandlerList.getRegisteredListeners(plugin);
        handlerLists = HandlerList.getHandlerLists();
        listener = listenerIn;
        player = playerIn;
    }

    @Override
    public void run() {
        for (HandlerList l : handlerLists) {
            for (RegisteredListener r : l.getRegisteredListeners()) {
                if (r.getListener() == listener) {
                    l.unregister(listener);
                    player.sendMessage("Remove Entity Cancelled.");
                }
            }
        }
    }
}
