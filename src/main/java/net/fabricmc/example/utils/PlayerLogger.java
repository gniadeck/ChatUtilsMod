package net.fabricmc.example.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.UUID;

public class PlayerLogger {

    public static void playerLog(String message, UUID playerUUID){
        MinecraftClient.getInstance().player.sendSystemMessage(Text.of(message), playerUUID);
    }


}
