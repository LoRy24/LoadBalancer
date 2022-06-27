package com.github.lory24.loadbalancer.velocityplugin.events;

import com.github.lory24.loadbalancer.velocityplugin.LoadBalancerVelocity;
import com.github.lory24.loadbalancer.velocityplugin.impl.ServerStats;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.server.ServerInfo;

import java.util.List;

public class ServerConnectListener {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        if (!LoadBalancerVelocity.INSTANCE.getConfigValues().isConnectToLobbyOnJoin()) return;

        firstConnectionBlock: {
            // Best lobby reference
            List<ServerStats> bestLobby = LoadBalancerVelocity.INSTANCE.getPriorityManager().bestLobby;
            if (bestLobby.size() == 0) break firstConnectionBlock;

            // If the server is full, try with another one
            ServerInfo serverInfo = null;
            for (int i = 0; i < bestLobby.size(); i++) {
                serverInfo = LoadBalancerVelocity.INSTANCE.getProxyServer().getServer(bestLobby.get(i).getServerName()).get().getServerInfo();
                boolean maxed = LoadBalancerVelocity.INSTANCE.getProxyServer().getServer(bestLobby.get(i).getServerName()).get().getPlayersConnected().size() == bestLobby.get(i).getServerInfos().getMaxPlayers();
                if (i == bestLobby.size() -1 && maxed) break firstConnectionBlock;
                else if (maxed) continue;
                break;

            }

            // Connect the player to the found server
            event.setInitialServer(LoadBalancerVelocity.INSTANCE.getProxyServer().getServer(serverInfo.getName()).get());
            return;
        }
        event.getPlayer().disconnect(LoadBalancerVelocity.INSTANCE.getConfigValues()
                .getAllLobbiesOfflineMessage());
    }
}
