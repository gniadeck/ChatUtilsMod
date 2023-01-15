package dev.komp15.commands;

import com.mojang.brigadier.Command;
import dev.komp15.ChatUtilsMod;
import dev.komp15.listeners.ListenerManager;
import dev.komp15.listeners.PlayerJoinListener;
import dev.komp15.listeners.PlayerJoinedEventProducer;
import dev.komp15.listeners.ServerLeftListener;
import dev.komp15.model.messages.GlobalMessageQueue;
import dev.komp15.utils.PlayerLogger;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class WelcomeNewPlayersCommand implements PlayerJoinListener, ServerLeftListener {

    private Thread daemon = null;
    private PlayerJoinedEventProducer eventProducer;

    public void register() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("welcomenewplayers")
                .executes(handleCommand()));
    }

    public Command<FabricClientCommandSource> handleCommand(){
        return c -> {
            if(daemon != null && daemon.isAlive()){
                PlayerLogger.playerLog("Welcoming new players has been turned off");
                daemon.stop();
                daemon = null;
                ListenerManager.PLAYER_JOIN_LISTENERS.remove(this);
                ListenerManager.SERVER_LEFT_LISTENERS.remove(this);
            } else {
                PlayerLogger.playerLog("Welcoming new players has been turned on");
                eventProducer = new PlayerJoinedEventProducer(c);
                daemon = new Thread(eventProducer);
                daemon.setDaemon(true);
                daemon.start();
                ListenerManager.PLAYER_JOIN_LISTENERS.add(this);
                ListenerManager.SERVER_LEFT_LISTENERS.add(this);
            }
            return 0;
        };
    }

    @Override
    public void notify(Set<String> joinedPlayers) {
        ChatUtilsMod.LOGGER.info("Detected player joining " + joinedPlayers);
        for(String player : joinedPlayers){
            GlobalMessageQueue.queueMessage("Siemanko " + player);
        }
    }

    @Override
    public void onServerLeft() {
        if(daemon != null){
            ChatUtilsMod.LOGGER.info("Cleaning welcoming new players...");
            eventProducer.stop();
            eventProducer = null;
//            daemon.stop();
//            daemon.interrupt();
            daemon = null;
        }
    }
}
