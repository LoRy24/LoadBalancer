package com.github.lory24.loadbalancer.bungee.events;

import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;
import com.github.lory24.loadbalancer.bungee.impl.ServerUtils;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class ServerDisconnectListener implements Listener {

    @EventHandler
    public void onServerDisconnect(@NotNull ServerDisconnectEvent event) {
        if (!ServerUtils.isServerReachable(event.getTarget().getSocketAddress())) LoadBalancerBungee.INSTANCE.getPriorityManager().connectToBestLobby(event.getPlayer());
    }
}
