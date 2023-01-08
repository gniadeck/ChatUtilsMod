package dev.komp15.utils;

import dev.komp15.ChatUtilsMod;
import net.minecraft.client.MinecraftClient;

public class PlayerMessageUtils {

    public static void sendPublicMessage(String message){
        ChatUtilsMod.LOGGER.debug("[PlayerMessageUtils] Sending message " + message);
        MinecraftClient.getInstance().player.sendChatMessage(message);
    }
}
