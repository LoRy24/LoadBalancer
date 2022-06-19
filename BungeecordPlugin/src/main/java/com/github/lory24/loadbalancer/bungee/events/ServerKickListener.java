package com.github.lory24.loadbalancer.bungee.events;

import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;
import com.github.lory24.loadbalancer.bungee.impl.LobbiesUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerKickListener implements Listener {

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        // Check if the server is online
        if (LobbiesUtils.isLobbyReachable(event.getKickedFrom().getSocketAddress())) return;

        // If there isn't any ready fallback lobby, kick the player
        if (LoadBalancerBungee.INSTANCE.getPriorityManager().bestLobby.size() == 0) {
            event.getPlayer().disconnect(new TextComponent(LoadBalancerBungee.INSTANCE.getConfigValues().getAllLobbiesOfflineMessage()));
            return;
        }

        // Connect the player on a fallback server
        LoadBalancerBungee.INSTANCE.getPriorityManager().connectToBestLobby(event.getPlayer());
    }
}
