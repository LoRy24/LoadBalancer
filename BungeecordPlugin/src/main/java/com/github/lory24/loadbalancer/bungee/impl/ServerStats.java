package com.github.lory24.loadbalancer.bungee.impl;

import lombok.Getter;

public class ServerStats {

    // Stats infos
    @Getter private final String serverHost;
    @Getter private final int serverPort;
    @Getter private final String serverName; // Lobby, ecc.

    // The server infos object
    @Getter
    private ServerInfos serverInfos;

    public ServerStats(String serverHost, int serverPort, String serverName) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.serverName = serverName;
    }

    public ServerStats setServerInfos(ServerInfos serverInfos) {
        this.serverInfos = serverInfos;
        return this;
    }
}
