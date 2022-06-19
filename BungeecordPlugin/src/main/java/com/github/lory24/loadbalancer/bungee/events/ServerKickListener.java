package com.github.lory24.loadbalancer.bungee.events;

import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;
import com.github.lory24.loadbalancer.bungee.impl.LobbiesUtils;
import com.github.lory24.loadbalancer.bungee.impl.ServerStats;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerKickListener implements Listener {

    @EventHandler
    public void onServerKick(@NotNull ServerKickEvent event) {
        // Check if the server is online
        if (LobbiesUtils.isLobbyReachable(event.getKickedFrom().getSocketAddress())) return;

        // Referencee
        List<ServerStats> bestLobby =
                LoadBalancerBungee.INSTANCE.getPriorityManager().bestLobby;

        // If there isn't any ready fallback lobby, kick the player
        if (bestLobby.size() == 0) {
            event.getPlayer().disconnect(new TextComponent(LoadBalancerBungee.INSTANCE.getConfigValues().getAllLobbiesOfflineMessage()));
            return;
        }

        // Connect the player on a fallback server
        ProxyServer.getInstance().getScheduler().schedule(LoadBalancerBungee.INSTANCE.getPlugin(), () -> {
            ServerInfo serverInfo = null;
            for (ServerStats stats : bestLobby) {
                if (event.getKickedFrom().getName().equals(stats.getServerName()) || !LobbiesUtils.isLobbyReachable(ProxyServer.getInstance()
                        .getServerInfo(stats.getServerName()).getSocketAddress()) || ProxyServer.getInstance().getServerInfo(stats.getServerName()).getPlayers().size() == stats
                        .getServerInfos().getMaxPlayers()) continue;
                serverInfo = ProxyServer.getInstance().getServerInfo(stats.getServerName());
                break;
            }

            if (serverInfo == null) return;
            event.getPlayer().connect(serverInfo);
        }, 1000, TimeUnit.MILLISECONDS);
    }
}
