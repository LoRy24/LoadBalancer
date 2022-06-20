package com.github.lory24.loadbalancer.spigot;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public enum LoadBalancerSpigot {

    /**
     * Used to access to the instance of the plugin
     */
    INSTANCE;

    // Plugin's reference
    @Getter
    private JavaPlugin plugin;

    @Getter
    private Logger logger;

    @Getter
    private final InfosServer infosServer = new InfosServer();

    public void enable(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.plugin.saveDefaultConfig();

        // Start the server thread
        Thread socketThread = new Thread(infosServer);
        socketThread.setName("LobbyBalancer-Server");
        socketThread.start();

        this.getLogger().info("Plugin enabled at version " + plugin.getDescription().getVersion() + "!");
    }

    public void disable() {
        if (Bukkit.getPluginManager().isPluginEnabled(this.getPlugin())) Bukkit.getPluginManager().disablePlugin(this.getPlugin());
        this.infosServer.setEnabled(false);
        this.getLogger().info("Plugin disabled!");
    }
}
