package dev.komp15.utils;

import net.minecraft.client.MinecraftClient;

public class PlayerMessageUtils {

    public static void sendPublicMessage(String message){
        MinecraftClient.getInstance().player.sendChatMessage(message);
    }
}
