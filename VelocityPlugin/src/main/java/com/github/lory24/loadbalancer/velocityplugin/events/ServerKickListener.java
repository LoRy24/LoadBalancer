package com.github.lory24.loadbalancer.velocityplugin.events;

import com.github.lory24.loadbalancer.velocityplugin.LoadBalancerVelocity;
import com.github.lory24.loadbalancer.velocityplugin.impl.ServerUtils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class ServerKickListener {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Subscribe
    public void onPlayerKickFromServer(@NotNull KickedFromServerEvent event) {
        // If the player disconnects
        if (!event.getPlayer().isActive() || event.kickedDuringServerConnect()) return;

        // Get the server
        RegisteredServer registeredServer = ServerUtils.getServerWhereToConnectThePlayer();
        if (registeredServer == null) {
            event.getPlayer().disconnect(LoadBalancerVelocity.INSTANCE.getConfigValues().getAllLobbiesOfflineMessage());
            return;
        }

        event.setResult(KickedFromServerEvent.RedirectPlayer.create(registeredServer));

        // Create the message
        String messageString = LegacyComponentSerializer.legacyAmpersand().serialize(LoadBalancerVelocity.INSTANCE.getConfigValues().getKickFromServerMessage());
        Component message = LegacyComponentSerializer.legacyAmpersand().deserialize(messageString.replace("%disconnected_from%", event.getServer().getServerInfo().getName())
                .replace("%reason%", LegacyComponentSerializer.legacyAmpersand().serialize(event.getServerKickReason().get())));

        // Send the message
        LoadBalancerVelocity.INSTANCE.getProxyServer().getScheduler().buildTask(LoadBalancerVelocity.INSTANCE.getPluginEntry(),
                () -> event.getPlayer().sendMessage(message))
                .delay(Duration.ofSeconds(2))
                .schedule();
    }
}
