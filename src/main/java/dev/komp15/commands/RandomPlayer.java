package dev.komp15.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.komp15.model.messages.GlobalMessageQueue;
import dev.komp15.utils.PlayerFacade;
import dev.komp15.utils.PlayerLogger;
import dev.komp15.utils.PlayerMessageUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.*;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class RandomPlayer {

    public void register(){

        ClientCommandManager.DISPATCHER.register(literal("randomplayer")
                .then(argument("message", greedyString())
                        .executes(c -> {
                            GlobalMessageQueue.queueMessage(processMessage(getString(c, "message"), c));
                            return 1;
                        }))
                        .executes(c -> {
                            PlayerLogger.playerLog("Please provide message", c);
                            return 0;
                        })

        );
    }

    private String processMessage(String message, CommandContext<FabricClientCommandSource> c){
        return message.replace("%u", getRandomPlayerNickname(c));
    }

    private String getRandomPlayerNickname(CommandContext<FabricClientCommandSource> c){
        Random random = new Random();
        List<String> players = new ArrayList<>(PlayerFacade.getFilteredServerPlayers(c));
        return players.get(random.nextInt(players.size()));
    }


}
