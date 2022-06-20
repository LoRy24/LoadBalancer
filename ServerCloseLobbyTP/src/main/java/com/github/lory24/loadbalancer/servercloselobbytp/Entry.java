package com.github.lory24.loadbalancer.servercloselobbytp;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class Entry extends JavaPlugin {

    @SuppressWarnings({"LoopConditionNotUpdatedInsideLoop", "StatementWithEmptyBody", "UnstableApiUsage"})
    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "LoadBalancer");
        getLogger().info("Plugin enabled! Remember that if you disable this plugin all the players will be kicked from the server!");

        // Kick all the players when the plugin isn't enabled
        new Thread(() -> {
            while (this.isEnabled());
            for (Player player: Bukkit.getOnlinePlayers()) {
                Bukkit.getLogger().info("A");
                ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
                byteArrayDataOutput.writeUTF("ConnectOnServerClose");
                player.sendPluginMessage(this, "LoadBalancer", byteArrayDataOutput.toByteArray());
            }
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
    }
}
