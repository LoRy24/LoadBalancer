package com.github.lory24.loadbalancer.velocityplugin.commands;

import com.github.lory24.loadbalancer.velocityplugin.ConfigValues;
import com.github.lory24.loadbalancer.velocityplugin.LoadBalancerVelocity;
import com.github.lory24.loadbalancer.velocityplugin.impl.ServerUtils;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HubCommand implements SimpleCommand {

    private static final List<Player> playersInCooldown = new ArrayList<>();

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public void execute(@NotNull Invocation invocation) {
        if (!(invocation.source() instanceof Player)) return;

        // Define some values
        ConfigValues configValues = LoadBalancerVelocity.INSTANCE.getConfigValues();
        Player proxiedPlayer = (Player) invocation.source();

        // Check if the player is into the only aviable lobby
        if (LoadBalancerVelocity.INSTANCE.getPriorityManager().bestLobby.size() == 1 &&
                ServerUtils.isLobby(proxiedPlayer.getCurrentServer().get().getServerInfo().getName())) {
            proxiedPlayer.sendMessage(configValues.getOnlyOneLobbyAviable());
            return;
        }

        // If the player is in cooldown, send him a message and then return
        if (!playersInCooldown.contains(proxiedPlayer)) {
            // Add in cooldown
            playersInCooldown.add(proxiedPlayer);
            LoadBalancerVelocity.INSTANCE.getProxyServer().getScheduler().buildTask(LoadBalancerVelocity.INSTANCE.getPluginEntry(), () -> playersInCooldown
                    .remove(proxiedPlayer)).delay(Duration.ofMillis(configValues.getHubCommandCooldownMS())).schedule();
            System.out.println();

            // Connect the player to the best server
            LoadBalancerVelocity.INSTANCE.getPriorityManager().connectToBestLobby(proxiedPlayer);

            // Send the connecting message
            proxiedPlayer.sendMessage(configValues.getHubCommandMessage());
            return;
        }

        proxiedPlayer.sendMessage(configValues
                .getHubCommandInCooldownMessage());
    }
}
