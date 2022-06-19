package com.github.lory24.loadbalancer.bungee.impl;

import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;

import java.util.stream.Collectors;

public class LobbiesUtils {

    public static boolean isLobby(String serverName) {
        return LoadBalancerBungee.INSTANCE.getPriorityManager().bestLobby.stream().map(ServerStats::getServerName)
                .collect(Collectors.toList()).contains(serverName);
    }
}
