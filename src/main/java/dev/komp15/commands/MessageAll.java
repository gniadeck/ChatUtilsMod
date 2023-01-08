package dev.komp15.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.komp15.config.ModConfig;
import dev.komp15.utils.PlayerFacade;
import dev.komp15.utils.PlayerMessageUtils;
import dev.komp15.utils.PlayerLogger;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.Collection;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

@Deprecated(forRemoval = true)
public class MessageAll {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){

        dispatcher.register(literal("messageall")
                .then(argument("message", greedyString())
                        .executes(c -> {
                                PlayerLogger.playerLog("Starting message all method...", c);
                                PlayerLogger.playerLog("Players " + PlayerFacade.getFilteredServerPlayers(c), c);

                                Collection<String> finalPlayerNames = PlayerFacade.getFilteredServerPlayers(c);
                                Thread executor = new Thread(() -> {
                                    for (String playerName : finalPlayerNames) {
                                        PlayerMessageUtils.sendPublicMessage(getMsgCommand(playerName, getString(c, "message")));
                                        sleep();
                                    }
                                });
                                executor.start();
                            return 1;
                        })).executes(c -> {
                    PlayerLogger.playerLog("Please pass message parameter!", c);
                    return 0;
                }));

    }

    private static void sleep(){
        try {
            Thread.sleep(ModConfig.MESSAGEALL_DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getMsgCommand(String player, String message){
        return ModConfig.MESSAGEALL_PREFIX +" " + player + " " + message;
    }

}
