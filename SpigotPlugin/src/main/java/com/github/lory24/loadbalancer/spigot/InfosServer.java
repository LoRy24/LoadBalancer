package com.github.lory24.loadbalancer.spigot;

import com.github.lory24.loadbalancer.spigot.utils.ServerInfos;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class InfosServer implements Runnable {

    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress((String) ConfigValues.SERVER_HOST.get(), (int) ConfigValues.SERVER_PORT.get()));
            this.listening(); // Start the listening state of the socket
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void listening() throws IOException {
        while (!Thread.interrupted()) {
            Socket socket = this.serverSocket.accept();
            new Thread(() -> {
                try {
                    String serverInfosJson = new Gson().toJson(new ServerInfos().initialize());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(serverInfosJson);
                    dataOutputStream.close();
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
