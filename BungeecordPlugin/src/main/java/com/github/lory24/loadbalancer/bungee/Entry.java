package com.github.lory24.loadbalancer.bungee;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;

@SuppressWarnings("unused")
public final class Entry extends Plugin {

    @Override
    public void onEnable() {
        try {
            LoadBalancerBungee.INSTANCE.enable(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        LoadBalancerBungee.INSTANCE.disable();
    }
}
