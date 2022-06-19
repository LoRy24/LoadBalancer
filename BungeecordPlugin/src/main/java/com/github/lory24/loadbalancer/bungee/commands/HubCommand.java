package com.github.lory24.loadbalancer.bungee.commands;

import com.github.lory24.loadbalancer.bungee.LoadBalancerBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub", "hub.execute", "hub");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

        // Send the connecting message
        sender.sendMessage(new TextComponent(LoadBalancerBungee.INSTANCE.getConfigValues().getHubCommandMessage()));

        // Connect the player to the best server
        LoadBalancerBungee.INSTANCE.getPriorityManager().connectToBestLobby(proxiedPlayer);
    }
}
