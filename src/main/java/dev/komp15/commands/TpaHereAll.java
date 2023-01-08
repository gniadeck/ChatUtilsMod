package dev.komp15.commands;

import com.mojang.brigadier.Command;
import dev.komp15.config.ModConfig;
import dev.komp15.utils.PlayerMessageUtils;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;

public class TpaHereAll extends AllPlayersCommand{
    @Override
    protected String getCommandToInvoke(String player, String arg) {
        return ModConfig.TPAHEREALL_TPAHERE_COMMAND +" " + player;
    }

    @Override
    protected String getCommandName() {
        return "tpahereall";
    }

    @Override
    protected Command<FabricClientCommandSource> handleNoArguments() {
        return c -> {
            setAndRunExecutor(new Thread(() -> {
                for(String playerName : c.getSource().getPlayerNames()){
                    PlayerMessageUtils.sendPublicMessage(getCommandToInvoke(playerName, null));
                    sleep();
                }
            }));
            return 1;
        };
    }

    @Override
    protected Command<FabricClientCommandSource> handleOneArgument() {
        return c -> {
            Thread thread = new Thread(() -> {
                for(String playerName : c.getSource().getPlayerNames()){
                    PlayerMessageUtils.sendPublicMessage(ModConfig.MESSAGEALL_PREFIX + " " + playerName + " " + getString(c, "arg"));
                    sleep();
                    PlayerMessageUtils.sendPublicMessage(getCommandToInvoke(playerName, null));
                    sleep();
                }
            });
            thread.start();
            return 1;
        };
    }

    @Override
    protected long getSleepTimeBetweenInvocations() {
        return 1000;
    }
}
