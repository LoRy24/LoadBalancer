package com.github.lory24.loadbalancer.velocityplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

@Plugin(id = "loadbalancer_velocity_plugin", name = "VelocityPlugin", version = "1.0-SNAPSHOT", description = "Loadbalancer plugin for Velocity",
        authors = {"LoRy24TV"})
public class Entry {

    @Inject @Getter
    private Logger logger;

    @Inject @Getter
    private ProxyServer proxyServer;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            LoadBalancerVelocity.INSTANCE.enable(this);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        LoadBalancerVelocity.INSTANCE.disable();
    }
}
