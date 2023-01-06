package dev.komp15.utils;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.UUID;

public class PlayerLogger {

    public static void playerLog(String message, UUID playerUUID){
        MinecraftClient.getInstance().player.sendSystemMessage(Text.of(message), playerUUID);
    }
    public static void playerLog(String message, CommandContext<FabricClientCommandSource> c){
        playerLog(message, c.getSource().getPlayer().getUuid());
    }
    public static void playerLog(String message){
        playerLog(message, UUID.randomUUID());
    }


}
