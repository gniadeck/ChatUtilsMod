package dev.komp15.listeners;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListenerManager {
    public static List<ChatListener> CHAT_LISTENERS = new ArrayList<>();
    public static List<PlayerJoinListener> PLAYER_JOIN_LISTENERS = new ArrayList<>();

    public static void notifyChatListeners(Text playerNames){
        for(ChatListener listener : CHAT_LISTENERS){
            listener.processMessage(playerNames);
        }
    }

    public static void notifyPlayerJoinListeners(Set<String> playerIds) {
        PLAYER_JOIN_LISTENERS.forEach(listener -> listener.notify(playerIds));
    }
}
