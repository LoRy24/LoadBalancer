package com.github.lory24.loadbalancer.velocityplugin.impl;

import com.github.lory24.loadbalancer.velocityplugin.LoadBalancerVelocity;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class ServerUtils {

    public static boolean isLobby(String serverName) {
        return LoadBalancerVelocity.INSTANCE.getPriorityManager().bestLobby.stream().map(ServerStats::getServerName)
                .collect(Collectors.toList()).contains(serverName);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isServerReachable(SocketAddress socketAddress) {
        // Check if the server is online
        try {
            Socket socket = new Socket();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(120);
            socket.connect(socketAddress);
            // If the connection success, then the kick was intentionally executed by the server, so the event can stop here
            socket.close();
            return true;
        } catch (IOException ignored) { // Ignore the exception and teleport the player on another server
            return false;
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static @Nullable RegisteredServer getServerWhereToConnectThePlayer() {

        // Best lobby reference
        List<ServerStats> bestLobby = LoadBalancerVelocity.INSTANCE.getPriorityManager().bestLobby;
        if (bestLobby.size() == 0) return null;

        // If the server is full, try with another one
        ServerInfo serverInfo = null;
        for (int i = 0; i < bestLobby.size(); i++) {
            serverInfo = LoadBalancerVelocity.INSTANCE.getProxyServer().getServer(bestLobby.get(i).getServerName()).get().getServerInfo();
            boolean maxed = LoadBalancerVelocity.INSTANCE.getProxyServer().getServer(bestLobby.get(i).getServerName()).get().getPlayersConnected().size() == bestLobby.get(i).getServerInfos().getMaxPlayers();
            if (i == bestLobby.size() -1 && maxed) return null;
            else if (maxed || !isServerReachable(serverInfo.getAddress())) continue;
            break;

        }

        return LoadBalancerVelocity.INSTANCE.getProxyServer().getServer(serverInfo.getName()).get();
    }
}
