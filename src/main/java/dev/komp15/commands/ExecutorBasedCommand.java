package dev.komp15.commands;

import com.mojang.brigadier.Command;
import dev.komp15.utils.PlayerLogger;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public abstract class ExecutorBasedCommand {

    private Thread executor;

    protected abstract String getCommandName();

    protected void setAndRunExecutor(Thread thread){
        if(executor != null && executor.isAlive()){
            PlayerLogger.playerLog("Command " + getCommandName() + " is already running! Invoke /" + getCommandName() + " stop" +
                    " in order to stop the command execution.");
        } else {
            executor = thread;
            executor.start();
        }
    }

    protected Command<FabricClientCommandSource> onStop() {
        return c -> {
            if(executor.isAlive()){
                executor.stop();
                PlayerLogger.playerLog("Command " + getCommandName() + " has been successfully stopped");
            } else {
                PlayerLogger.playerLog("Command " + getCommandName() + " is already stopped");
            }
            return 1;
        };
    }

    protected boolean executorIsRunning(){
        return executor != null && executor.isAlive();
    }

}
