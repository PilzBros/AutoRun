package com.pilzbros.autorun.component;

import com.pilzbros.autorun.Autorun;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class APlayer {
    private List<ACommand> commands;
    private String pUUID;

    public APlayer(String UUID) {
        this.pUUID = UUID;
        this.commands = new CopyOnWriteArrayList();
    }

    public void addCommand(ACommand command) {
        if (!this.commandExists(command.getCommand())) {
            this.commands.add(command);
        }

    }

    public ACommand getCommand(String command) {
        boolean found = false;
        Iterator var4 = this.commands.iterator();

        while(var4.hasNext()) {
            ACommand cmd = (ACommand)var4.next();
            if (cmd.getCommand().equalsIgnoreCase(command)) {
                found = true;
                return cmd;
            }
        }

        return null;
    }

    public void removeCommand(ACommand command) {
        this.commands.remove(command);
        if (this.commands.size() == 0) {
            Autorun.commandManager.removePlayer(this);
        }

    }

    public void removeCommand(String command) {
        Iterator var3 = this.commands.iterator();

        while(var3.hasNext()) {
            ACommand cmd = (ACommand)var3.next();
            if (cmd.getCommand().equalsIgnoreCase(command)) {
                this.commands.remove(cmd);
            }
        }

    }

    public String getUUID() {
        return this.pUUID;
    }

    public int getNumCommands() {
        return this.commands.size();
    }

    public List<ACommand> getCommandsList() {
        return this.commands;
    }

    public void printCommands(CommandSender requestingUser) {
        UUID pID = UUID.fromString(this.pUUID);
        Player player = Bukkit.getServer().getPlayer(pID);
        requestingUser.sendMessage(ChatColor.GRAY + "---------- " + ChatColor.GREEN + "Autorun Command List" + ChatColor.GRAY + " ----------");
        requestingUser.sendMessage("Player: " + ChatColor.GREEN + ChatColor.BOLD + player.getName());
        List<ACommand> cmds = this.commands;
        Iterator<ACommand> i = cmds.iterator();

        for(int count = 1; i.hasNext(); ++count) {
            ACommand c = (ACommand)i.next();
            UUID byID = UUID.fromString(c.getByUUID());
            Player byPlayer = Bukkit.getServer().getPlayer(pID);
            requestingUser.sendMessage(count + ".) " + c.getCommand() + ChatColor.YELLOW + " by " + byPlayer.getName() + ChatColor.WHITE + " on level " + ChatColor.RED + c.getLevel() + ChatColor.WHITE + " for " + ChatColor.GREEN + c.getTimes() + ChatColor.WHITE + " time(s)");
        }

    }

    public boolean commandExists(String command) {
        boolean found = false;
        Iterator var4 = this.commands.iterator();

        while(var4.hasNext()) {
            ACommand cmd = (ACommand)var4.next();
            if (cmd.getCommand().equalsIgnoreCase(command)) {
                found = true;
            }
        }

        return found;
    }

    public void executeCommands() {
        Iterator var2 = this.commands.iterator();

        while(var2.hasNext()) {
            ACommand cmd = (ACommand)var2.next();
            cmd.execute();
        }

    }

    public void executeCommands(Player player) {
        Iterator var3 = this.commands.iterator();

        while(var3.hasNext()) {
            ACommand cmd = (ACommand)var3.next();
            cmd.execute(player);
        }

    }
}