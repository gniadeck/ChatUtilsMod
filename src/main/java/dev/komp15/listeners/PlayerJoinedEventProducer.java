package dev.komp15.listeners;

import com.mojang.brigadier.context.CommandContext;
import dev.komp15.ChatUtilsMod;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.HashSet;
import java.util.Set;

public class PlayerJoinedEventProducer implements Runnable {

    private final CommandContext<FabricClientCommandSource> context;
    private Set<String> currentPlayers;
    private volatile boolean isStopped;

    public PlayerJoinedEventProducer(CommandContext<FabricClientCommandSource> context) {
        this.context = context;
    }

    public void stop(){
        isStopped = true;
    }

    @Override
    public void run() {
        currentPlayers = refreshPlayers();

        while(!isStopped) {
            sleep();
            Set<String> refreshedPlayers = refreshPlayers();
            refreshedPlayers.removeAll(currentPlayers);
            if (!refreshedPlayers.isEmpty()) {
                ChatUtilsMod.LOGGER.info("Detected new players! Notifying listeners");
                ListenerManager.notifyPlayerJoinListeners(refreshedPlayers);
            }
            currentPlayers = refreshPlayers();

        }
        ChatUtilsMod.LOGGER.info("Stopped player joined event producer");

    }

    private void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Set<String> refreshPlayers(){
        return new HashSet<>(context.getSource().getPlayerNames());
    }
}
