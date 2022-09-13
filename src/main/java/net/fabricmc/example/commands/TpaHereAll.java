package net.fabricmc.example.commands;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;

public class TpaHereAll extends AllPlayersCommand{
    @Override
    protected String getCommandToInvoke(String player, String arg) {
        return "/tpahere " + player;
    }

    @Override
    protected String getCommandName() {
        return "tpahereall";
    }

    @Override
    protected Command<FabricClientCommandSource> handleNoArguments() {
        return c -> {
            Thread thread = new Thread(() -> {
                for(String playerName : c.getSource().getPlayerNames()){
                    c.getSource().getPlayer().sendChatMessage(getCommandToInvoke(playerName, null));
                    try {
                        Thread.sleep(getSleepTimeBetweenInvocations());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
            return 1;
        };
    }

    @Override
    protected Command<FabricClientCommandSource> handleOneArgument() {
        return c -> {
            Thread thread = new Thread(() -> {
                for(String playerName : c.getSource().getPlayerNames()){
                    c.getSource().getPlayer().sendChatMessage("/msg " + playerName + " " + getString(c, "arg"));
                    try {
                        Thread.sleep(getSleepTimeBetweenInvocations());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    c.getSource().getPlayer().sendChatMessage(getCommandToInvoke(playerName, null));
                    try {
                        Thread.sleep(getSleepTimeBetweenInvocations());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
