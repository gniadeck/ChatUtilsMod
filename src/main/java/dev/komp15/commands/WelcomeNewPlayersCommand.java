package dev.komp15.commands;

import com.mojang.brigadier.Command;
import dev.komp15.ChatUtilsMod;
import dev.komp15.listeners.ListenerManager;
import dev.komp15.listeners.PlayerJoinListener;
import dev.komp15.listeners.PlayerJoinedEventProducer;
import dev.komp15.model.messages.GlobalMessageQueue;
import dev.komp15.utils.PlayerLogger;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.Set;

public class WelcomeNewPlayersCommand implements PlayerJoinListener {

    private Thread daemon = null;
    private boolean isTurnedOn;

    public void register() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("welcomenewplayers")
                .executes(handleCommand()));
    }

    public Command<FabricClientCommandSource> handleCommand(){
        return c -> {
            if(isTurnedOn){
                PlayerLogger.playerLog("Welcoming new players has been turned off");
                daemon.stop();
                ListenerManager.PLAYER_JOIN_LISTENERS.remove(this);
                isTurnedOn = false;
            } else {
                PlayerLogger.playerLog("Welcoming new players has been turned on");
                daemon = new Thread(new PlayerJoinedEventProducer(c));
                daemon.setDaemon(true);
                daemon.start();
                ListenerManager.PLAYER_JOIN_LISTENERS.add(this);
                isTurnedOn = true;
            }
            return 0;
        };
    }

    // TODO extract message to properties and find out a mixin to grab messages
    // sent by the user, instead of checking if a given message contains nickname of the user
    @Override
    public void notify(Set<String> joinedPlayers) {
        ChatUtilsMod.LOGGER.info("Detected player joining " + joinedPlayers);
        for(String player : joinedPlayers){
            GlobalMessageQueue.queueMessage("Siemanko " + player);
        }
    }
}
