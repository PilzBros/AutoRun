package com.pilzbros.autorun.component;

import com.pilzbros.autorun.Autorun;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ACommand {
    private APlayer aplayer;
    private String byUUID;
    private String command;
    private String level;
    private int times;

    public ACommand(APlayer player, String b, String c, String l, int t) {
        this.aplayer = player;
        this.byUUID = b;
        this.command = c;
        this.level = l;
        this.times = t;
    }

    public void execute() {
        if (this.times > 0) {
            UUID pID;
            Player player;
            if (this.level.equalsIgnoreCase("OP")) {
                pID = UUID.fromString(this.byUUID);
                player = Bukkit.getServer().getPlayer(pID);
                Bukkit.getServer().dispatchCommand(player, this.command);
            } else if (this.level.equalsIgnoreCase("Console")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.command);
            } else {
                pID = UUID.fromString(this.aplayer.getUUID());
                player = Bukkit.getServer().getPlayer(pID);
                Bukkit.getServer().dispatchCommand(player, this.command);
            }

            this.subtract();
        } else {
            this.remove();
        }

    }

    public void execute(Player player) {
        if (this.times > 0) {
            if (this.level.equalsIgnoreCase("OP")) {
                Bukkit.getServer().dispatchCommand(player, this.command);
            } else if (this.level.equalsIgnoreCase("Console")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.command);
            } else {
                Bukkit.getServer().dispatchCommand(player, this.command);
            }

            this.subtract();
        } else {
            this.remove();
        }

    }

    private void subtract() {
        if (!this.aplayer.getUUID().equalsIgnoreCase("666")) {
            --this.times;
            if (this.times <= 0) {
                this.remove();
            } else {
                Autorun.IO.updateCommand(this);
            }
        }

    }

    private void remove() {
        this.aplayer.removeCommand(this);
        Autorun.IO.removeCommand(this);
    }

    public APlayer getAPlayer() {
        return this.aplayer;
    }

    public String getCommand() {
        return this.command;
    }

    public String getByUUID() {
        return this.byUUID;
    }

    public String getLevel() {
        return this.level;
    }

    public int getTimes() {
        return this.times;
    }
}
