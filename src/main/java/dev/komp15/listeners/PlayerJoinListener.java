package dev.komp15.listeners;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Set;

public interface PlayerJoinListener {
    void notify(Set<String> playerNames);
}
