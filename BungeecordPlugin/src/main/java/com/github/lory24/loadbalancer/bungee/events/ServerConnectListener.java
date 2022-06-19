package com.github.lory24.loadbalancer.bungee.events;

import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class ServerConnectListener implements Listener {

    @EventHandler
    public void onServerConnect(@NotNull ServerConnectEvent event) {
        if (event.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
            event.setTarget(ProxyServer.getInstance().getServerInfo(LoadBalancerBungee.INSTANCE.getPriorityManager().getBestLobby().get(0).getServerName()));
        }
    }
}
