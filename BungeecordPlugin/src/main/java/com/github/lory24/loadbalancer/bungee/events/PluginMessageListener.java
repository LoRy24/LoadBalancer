package com.github.lory24.loadbalancer.bungee.events;

import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class PluginMessageListener implements Listener {

    @SuppressWarnings("UnstableApiUsage")
    @EventHandler
    public void onPluginMessage(@NotNull PluginMessageEvent event) {
        // Check if the channel is the LoadBalancer's one
        if (!event.getTag().equals("LoadBalancer")) return;

        // Now it's time to check if the subchannel is 'ConnectOnServerClose'
        ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(event.getData());

        // Process the action
        if (byteArrayDataInput.readUTF().equals("ConnectOnServerClose"))
            LoadBalancerBungee.INSTANCE.getPriorityManager().connectToBestLobby((ProxiedPlayer) event.getSender());
    }
}