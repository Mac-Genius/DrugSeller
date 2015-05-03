package io.github.mac_genius.drugseller;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/**
 * Created by Mac on 4/28/2015.
 */
public class Commands implements CommandExecutor {
    private Plugin plugin;
    private ArrayList<Entity> entities;

    public Commands(Plugin pluginIn, ArrayList<Entity> entitiesIn) {
        plugin = pluginIn;
        entities = entitiesIn;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            if (command.getName().equalsIgnoreCase("dealer") && commandSender.hasPermission("drugseller.help")) {
                if (args.length == 0) {
                    commandSender.sendMessage(ChatColor.GREEN + "---------- DrugSeller Help ----------");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer reload" + ChatColor.WHITE + " reloads the config");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer help" + ChatColor.WHITE + " commands");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer create <EntityType> (name)" + ChatColor.WHITE + " creates a new dealer where you are standing.");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer remove (name)" + ChatColor.WHITE + " removes a dealer.");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer set (name)" + ChatColor.WHITE + " teleports a dealer to where you are standing.");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer gui (name)" + ChatColor.WHITE + " shows the trading gui.");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer entitylist" + ChatColor.WHITE + " shows the name of every type of entity you can use as a dealer.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("help") && commandSender.hasPermission("drugseller.help")) {
                    commandSender.sendMessage(ChatColor.GREEN + "---------- DrugSeller Help ----------");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer reload" + ChatColor.WHITE + " reloads the config");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer help" + ChatColor.WHITE + " commands");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer create <EntityType> (name)" + ChatColor.WHITE + " creates a new dealer where you are standing.");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer remove (name)" + ChatColor.WHITE + " removes a dealer.");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer set (name)" + ChatColor.WHITE + " teleports a dealer to where you are standing.");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer gui (name)" + ChatColor.WHITE + " shows the trading gui.");
                    commandSender.sendMessage(ChatColor.GOLD + "/dealer entitylist" + ChatColor.WHITE + " shows the name of every type of entity you can use as a dealer.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload") && commandSender.hasPermission("drugseller.reload")) {
                    plugin.reloadConfig();
                    commandSender.sendMessage(ChatColor.GREEN + "[DrugSeller] " + ChatColor.WHITE + "Config Reloaded!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("create") && commandSender.hasPermission("drugseller.create")) {
                    ArrayList<String> uuid = new ArrayList<>(plugin.getConfig().getStringList("dealers"));
                    try {
                        String entityType = args[1];
                        String entityName = args[2];
                        if (entityName.equalsIgnoreCase("frank")) {
                            commandSender.sendMessage("Sorry, but Frank seems to be busy at the moment. He says hello though!");
                            return true;
                        }
                        if (entityName.equalsIgnoreCase("Herobrine")) {
                            commandSender.sendMessage(ChatColor.MAGIC + "There is only one true Herobrine.");
                            return true;
                        }
                        entityName = ChatColor.translateAlternateColorCodes('&', entityName);
                        Entity entity = ((Player) commandSender).getWorld().spawnEntity(((Player) commandSender).getLocation(), EntityType.valueOf(entityType));
                        entity.setCustomName(entityName);
                        entity.setCustomNameVisible(true);
                        if (entity instanceof LivingEntity) {
                            PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 1000000000, 100, false, false);
                            ((LivingEntity) entity).addPotionEffect(slowness);
                        }
                        entities.add(entity);
                        uuid.add(entity.getUniqueId().toString());
                        plugin.getConfig().set("dealers", uuid);
                        plugin.saveConfig();
                        return true;
                    } catch (IllegalArgumentException e) {
                        commandSender.sendMessage("Please type in a valid entity.");
                        return true;
                    } catch (NullPointerException e) {
                        commandSender.sendMessage("Make sure to type in a valid entity and name.");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("remove") && commandSender.hasPermission("drugseller.remove")) {
                    ArrayList<String> uuids = new ArrayList<>(plugin.getConfig().getStringList("dealers"));
                    try {
                        for (Entity e : entities) {
                            if (e.getCustomName().equals(ChatColor.translateAlternateColorCodes('&', args[1]))) {
                                for (String u : uuids) {
                                    if (e.getUniqueId().toString().equals(u)) {
                                        e.remove();
                                        commandSender.sendMessage("The entity \"" + e.getCustomName() + ChatColor.WHITE + "\"" + " was removed.");
                                        entities.remove(e);
                                        uuids.remove(u);
                                        plugin.getConfig().set("dealers", uuids);
                                        plugin.saveConfig();
                                        return true;
                                    }
                                }
                            }
                        }
                        if (args[1].equalsIgnoreCase("frank")) {
                            commandSender.sendMessage("You can't remove Frank, why would you want to do that? D:");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("herobrine")) {
                            commandSender.sendMessage(ChatColor.MAGIC + "soijfsodifsldfnsldfn");
                            return true;
                        }
                        commandSender.sendMessage("The entity \"" + args[1] + "\" does not exist.");
                        return true;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        plugin.getServer().getPluginManager().registerEvents(new RemoveEntity(plugin, (Player) commandSender, entities), plugin);
                        commandSender.sendMessage("Please left click on the entity you would like to remove.");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("set") && commandSender.hasPermission("drugseller.set")) {
                    try {
                        for (Entity e : entities) {
                            if (e.getCustomName().equals(ChatColor.translateAlternateColorCodes('&', args[1]))) {
                                e.teleport(((Player) commandSender).getLocation());
                                commandSender.sendMessage("The entity \"" + e.getCustomName() + ChatColor.WHITE + "\"" + " was teleported to your location.");
                                return true;
                            }
                        }
                        if (args[1].equalsIgnoreCase("frank")) {
                            commandSender.sendMessage("Frank really doesn't want to be bothered right now.");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("herobrine")) {
                            commandSender.sendMessage(ChatColor.MAGIC + "skdjfnskd");
                            return true;
                        }
                        commandSender.sendMessage("The entity \"" + args[1] + "\" does not exist.");
                        return true;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        commandSender.sendMessage("Please type in a name of an entity.");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("gui") && commandSender.hasPermission("drugseller.gui")) {
                    String inventoryName = plugin.getConfig().getString("inventory name");
                    plugin.getServer().getScheduler().runTaskLater(plugin, new InventoryCreator((Player) commandSender, inventoryName), 1);
                    return true;
                }
                if (args[0].equalsIgnoreCase("entitylist") && commandSender.hasPermission("drugseller.entitylist")) {
                    EntityType[] entities = EntityType.values();
                    String entityNames = "";
                    for (EntityType e : entities) {
                        entityNames += e.name() + ", ";
                    }
                    commandSender.sendMessage(entityNames);
                    return true;
                }
            }
        }
        return false;
    }
}
