package com.github.lory24.loadbalancer.bungee;

import com.github.lory24.loadbalancer.bungee.commands.HubCommand;
import com.github.lory24.loadbalancer.bungee.events.PostLoginListener;
import com.google.common.io.Files;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.logging.Logger;

public enum LoadBalancerBungee {

    /**
     * Used to access to the instance of the plugin
     */
    INSTANCE;

    // Plugin's reference
    @Getter
    private Plugin plugin;

    @Getter
    private Logger logger;

    // Config file
    @Getter
    private File configFile;

    @Getter
    private InputStream configInputStream;

    @Getter
    private ConfigValues configValues;

    /**
     * This is the main feature of this plugin: The priority manager
     */
    @Getter
    private PriorityManager priorityManager;

    public void enable(@NotNull Plugin plugin) throws IOException {
        this.plugin = plugin;
        this.logger = plugin.getLogger();

        // Load the plugin's config
        this.loadConfig();
        this.configValues = new ConfigValues();
        this.configValues.loadConfig();

        // Instance the priorityManager
        this.priorityManager = new PriorityManager();

        // Register the commands
        ProxyServer.getInstance().getPluginManager()
                .registerCommand(this.getPlugin(), new HubCommand());

        // Register the events
        ProxyServer.getInstance().getPluginManager()
                .registerListener(this.getPlugin(), new PostLoginListener());

        getLogger().info("Plugin enabled at version " + this.getPlugin().getDescription().getVersion() + "!");
    }

    public void disable() {
        getLogger().info("Plugin disabled!");
    }

    /**
     * Private function used by this class to create the config file.
     * @throws IOException When there was an error during the creation of the config file
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored", "UnstableApiUsage"})
    private void loadConfig() throws IOException {
        configLoadingProcess: {
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir(); // Create the datafolder
            configFile = new File(plugin.getDataFolder(), "config.yml");
            if (configFile.exists()) break configLoadingProcess;
            configFile.createNewFile();
            byte[] bytes = new byte[plugin.getResourceAsStream("config.yml").available()];
            plugin.getResourceAsStream("config.yml").read(bytes);
            Files.write(bytes, configFile);
        }
        this.configInputStream = java.nio.file.Files.newInputStream(this.configFile.toPath());
    }
}
