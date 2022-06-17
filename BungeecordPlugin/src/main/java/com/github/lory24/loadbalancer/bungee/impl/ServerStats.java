package com.github.lory24.loadbalancer.bungee.impl;

import lombok.Getter;

public class ServerStats {

    /**
     * Copyed from the SpigotPlugin module. Used as json object
     */
    @SuppressWarnings("unused")
    public static class ServerInfos {
        @Getter private int onlinePlayers;
        @Getter private int maxPlayers;
        @Getter private double tps;
        @Getter private int ramUsageInBytes;
    }

    // Stats infos
    @Getter private final String serverHost;
    @Getter private final int serverPort;
    @Getter private final String serverName; // Lobby, ecc.

    public ServerStats(String serverHost, int serverPort, String serverName) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.serverName = serverName;
    }
}
