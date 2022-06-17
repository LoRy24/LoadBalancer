package com.github.lory24.loadbalancer.spigot;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class Entry extends JavaPlugin {

    @Override
    public void onEnable() {
        LoadBalancerSpigot.INSTANCE.enable(this);
    }

    @Override
    public void onDisable() {
        LoadBalancerSpigot.INSTANCE.disable();
    }
}
