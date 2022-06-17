package com.github.lory24.loadbalancer.spigot.utils;

import com.github.lory24.loadbalancer.api.ServerInfos;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ServerInfosInitializer {

    @Contract(" -> new")
    public static @NotNull ServerInfos initialize() {
        return new ServerInfos(Bukkit.getServer().getOnlinePlayers().size(), Bukkit.getServer().getMaxPlayers(), TpsUtil.getCurrentTPS(TpsUtil.DEFAULT_FORMAT),
                (int)(Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()));
    }
}
