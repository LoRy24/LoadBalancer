package com.github.lory24.loadbalancer.velocityplugin.events;

import com.github.lory24.loadbalancer.velocityplugin.LoadBalancerVelocity;
import com.github.lory24.loadbalancer.velocityplugin.impl.ServerUtils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class ServerConnectListener {

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        if (!LoadBalancerVelocity.INSTANCE.getConfigValues().isConnectToLobbyOnJoin()) return;

        // Get the server
        RegisteredServer registeredServer = ServerUtils.getServerWhereToConnectThePlayer();
        if (registeredServer == null) {
            event.getPlayer().disconnect(LoadBalancerVelocity.INSTANCE.getConfigValues().getAllLobbiesOfflineMessage());
            return;
        }

        // Connect the player to the found server
        event.setInitialServer(registeredServer);
    }
}
