package com.github.lory24.loadbalancer.spigot.utils;

import lombok.Getter;

/**
 * The {@link ServerInfos} class can be used to parse the JSON response obtained when connecting to the InfosServer.
 * This class is used by the bungee plugin to parse the result and by the spigot plugin to create it.
 */
public class ServerInfos {
    @Getter private final int onlinePlayers;
    @Getter private final int maxPlayers;
    @Getter private final double tps;
    @Getter private final int ramUsageInBytes;

    /**
     * The constructor for the ServerInfos class.
     * @param onlinePlayers How many players are currently online in the server
     * @param maxPlayers The maximum amount of players that can be connected in the server
     * @param tps The ticks per second value
     * @param ramUsageInBytes The ram usage of the spigot server.
     */
    public ServerInfos(int onlinePlayers, int maxPlayers, double tps, int ramUsageInBytes) {
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.tps = tps;
        this.ramUsageInBytes = ramUsageInBytes;
    }
}
