package com.github.lory24.loadbalancer.spigot.utils;

import lombok.Getter;
import org.bukkit.Bukkit;

public class ServerInfos {
    @Getter private int onlinePlayers;
    @Getter private int maxPlayers;
    @Getter private double tps;
    @Getter private int ramUsageInBytes;

    public ServerInfos initialize() {
        this.onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        this.maxPlayers = Bukkit.getServer().getMaxPlayers();
        this.tps = TpsUtil.getCurrentTPS(TpsUtil.DEFAULT_FORMAT);
        this.ramUsageInBytes = (int)(Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory());
        return this;
    }
}
