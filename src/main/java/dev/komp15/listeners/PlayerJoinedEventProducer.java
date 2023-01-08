package dev.komp15.listeners;

import com.mojang.brigadier.context.CommandContext;
import dev.komp15.ChatUtilsMod;
import dev.komp15.utils.PlayerFacade;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.HashSet;
import java.util.Set;

public class PlayerJoinedEventProducer implements Runnable {

    private final CommandContext<FabricClientCommandSource> context;
    private Set<String> currentPlayers;

    public PlayerJoinedEventProducer(CommandContext<FabricClientCommandSource> context) {
        this.context = context;
    }

    @Override
    public void run() {
        currentPlayers = refreshPlayers();

        while(true) {
            sleep();
            Set<String> refreshedPlayers = refreshPlayers();
            refreshedPlayers.removeAll(currentPlayers);
            if (!refreshedPlayers.isEmpty()) {
                ChatUtilsMod.LOGGER.info("Detected new players! Notifying listeners");
                ListenerManager.notifyPlayerJoinListeners(refreshedPlayers);
            }
            currentPlayers = refreshPlayers();

        }

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
