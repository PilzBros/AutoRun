package com.pilzbros.autorun.listener;

import com.pilzbros.autorun.Autorun;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Autorun.commandManager.playerLogin(evt.getPlayer());
    }
}
