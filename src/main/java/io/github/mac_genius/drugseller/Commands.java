package io.github.mac_genius.drugseller;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/**
 * Created by Mac on 4/28/2015.
 */
public class Commands implements CommandExecutor {
    private Plugin plugin;
    private ArrayList<IronGolem> ironGolems;

    public Commands(Plugin pluginIn, ArrayList<IronGolem> ironGolemsIn) {
        plugin = pluginIn;
        ironGolems = ironGolemsIn;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        String name = plugin.getConfig().getString("dealer name");
        name = ChatColor.translateAlternateColorCodes('&', name);
        if (commandSender instanceof Player) {
            if (command.getName().equalsIgnoreCase("dealer") && commandSender.hasPermission("drugseller.help")) {
                if (args.length == 0) {
                    commandSender.sendMessage(ChatColor.GREEN + "---------- DrugSeller Help ----------");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer reload" + ChatColor.WHITE + " reloads the config");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer help" + ChatColor.WHITE + " commands");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer create" + ChatColor.WHITE + " creates a new dealer where you are standing.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload") && commandSender.hasPermission("drugseller.reload")) {
                    ArrayList<IronGolem> before = ironGolems;
                    plugin.reloadConfig();
                    name = plugin.getConfig().getString("dealer name");
                    name = ChatColor.translateAlternateColorCodes('&', name);
                    commandSender.sendMessage(ChatColor.GREEN + "[DrugSeller] " + ChatColor.WHITE + "Config reloaded!");
                    for (IronGolem i : ironGolems) {
                        i.setCustomName(name);
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("create") && commandSender.hasPermission("drugseller.create")) {
                    Entity ironGolem = ((Player) commandSender).getWorld().spawnEntity(((Player) commandSender).getLocation(), EntityType.IRON_GOLEM);
                    if (ironGolem instanceof IronGolem) {
                        ironGolems.add((IronGolem) ironGolem);
                        ironGolem.setCustomName(name);
                        ironGolem.setCustomNameVisible(true);
                        PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 1000000000, 255, false, false);
                        ((IronGolem) ironGolem).addPotionEffect(slowness);
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove") && commandSender.hasPermission("drugseller.remove")) {
                    plugin.getServer().getPluginManager().registerEvents(new RemoveEntity(plugin, (Player) commandSender, ironGolems), plugin);
                    commandSender.sendMessage("Please left click on the entity you would like to remove.");
                    return true;
                }

            }
        }
        return false;
    }
}
