package com.github.lory24.loadbalancer.velocityplugin;

import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
    private TextComponent allLobbiesOfflineMessage,
                   hubCommandMessage,
                   hubCommandInCooldownMessage,
                   onlyOneLobbyAviable,
                   kickFromServerMessage;

    // Hub command cooldown
    @Getter
    private int hubCommandCooldownMS;

    // Define if the plugin should connect the player to a lobby on join
    @Getter
    private boolean connectToLobbyOnJoin;

    @SuppressWarnings("unchecked")
    public void loadConfig() {
        // Load the config
        Map<String, Object> yamlConfigSettings = (Map<String, Object>)((Map<String, Object>) new Yaml().load(LoadBalancerVelocity.INSTANCE.getConfigInputStream())).get("settings");

        // Load the values
        this.lobbiesServersFromYAML = (ArrayList<Map<String, Object>>) yamlConfigSettings.get("lobbies");
        this.allLobbiesOfflineMessage = color((String) yamlConfigSettings.get("allLobbiesOfflineMessage"));
        this.hubCommandMessage = color((String) yamlConfigSettings.get("hubCommandMessage"));
        this.hubCommandInCooldownMessage = color((String) yamlConfigSettings.get("hubCommandInCooldownMessage"));
        this.hubCommandCooldownMS = (int) yamlConfigSettings.get("hubCommandCooldownMS");
        this.onlyOneLobbyAviable = color((String) yamlConfigSettings.get("onlyOneLobbyAviable"));
        this.connectToLobbyOnJoin = (boolean) yamlConfigSettings.get("connectToLobbyOnJoin");
        this.kickFromServerMessage = color((String) yamlConfigSettings.get("kickFromServerMessage"));
    }

    @Contract("_ -> new")
    private @NotNull TextComponent color(String input) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(input);
    }
}
