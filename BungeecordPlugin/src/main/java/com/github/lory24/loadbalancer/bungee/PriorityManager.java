package com.github.lory24.loadbalancer.bungee;

import com.github.lory24.loadbalancer.api.ServerInfos;
import com.github.lory24.loadbalancer.bungee.impl.ServerStats;
import com.google.gson.Gson;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.DataInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

/**
 * NOTES:
 * The priority manager is the class that will elaborate all the server's data and process them to create a priority
 * list used when someone tries to connect to a server. This class will support also a future feature, that will not
 * be implemented in the first version of this plugin: the support for other servers that aren't the lobbies servers.
 */
public class PriorityManager {

    public void connectToBestLobby(@NotNull ProxiedPlayer proxiedPlayer) {
        final ServerInfo serverInfo = ProxyServer.getInstance().getServers().get(getBestLobby().get(0).getServerName());
        proxiedPlayer.connect(serverInfo);
    }

    @SuppressWarnings("unchecked")
    private @NotNull List<ServerStats> getBestLobby() {
        // Create the lobbies servers arraylist. This will store all the server without any order
        final List<ServerStats> allLobbiesServers = new ArrayList<>();
        ArrayList<Map<String, Object>> lobbiesServersFromYAML = (ArrayList<Map<String, Object>>) ((Map<String, Object>)((Map<String, Object>) new Yaml().load(LoadBalancerBungee.INSTANCE.getConfigInputStream())).get("settings")).get("lobbies");
        for (Map<String, Object> map: lobbiesServersFromYAML) allLobbiesServers.add(new ServerStats((String) map.get("host"), (int) map.get("port"), (String) map.get("inProxyConfigName")));

        // Get the data from every server
        allLobbiesServers.replaceAll(stats -> stats.setServerInfos(getServerInfos(stats)));

        // Start the magic: Update the order of the servers.
        return this.orderTheServers(allLobbiesServers);
    }

    /**
     * Get the infos about the server by connecting to the SpigotPlugin's server.
     * @param stats The infos of the server
     * @return The obtained infos
     */
    private ServerInfos getServerInfos(ServerStats stats) {
        ServerInfos serverInfos = null; // Default value = null
        try {
            Socket socket = new Socket();
            // Set some options
            socket.setSoTimeout(50);
            socket.setTcpNoDelay(true);
            // Connect and parse the response
            socket.connect(new InetSocketAddress(stats.getServerHost(), stats.getServerPort()));
            serverInfos = new Gson().fromJson(new DataInputStream(socket.getInputStream()).readUTF(), ServerInfos.class);
            socket.close(); // Close the connection
        } catch (Exception ignored) {
            // Skip the error: The server will be unreachable, so the loadbalancer will skip it
        }
        return serverInfos;
    }

    /**
     * This function will elaborate all the received data and determine the priority list. This list contains all the
     * servers in order by their performance. In order to determine this, it will use a "points system". This system
     * will compare each value of a server (in steps) and who has the best value wins a point. For example, in the TPS
     * check, who has the highest amount of tps will take a point. If every server has the same amount of tps (for
     * example), the processor will give the point to the first server inserted in the config.
     *
     * NOTE: If a server has reached the maximum amount of players, it will be removed from the list. So, if every server
     *       is full, the incoming player won't be connected to any of those
     *
     * @param stats The servers without any order
     * @return A list with the order of the servers by their performance
     */
    private @NotNull List<ServerStats> orderTheServers(@NotNull List<ServerStats> stats) {
        List<ServerStats> finalResult = new ArrayList<>();

        // Remove the values that won't be used
        stats.removeIf(value -> value == null ||
                value.getServerInfos().getOnlinePlayers() == value.getServerInfos().getMaxPlayers());

        // Points system
        LinkedHashMap<ServerStats, Double> points = new LinkedHashMap<>() {{ for (ServerStats serverStats: stats) put(serverStats, .0D); }};

        // The first check will be the % of used slots of the lobby server
        LinkedHashMap<ServerStats, Double> usedSlots = orderByValues(new LinkedHashMap<>()
            {{ for (ServerStats serverStats: stats) put(serverStats, (double) serverStats.getServerInfos().getOnlinePlayers() / serverStats.getServerInfos().getMaxPlayers()); }});
        points.replace(getFirstKey(usedSlots), points.get(getFirstKey(usedSlots)) +1);

        // The second check is the MB of used ram
        LinkedHashMap<ServerStats, Double> usedRam = orderByValues(new LinkedHashMap<>()
            {{ for (ServerStats serverStats: stats) put(serverStats, (double) serverStats.getServerInfos().getRamUsageInBytes() / 1000000); }});
        points.replace(getFirstKey(usedRam), points.get(getFirstKey(usedRam)) +1);

        // The last check is the TPS one
        LinkedHashMap<ServerStats, Double> tps = orderByValues(new LinkedHashMap<>()
            {{ for (ServerStats serverStats: stats) put(serverStats, serverStats.getServerInfos().getTps()); }});
        points.replace(getLastKey(tps), points.get(getLastKey(tps)) +1);

        // Put the values in the list, but first order the points hashmap
        points = orderByValues(points);
        for (Map.Entry<ServerStats, Double> e: points.entrySet()) finalResult.add(e.getKey());

        // Return the result
        return finalResult;
    }


    // region LoadBalancer utils functions

    @Contract("_ -> new")
    private @NotNull LinkedHashMap<ServerStats, Double> orderByValues(@NotNull LinkedHashMap<ServerStats, Double> hashMap) {
        // Sort the list by the values of the entries
        List<Map.Entry<ServerStats, Double>> entryList = new ArrayList<>(hashMap.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        return new LinkedHashMap<>() {{
            for (Map.Entry<ServerStats, Double> e: entryList)
                put(e.getKey(), e.getValue());
        }};
    }

    private ServerStats getLastKey(@NotNull LinkedHashMap<ServerStats, Double> hashMap) {
        ServerStats last = null;
        while (hashMap.entrySet().iterator().hasNext()) last = hashMap.entrySet().iterator().next().getKey();
        return last;
    }

    private ServerStats getFirstKey(@NotNull LinkedHashMap<ServerStats, Double> hashMap) {
        return hashMap.entrySet().iterator().next().getKey();
    }

    // endregion
}
