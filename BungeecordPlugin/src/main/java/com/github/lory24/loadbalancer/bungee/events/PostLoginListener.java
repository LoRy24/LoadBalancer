package com.github.lory24.loadbalancer.bungee.events;

import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(@NotNull PostLoginEvent event) {
        LoadBalancerBungee.INSTANCE.getPriorityManager().connectToBestLobby(event.getPlayer());
    }
}
