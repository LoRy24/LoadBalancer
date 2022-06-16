package com.github.lory24.lobbybalancer.spigot;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public enum LobbyBalancerSpigot {

    /**
     * Used to access to the instance of the plugin
     */
    INSTANCE;

    // Plugin's reference
    @Getter
    private JavaPlugin plugin;

    @Getter
    private Logger logger;

    public void enable(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.plugin.saveDefaultConfig();

        // Start the server thread
        Thread t = new Thread(new InfosServer());
        t.setName("LobbyBalancer-Server");
        t.start();

        this.getLogger().info("Plugin enabled at version " + plugin.getDescription().getVersion() + "!");
    }

    public void disable() {
        this.getLogger().info("Plugin disabled!");
    }
}
