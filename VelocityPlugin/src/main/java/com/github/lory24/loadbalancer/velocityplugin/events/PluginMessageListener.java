package com.github.lory24.loadbalancer.velocityplugin.events;

import com.github.lory24.loadbalancer.velocityplugin.LoadBalancerVelocity;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

public class PluginMessageListener {

    @SuppressWarnings("UnstableApiUsage")
    public void onPluginMessage(@NotNull PluginMessageEvent event) {
        // Check if the channel is the LoadBalancer's one
        if (!event.getIdentifier().getId().equals("LoadBalancer")) return;

        // Now it's time to check if the subchannel is 'ConnectOnServerClose'
        ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(event.getData());

        // Process the action
        if (!byteArrayDataInput.readUTF().equals("ConnectToServer")) return;

        LoadBalancerVelocity.INSTANCE.getPriorityManager().connectToBestLobby((Player) event.getSource());
    }
}
