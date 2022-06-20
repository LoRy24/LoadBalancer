package com.github.lory24.loadbalancer.bungee;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Map;

public class ConfigValues {

    // Servers
    @Getter
    private ArrayList<Map<String, Object>> lobbiesServersFromYAML;

    // Messages
    @Getter
    private String allLobbiesOfflineMessage,
                   hubCommandMessage,
                   hubCommandInCooldownMessage,
                   onlyOneLobbyAviable;

    // Hub command cooldown
    @Getter
    private int hubCommandCooldownMS;

    @SuppressWarnings("unchecked")
    public void loadConfig() {
        // Load the config
        Map<String, Object> yamlConfigSettings = (Map<String, Object>)((Map<String, Object>) new Yaml().load(LoadBalancerBungee.INSTANCE.getConfigInputStream())).get("settings");

        // Load the values
        this.lobbiesServersFromYAML = (ArrayList<Map<String, Object>>) yamlConfigSettings.get("lobbies");
        this.allLobbiesOfflineMessage = color((String) yamlConfigSettings.get("allLobbiesOfflineMessage"));
        this.hubCommandMessage = color((String) yamlConfigSettings.get("hubCommandMessage"));
        this.hubCommandInCooldownMessage = color((String) yamlConfigSettings.get("hubCommandInCooldownMessage"));
        this.hubCommandCooldownMS = (int) yamlConfigSettings.get("hubCommandCooldownMS");
        this.onlyOneLobbyAviable = (String) yamlConfigSettings.get("onlyOneLobbyAviable");
    }

    @Contract("_ -> new")
    private @NotNull String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
