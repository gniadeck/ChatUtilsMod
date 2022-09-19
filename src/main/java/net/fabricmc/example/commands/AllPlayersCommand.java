package net.fabricmc.example.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.example.utils.PlayerLogger;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ClientChatListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public abstract class AllPlayersCommand {

    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    protected abstract String getCommandToInvoke(String player, String arg);
    protected abstract String getCommandName();

    protected long getSleepTimeBetweenInvocations(){
        return 2200;
    }

    public void init(){
        register();
    }

    private void register(){

        ClientCommandManager.DISPATCHER.register(literal(getCommandName())
                .then(argument("arg", greedyString())
                        .executes(handleOneArgument()))
                        .executes(handleNoArguments()));


    }

    protected Command<FabricClientCommandSource> handleNoArguments(){
        System.out.println("No args");

        return c -> {
            LOGGER.error("NO ARGS");
            PlayerLogger.playerLog("Please pass argument parameter!!", c.getSource().getPlayer().getUuid());
            return 0;
        };
    }

    protected Command<FabricClientCommandSource> handleOneArgument(){
        System.out.println("One arg");

        return c -> {
            LOGGER.info("One arg");
            try {
                PlayerLogger.playerLog("Starting method...",
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
                        c.getSource().getPlayer().sendChatMessage(getCommandToInvoke(playerName, getString(c, "arg")));
                        System.out.println("Invoking " + getCommandToInvoke(playerName, getString(c, "arg")));
                        try {
                            Thread.sleep(getSleepTimeBetweenInvocations());
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
        };
    }

}
