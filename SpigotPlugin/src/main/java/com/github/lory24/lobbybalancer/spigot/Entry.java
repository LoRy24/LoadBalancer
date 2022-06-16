package com.github.lory24.lobbybalancer.spigot;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class Entry extends JavaPlugin {

    @Override
    public void onEnable() {
        LobbyBalancerSpigot.INSTANCE.enable(this);
    }

    @Override
    public void onDisable() {
        LobbyBalancerSpigot.INSTANCE.disable();
    }
}
