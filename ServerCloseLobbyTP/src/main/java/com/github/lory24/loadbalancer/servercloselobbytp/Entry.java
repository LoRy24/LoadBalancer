package com.github.lory24.loadbalancer.servercloselobbytp;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Entry extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "LoadBalancer");
        getLogger().info("Plugin enabled! Remember that if you disable this plugin all the players will be kicked from the server!");
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onDisable() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
            byteArrayDataOutput.writeUTF("ConnectOnServerClose");
            player.sendPluginMessage(this, "LoadBalancer", byteArrayDataOutput.toByteArray());
        }
        getLogger().info("Plugin disabled!");
    }
}
