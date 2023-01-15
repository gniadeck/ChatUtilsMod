package dev.komp15.commands;

import com.mojang.brigadier.Command;
import dev.komp15.config.ModConfig;
import dev.komp15.model.messages.GlobalMessageQueue;
import dev.komp15.utils.PlayerCollectionUtils;
import dev.komp15.utils.PlayerFacade;
import dev.komp15.utils.PlayerLogger;
import dev.komp15.utils.PlayerMessageUtils;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.Collection;
public class HelloAll extends AllPlayersCommand{

    @Override
    protected String getCommandToInvoke(String player, String arg) {
        return ModConfig.HELLO_PREFIX.replace("%p", player);
    }

    @Override
    protected Command<FabricClientCommandSource> handleNoArguments() {
        return c -> {
            PlayerLogger.playerLog("Players " + c.getSource().getPlayerNames(), c);
            Collection<String> playerNames = PlayerCollectionUtils
                    .filterPlayerNames(PlayerFacade.getFilteredServerPlayers(c), PlayerFacade.getInvokerName(c));

            setAndRunExecutor(new Thread(() -> {
                for (String playerName : playerNames) {
                    GlobalMessageQueue.queueMessage(getCommandToInvoke(playerName, null));
                    sleep();
                }
            }));

            return 1;
        };
    }

    @Override
    protected String getCommandName() {
        return "helloall";
    }

    @Override
    protected long getSleepTimeBetweenInvocations() {
        return ModConfig.ALLPLAYERS_DELAY;
    }
}
