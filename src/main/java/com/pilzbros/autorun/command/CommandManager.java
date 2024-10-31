package com.pilzbros.autorun.command;

import com.pilzbros.autorun.component.ACommand;
import com.pilzbros.autorun.component.APlayer;
import java.util.HashMap;
import org.bukkit.entity.Player;

public class CommandManager {
    private HashMap<String, APlayer> players = new HashMap();

    public CommandManager() {
        this.addPlayer("666");
    }

    private void addPlayer(String UUID) {
        this.players.put(UUID, new APlayer(UUID));
    }

    public void purge() {
        this.players.clear();
    }

    public void removePlayer(APlayer player) {
        this.players.remove(player.getUUID().toString());
    }

    public void addCommand(String UUID, String By, String Command, String Level, int Times) {
        ACommand command;
        if (this.playerExists(UUID)) {
            command = new ACommand(this.getPlayer(UUID), By, Command, Level, Times);
            this.getPlayer(UUID).addCommand(command);
        } else {
            this.addPlayer(UUID);
            command = new ACommand(this.getPlayer(UUID), By, Command, Level, Times);
            this.getPlayer(UUID).addCommand(command);
        }

    }

    public APlayer getPlayer(String UUID) {
        return this.playerExists(UUID) ? (APlayer)this.players.get(UUID) : null;
    }

    public APlayer getPlayer(Player player) {
        return this.playerExists(player.getUniqueId().toString()) ? (APlayer)this.players.get(player.getUniqueId().toString()) : null;
    }

    public int getNumPlayers() {
        return this.players.size();
    }

    public HashMap<String, APlayer> getPlayers() {
        return this.players;
    }

    public boolean playerExists(String UUID) {
        return this.players.containsKey(UUID);
    }

    public void playerLogin(Player player) {
        this.getPlayer("666").executeCommands(player);
        if (this.playerExists(player.getUniqueId().toString())) {
            this.getPlayer(player.getUniqueId().toString()).executeCommands();
        }

    }
}
