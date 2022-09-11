package net.fabricmc.example.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.example.utils.PlayerLogger;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class MessageAll {
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){

        dispatcher.register(literal("messageall")
                .then(argument("message", greedyString())
                        .executes(c -> {
                            try {
                                PlayerLogger.playerLog("Starting message all method...",
                                        c.getSource().getPlayer().getUuid());
                                PlayerLogger.playerLog("Players " + c.getSource().getPlayerNames(),
                                        c.getSource().getPlayer().getUuid());

                                Collection<String> playerNames = c.getSource().getPlayerNames();
                                playerNames = playerNames.stream()
                                        .filter(name -> name.length() > 1 && name.charAt(0) != '!')
                                        .collect(Collectors.toList());
                                playerNames.remove(c.getSource().getPlayer().getEntityName());

                                Collection<String> finalPlayerNames = playerNames;
                                Thread executor = new Thread(() -> {
                                    for (String playerName : finalPlayerNames) {
                                        c.getSource().getPlayer().sendChatMessage(getMsgCommand(playerName, getString(c, "message")));
                                        try {
                                            Thread.sleep(2100);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                                executor.start();



                            } catch (Throwable t){
                                LOGGER.error(t.getMessage());
                            }
                            return 1;
                        })).executes(c -> {
                    PlayerLogger.playerLog("Please pass message parameter!", c.getSource().getPlayer().getUuid());
                    return 0;
                }));

    }

    private static String getMsgCommand(String player, String message){
        Random random = new Random();
        return "/msg " + player + " " + message;
    }

    private static String randomChars(int length){
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; i++){
            builder.append(((char)(random.nextInt(26) + 'a')));
        }
        return builder.toString();
    }
}
