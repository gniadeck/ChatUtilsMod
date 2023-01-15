package dev.komp15.commands;

import com.mojang.brigadier.Command;
import dev.komp15.listeners.ListenerManager;
import dev.komp15.listeners.ServerLeftListener;
import dev.komp15.utils.PlayerLogger;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public abstract class ExecutorBasedCommand implements ServerLeftListener {

    protected Thread executor;

    public ExecutorBasedCommand() {
        init();
    }

    private void init(){
        onInit();
        registerAsServerLeftListener();
    }

    private void registerAsServerLeftListener() {
        ListenerManager.SERVER_LEFT_LISTENERS.add(this);
    }

    protected void onInit(){};

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

    @Override
    public void onServerLeft() {
        executor.interrupt();
    }
}
