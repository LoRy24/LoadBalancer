package com.github.lory24.loadbalancer.bungee.commands;

import com.github.lory24.loadbalancer.bungee.ConfigValues;
import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;
import com.github.lory24.loadbalancer.bungee.impl.LobbiesUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HubCommand extends Command {

    private static final List<ProxiedPlayer> playersInCooldown = new ArrayList<>();

    public HubCommand() {
        super("hub", "hub.execute", "hub");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;

        // Define some values
        ConfigValues configValues = LoadBalancerBungee.INSTANCE.getConfigValues();
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

        if (LoadBalancerBungee.INSTANCE.getPriorityManager().bestLobby.size() == 1 && LobbiesUtils.isLobby(proxiedPlayer.getServer().getInfo().getName())) {
            proxiedPlayer.sendMessage(new TextComponent(LoadBalancerBungee.INSTANCE.getConfigValues().getOnlyOneLobbyAviable()));
            return;
        }

        // If the player is in cooldown, send him a message and then return
        if (!playersInCooldown.contains(proxiedPlayer)) {
            // Add in cooldown
            playersInCooldown.add(proxiedPlayer);
            ProxyServer.getInstance().getScheduler().schedule(LoadBalancerBungee.INSTANCE.getPlugin(), () -> playersInCooldown
                            .remove(proxiedPlayer), configValues.getHubCommandCooldownMS(), TimeUnit.MILLISECONDS);

            // Connect the player to the best server
            LoadBalancerBungee.INSTANCE.getPriorityManager().connectToBestLobby(proxiedPlayer);

            // Send the connecting message
            sender.sendMessage(new TextComponent(configValues.getHubCommandMessage()));
            return;
        }

        proxiedPlayer.sendMessage(new TextComponent(configValues
                .getHubCommandInCooldownMessage()));
    }
}
