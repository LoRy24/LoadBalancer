package com.github.lory24.loadbalancer.velocityplugin.impl;

import com.github.lory24.loadbalancer.velocityplugin.LoadBalancerVelocity;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
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
}
