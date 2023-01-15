package dev.komp15.commands;

import com.mojang.brigadier.Command;
import dev.komp15.ChatUtilsMod;
import dev.komp15.model.messages.GlobalMessageQueue;
import dev.komp15.utils.PlayerFacade;
import dev.komp15.utils.PlayerMessageUtils;
import dev.komp15.utils.PlayerCollectionUtils;
import dev.komp15.utils.PlayerLogger;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.Collection;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public abstract class AllPlayersCommand extends ExecutorBasedCommand {

    protected abstract String getCommandToInvoke(String player, String arg);


    protected long getSleepTimeBetweenInvocations(){
        return 2200;
    }

    @Override
    public void onInit(){
        register();
    }

    @Override
    public void onServerLeft() {
        if(executor != null){
            executor.stop();
            executor = null;
        }
    }

    private void register(){
        ClientCommandManager.DISPATCHER.register(literal(getCommandName())
                .then(argument("arg", greedyString())
                        .executes(handleStopArgument()))
                        .executes(handleNoArguments()));
    }

    protected Command<FabricClientCommandSource> handleNoArguments(){
        return c -> {
            ChatUtilsMod.LOGGER.error("NO ARGS");
            PlayerLogger.playerLog("Please pass argument parameter!!", c);
            return 0;
        };
    }

    private Command<FabricClientCommandSource> handleStopArgument(){
        return c -> {
            if(getString(c, "arg").equalsIgnoreCase("stop")){
                onStop().run(c);
            } else {
                handleOneArgument().run(c);
            }
            return 1;
        };
    }

    protected Command<FabricClientCommandSource> handleOneArgument(){
        return c -> {
                PlayerLogger.playerLog("Players " + c.getSource().getPlayerNames(),c);
                Collection<String> playerNames = PlayerCollectionUtils
                        .filterPlayerNames(PlayerFacade.getFilteredServerPlayers(c), PlayerFacade.getInvokerName(c));

                setAndRunExecutor(new Thread(() -> {
                    for (String playerName : playerNames) {
                        GlobalMessageQueue.queueMessage(getCommandToInvoke(playerName, getString(c, "arg")));
                        sleep();
                    }
                }));


            return 1;
        };
    }


    public void sleep(){
        try {
            Thread.sleep(getSleepTimeBetweenInvocations());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
