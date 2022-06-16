package com.github.lory24.lobbybalancer.spigot;

import com.github.lory24.lobbybalancer.spigot.ConfigValues;
import com.github.lory24.lobbybalancer.spigot.utils.ServerInfos;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class InfosServer implements Runnable {

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress((String) ConfigValues.SERVER_HOST.get(), (int) ConfigValues.SERVER_PORT.get()));
            this.listening(); // Start the listening state of the socket
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void listening() throws IOException {
        while (!Thread.interrupted()) {
            Socket socket = new Socket();
            new Thread(() -> processConnection(socket)).start();
            socket.close();
        }
    }

    private void processConnection(@NotNull Socket socket) {
        try {
            String serverInfosJson = new Gson().toJson(new ServerInfos().initialize());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(serverInfosJson);
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
