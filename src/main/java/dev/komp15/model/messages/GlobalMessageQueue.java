package dev.komp15.model.messages;

import dev.komp15.ChatUtilsMod;
import dev.komp15.config.ModConfig;
import dev.komp15.listeners.ChatListener;
import dev.komp15.listeners.ListenerManager;
import dev.komp15.utils.PlayerFacade;
import dev.komp15.utils.PlayerMessageUtils;
import net.minecraft.text.Text;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GlobalMessageQueue implements ChatListener {

    private static BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private static Thread messageSender;
    private static long lastUserMessageTimestamp;

    public static void queueMessage(String message){
        if(messageSlotIsFree()){
            ChatUtilsMod.LOGGER.info("Skipping adding to queue since slot is free");
            sendMessage(message);
        } else {
            ChatUtilsMod.LOGGER.info("Added message: \"" + message + "\" to the queue");
            messageQueue.add(message);
        }
    }

    public void init(){
        ListenerManager.CHAT_LISTENERS.add(this);
        lastUserMessageTimestamp = System.currentTimeMillis();

        messageSender = new Thread(() -> {
            while(true){
                trySend();
                sleep();
            }
        });
        messageSender.setDaemon(true);
        messageSender.start();
        ChatUtilsMod.LOGGER.info("Initialized message queue");
    }

    private void trySend(){
        if(!messageQueue.isEmpty() && messageSlotIsFree()){
            sendMessage(messageQueue.poll());
        }
    }

    private static void sendMessage(String message){
        PlayerMessageUtils.sendPublicMessage(message);
        lastUserMessageTimestamp = System.currentTimeMillis();
    }

    private static boolean messageSlotIsFree(){
        return lastUserMessageTimestamp + ModConfig.ALLPLAYERS_DELAY < System.currentTimeMillis();
    }

    private void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void processMessage(Text message) {
        if(message.getString().contains(PlayerFacade.getPlayerName())){
            ChatUtilsMod.LOGGER.info("Detected user message, updated last user message timestamp");
            lastUserMessageTimestamp = System.currentTimeMillis();
        }
    }
}
