package net.fabricmc.example.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.example.utils.PlayerLogger;
import net.fabricmc.example.utils.PlayerMessageUtils;
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
                            PlayerMessageUtils.sendPublicMessage(processMessage(getString(c, "message"), c));
                            return 1;
                        }))
                        .executes(c -> {
                            PlayerLogger.playerLog("Please provide message",
                                    UUID.randomUUID());
                            return 0;
                        })

        );
    }

    private String processMessage(String message, CommandContext<FabricClientCommandSource> c){
        return message.replace("%u", getRandomPlayerNickname(c));
    }

    private String getRandomPlayerNickname(CommandContext<FabricClientCommandSource> c){
        Random random = new Random();
        List<String> players = new ArrayList<>(c.getSource().getPlayerNames());
        players.removeIf(player -> player.equals(c.getSource().getPlayer().getEntityName()));
        return players.get(random.nextInt(players.size()));
    }


}
