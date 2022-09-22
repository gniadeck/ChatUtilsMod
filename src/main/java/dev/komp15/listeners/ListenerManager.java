package dev.komp15.listeners;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {
    public static List<ChatListener> CHAT_LISTENERS = new ArrayList<>();

    public static void notifyChatListeners(Text text){
        for(ChatListener listener : CHAT_LISTENERS){
            listener.processMessage(text);
        }
    }
}
