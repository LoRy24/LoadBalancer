package com.github.lory24.lobbybalancer.spigot;

import lombok.Getter;

public enum ConfigValues {
    SERVER_HOST("Settings.Host"),
    SERVER_PORT("Settings.Port"),
    ;

    @Getter
    private final String path;

    ConfigValues(String path) {
        this.path = path;
    }

    public Object get() {
        return LobbyBalancerSpigot.INSTANCE.getPlugin().getConfig().get(this.path);
    }
}
