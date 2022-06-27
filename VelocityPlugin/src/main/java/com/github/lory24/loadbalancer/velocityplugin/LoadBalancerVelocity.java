package com.github.lory24.loadbalancer.velocityplugin;

import com.github.lory24.loadbalancer.velocityplugin.commands.HubCommand;
import com.github.lory24.loadbalancer.velocityplugin.events.PluginMessageListener;
import com.github.lory24.loadbalancer.velocityplugin.events.ServerConnectListener;
import com.google.common.io.Files;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

import static java.nio.file.Files.newInputStream;

public enum LoadBalancerVelocity {

    INSTANCE;

    @Getter
    private Entry pluginEntry;

    @Getter
    private Logger logger;

    @Getter
    private ProxyServer proxyServer;

    // Config file
    @Getter
    private File dataFolder, configFile;

    @Getter
    private InputStream configInputStream;

    @Getter
    private ConfigValues configValues;

    @Getter
    private PriorityManager priorityManager;

    public void enable(@NotNull Entry entry) throws URISyntaxException, IOException {
        this.pluginEntry = entry;
        this.logger = pluginEntry.getLogger();
        this.proxyServer = pluginEntry.getProxyServer();

        // Load the plugin's config
        this.dataFolder = new File("./plugins/LoadBalancer_Velocity");
        this.configFile = new File(this.getDataFolder(), "config.yml");
        this.loadConfig();
        this.configValues = new ConfigValues();
        this.configValues.loadConfig();

        // Register the channel
        this.getProxyServer().getChannelRegistrar().register(new LegacyChannelIdentifier("LoadBalancer"));

        // Instance the priorityManager
        this.priorityManager = new PriorityManager();
        this.priorityManager.run();

        // Register the commands
        this.proxyServer.getCommandManager().register("Hub", new HubCommand(),
                "Lobby");

        // Register the events
        registerEvents();

        getLogger().info("Plugin enabled!");
    }

    public void disable() {
        getLogger().info("Plugin disabled!");
    }

    private void registerEvents() {
        Arrays.stream(new Object[]{new ServerConnectListener(), new PluginMessageListener()}).forEach(o ->
                this.proxyServer.getEventManager().register(this.pluginEntry, o));
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "UnstableApiUsage"})
    private void loadConfig() throws IOException {
        loadConfig: {
            if (!this.getDataFolder().exists()) this.getDataFolder().mkdir(); // Create the data-folder
            if (this.configFile.exists()) break loadConfig;
            this.configFile.createNewFile();
            byte[] bytes = new byte[Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("config.yml")).available()];
            Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("config.yml")).read(bytes);
            Files.write(bytes, this.configFile);
        }
        this.configInputStream = newInputStream(this.configFile.toPath());
    }
}
