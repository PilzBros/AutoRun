package com.pilzbros.autorun;

import com.pilzbros.autorun.command.Commands;
import com.pilzbros.autorun.io.InputOutput;
import com.pilzbros.autorun.io.Setting;
import com.pilzbros.autorun.io.Settings;
import com.pilzbros.autorun.io.Update;
import com.pilzbros.autorun.command.CommandManager;
import com.pilzbros.autorun.listener.PlayerListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Autorun extends JavaPlugin implements Listener {
    public static final String pluginName = "Autorun";
    public static final String pluginVersion = "1.4";
    public static final String pluginPrefix;
    public static final String pluginURL = "http://dev.bukkit.org/bukkit-plugins/autorun/";
    public static Autorun instance;
    public static final Logger log;
    public static boolean updateNeeded;
    public static InputOutput IO;
    public static CommandManager commandManager;

    static {
        pluginPrefix = ChatColor.RED + "[Autorun] " + ChatColor.WHITE;
        log = Logger.getLogger("Minecraft");
    }

    public void onLoad() {
    }

    public void onEnable() {
        long startMili = System.currentTimeMillis() % 1000L;
        instance = this;
        commandManager = new CommandManager();
        IO = new InputOutput();
        IO.prepareDB();
        IO.LoadSettings();
        IO.loadCommands();
        IO.loadAllUserCommands();
        this.getCommand("autorun").setExecutor(new Commands());
        this.getCommand("ar").setExecutor(new Commands());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        if (Settings.getGlobalBoolean(Setting.CheckForUpdates)) {
            new Update(82061, "");
        }

        log.log(Level.INFO, "[Autorun] Bootup took " + (System.currentTimeMillis() % 1000L - startMili) + " ms!");
    }

    public void onReload() {
        InputOutput.freeConnection();
    }

    public void onDisable() {
        InputOutput.freeConnection();
    }
}