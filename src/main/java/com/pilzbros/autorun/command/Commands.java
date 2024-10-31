package com.pilzbros.autorun.command;

import com.pilzbros.autorun.Autorun;
import com.pilzbros.autorun.component.ACommand;
import com.pilzbros.autorun.component.APlayer;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("autorun.admin") && !sender.hasPermission("autorun.*")) {
            sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + "You do not have permissions to access Autorun");
        } else if (args.length < 1) {
            sender.sendMessage(Autorun.pluginPrefix + ChatColor.WHITE + "Autorun v" + "1.4");
        } else {
            APlayer aplayer;
            String tmpUUID;
            APlayer aplayer;
            if (args.length == 1) {
                if (!args[0].equalsIgnoreCase("?") && !args[0].equalsIgnoreCase("help")) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        Autorun.IO.prepareDB();
                        Autorun.IO.LoadSettings();
                        Autorun.IO.loadCommands();
                        Autorun.IO.loadAllUserCommands();
                        sender.sendMessage(Autorun.pluginPrefix + ChatColor.GREEN + "Autorun has been reloaded!");
                    } else if (args[0].equalsIgnoreCase("purge")) {
                        sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + ChatColor.BOLD + "WARNING: " + ChatColor.WHITE + " Purging Autorun data will remove all commands from database. There is no way to undo this!");
                        sender.sendMessage("");
                        sender.sendMessage("To confirm purge: " + ChatColor.RED + ChatColor.BOLD + " /autorun purge Y");
                    } else if (args[0].equalsIgnoreCase("update")) {
                        if (!Autorun.updateNeeded) {
                            sender.sendMessage(Autorun.pluginPrefix + ChatColor.WHITE + "Autorun is up to date :)");
                        } else {
                            sender.sendMessage(Autorun.pluginPrefix + ChatColor.GREEN + "An update for Autorun is available!" + ChatColor.WHITE + " You can download it at: " + ChatColor.YELLOW + "http://dev.bukkit.org/bukkit-plugins/autorun/");
                        }
                    } else {
                        Iterator it;
                        if (args[0].equalsIgnoreCase("commands")) {
                            if (Autorun.commandManager.getNumPlayers() > 0) {
                                sender.sendMessage(ChatColor.RED + "----------" + ChatColor.GREEN + " Autorun All Player Commands " + ChatColor.RED + "----------");
                                it = Autorun.commandManager.getPlayers().entrySet().iterator();

                                while(true) {
                                    Entry entry;
                                    String tmpUUID;
                                    do {
                                        if (!it.hasNext()) {
                                            return true;
                                        }

                                        entry = (Entry)it.next();
                                        tmpUUID = (String)entry.getKey();
                                    } while(!tmpUUID.equalsIgnoreCase("666"));

                                    aplayer = (APlayer)entry.getValue();
                                    int count = 1;
                                    Iterator var11 = aplayer.getCommandsList().iterator();

                                    while(var11.hasNext()) {
                                        ACommand tmpcmd = (ACommand)var11.next();
                                        UUID pUUID = UUID.fromString(tmpcmd.getByUUID());
                                        OfflinePlayer bplayer = Bukkit.getOfflinePlayer(pUUID);
                                        sender.sendMessage(count + ".) " + ChatColor.YELLOW + tmpcmd.getCommand() + ChatColor.WHITE + " to be executed on level " + ChatColor.AQUA + tmpcmd.getLevel() + ChatColor.WHITE + " | submitted by " + ChatColor.GOLD + bplayer.getName());
                                    }
                                }
                            } else {
                                sender.sendMessage(Autorun.pluginPrefix + ChatColor.WHITE + "There are no all-player commands to display");
                            }
                        } else if (args[0].equalsIgnoreCase("players")) {
                            if (Autorun.commandManager.getNumPlayers() > 0) {
                                sender.sendMessage(ChatColor.RED + "----------" + ChatColor.GREEN + " Autorun Players " + ChatColor.RED + "----------");
                                it = Autorun.commandManager.getPlayers().entrySet().iterator();
                                byte count = 1;

                                while(it.hasNext()) {
                                    Entry entry = (Entry)it.next();
                                    tmpUUID = (String)entry.getKey();
                                    if (!tmpUUID.equalsIgnoreCase("666")) {
                                        UUID pUUID = UUID.fromString(tmpUUID);
                                        aplayer = (APlayer)entry.getValue();
                                        OfflinePlayer bplayer = Bukkit.getOfflinePlayer(pUUID);
                                        sender.sendMessage(count + ".) " + bplayer.getName() + " has " + ChatColor.GREEN + aplayer.getNumCommands() + ChatColor.WHITE + " commands");
                                    }
                                }
                            } else {
                                sender.sendMessage(Autorun.pluginPrefix + ChatColor.WHITE + "There are no players to display");
                            }
                        } else {
                            sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + "Unknown Autorun command or incorrect syntax");
                        }
                    }
                }
            } else {
                String command;
                APlayer allPlayer;
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("purge")) {
                        if (args[1].equalsIgnoreCase("y")) {
                            Autorun.commandManager.purge();
                            Autorun.IO.purgeData();
                            Autorun.IO.prepareDB();
                            Autorun.IO.LoadSettings();
                            Autorun.IO.loadCommands();
                            Autorun.IO.loadAllUserCommands();
                            sender.sendMessage(Autorun.pluginPrefix + ChatColor.GREEN + "All Autorun data has been erased from the database!");
                        } else {
                            sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + "Incorrect Syntax! To purge data, " + ChatColor.YELLOW + "/autorun purge y");
                        }
                    } else if (args[0].equalsIgnoreCase("player")) {
                        command = args[1];
                        OfflinePlayer player = Bukkit.getOfflinePlayer(command);
                        if (Autorun.commandManager.playerExists(player.getUniqueId().toString())) {
                            allPlayer = Autorun.commandManager.getPlayer(player.getUniqueId().toString());
                            allPlayer.printCommands(sender);
                        } else {
                            sender.sendMessage(Autorun.pluginPrefix + ChatColor.WHITE + command + " has no Autorun commands");
                        }
                    } else {
                        sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + "Unknown Autorun command or incorrect syntax");
                    }
                } else if (args.length >= 3) {
                    String command;
                    int times;
                    if (args[0].equalsIgnoreCase("add")) {
                        if (args[1].equalsIgnoreCase("all")) {
                            command = args[2];
                            command = args[3];

                            for(times = 4; times < args.length; ++times) {
                                command = command + " " + args[times];
                            }

                            allPlayer = Autorun.commandManager.getPlayer("666");
                            if (!allPlayer.commandExists(command)) {
                                Autorun.commandManager.addCommand("666", Bukkit.getPlayer(sender.getName()).getUniqueId().toString(), command, command, 666);
                                Autorun.IO.saveCommand(new ACommand(allPlayer, Bukkit.getPlayer(sender.getName()).getUniqueId().toString(), command, command, 666));
                                sender.sendMessage(Autorun.pluginPrefix + ChatColor.WHITE + "Command (" + ChatColor.YELLOW + command + ChatColor.WHITE + ") " + ChatColor.GREEN + " successfully added for " + ChatColor.AQUA + " all users");
                            } else {
                                sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + "Command already exists for all-users");
                            }
                        } else {
                            command = args[1];
                            command = args[2];
                            times = Integer.parseInt(args[3]);
                            tmpUUID = args[4];

                            for(int i = 5; i < args.length; ++i) {
                                tmpUUID = tmpUUID + " " + args[i];
                            }

                            String uids;
                            if (Bukkit.getOfflinePlayer(command).isOnline()) {
                                uids = Bukkit.getOfflinePlayer(command).getUniqueId().toString();
                            } else {
                                Player player = Bukkit.getPlayer(command);
                                uids = player.getUniqueId().toString();
                            }

                            Autorun.commandManager.addCommand(uids, Bukkit.getPlayer(sender.getName()).getUniqueId().toString(), tmpUUID, command, times);
                            aplayer = Autorun.commandManager.getPlayer(Bukkit.getPlayer(command));
                            Autorun.IO.saveCommand(new ACommand(aplayer, Bukkit.getPlayer(sender.getName()).getUniqueId().toString(), tmpUUID, command, times));
                            sender.sendMessage(Autorun.pluginPrefix + ChatColor.WHITE + "Command (" + ChatColor.YELLOW + tmpUUID + ChatColor.WHITE + ") " + ChatColor.GREEN + " successfully added for " + ChatColor.AQUA + command);
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (args[1].equalsIgnoreCase("all")) {
                            command = args[2];

                            for(int i = 3; i < args.length; ++i) {
                                command = command + " " + args[i];
                            }

                            APlayer allPlayer = Autorun.commandManager.getPlayer("666");
                            if (allPlayer.commandExists(command)) {
                                ACommand comm = allPlayer.getCommand(command);
                                allPlayer.removeCommand(comm);
                                Autorun.IO.removeCommand(comm);
                                sender.sendMessage(Autorun.pluginPrefix + ChatColor.WHITE + "Command (" + ChatColor.YELLOW + command + ChatColor.WHITE + ") " + ChatColor.GREEN + " successfully removed for " + ChatColor.AQUA + " all users");
                            } else {
                                sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + "Command (" + command + ") does not exist for all users!");
                            }
                        } else {
                            command = args[1];
                            command = args[2];

                            for(times = 3; times < args.length; ++times) {
                                command = command + " " + args[times];
                            }

                            Player player = Bukkit.getPlayer(command);
                            aplayer = Autorun.commandManager.getPlayer(Bukkit.getPlayer(command));
                            if (aplayer.commandExists(command)) {
                                ACommand comm = aplayer.getCommand(command);
                                aplayer.removeCommand(comm);
                                Autorun.IO.removeCommand(comm);
                                sender.sendMessage(Autorun.pluginPrefix + ChatColor.WHITE + "Command (" + ChatColor.YELLOW + command + ChatColor.WHITE + ") " + ChatColor.GREEN + " successfully removed for " + ChatColor.AQUA + command);
                            } else {
                                sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + "Command (" + command + ") does not exist for " + ChatColor.AQUA + command);
                            }
                        }
                    } else {
                        sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + "Unknown Autorun command or incorrect syntax");
                    }
                } else {
                    sender.sendMessage(Autorun.pluginPrefix + ChatColor.RED + "Unknown Autorun command or incorrect syntax");
                }
            }
        }

        return true;
    }
}
