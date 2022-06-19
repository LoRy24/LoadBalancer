package com.github.lory24.loadbalancer.bungee.events;

import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;
import com.github.lory24.loadbalancer.bungee.impl.ServerStats;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerConnectListener implements Listener {

    @EventHandler
    public void onServerConnect(@NotNull ServerConnectEvent event) {
        if (event.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {

            boolean connected = false;

            firstConnectionBlock: {
                // Best lobby reference
                List<ServerStats> bestLobby = LoadBalancerBungee.INSTANCE.getPriorityManager().bestLobby;
                if (bestLobby.size() == 0) break firstConnectionBlock;

                ServerInfo serverInfo = null;

                // If the server is full, try with another one
                for (int i = 0; i < bestLobby.size(); i++) {
                    serverInfo = ProxyServer.getInstance().getServerInfo(bestLobby.get(i).getServerName());
                    boolean maxed = serverInfo.getPlayers().size() == bestLobby.get(bestLobby.size() -1).getServerInfos().getMaxPlayers();
                    if (i == bestLobby.size() -1 && maxed) break firstConnectionBlock;
                    else if (maxed) continue;
                    break;

                }

                // Connect the player to the found server
                event.setTarget(serverInfo);
                connected = true;
            }

            if (connected) return;

            event.getPlayer().disconnect(new TextComponent(LoadBalancerBungee.INSTANCE
                    .getConfigValues().getAllLobbiesOfflineMessage()));
            event.setCancelled(true);
        }
    }
}
