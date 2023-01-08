package dev.komp15.model.messages;

import dev.komp15.ChatUtilsMod;
import dev.komp15.config.ModConfig;
import dev.komp15.listeners.ListenerManager;
import dev.komp15.listeners.MessageSentListener;
import dev.komp15.utils.PlayerMessageUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GlobalMessageQueue implements MessageSentListener {

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
        ListenerManager.MESSAGE_SENT_LISTENERS.add(this);
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
        return lastUserMessageTimestamp + ModConfig.GLOBAL_MESSAGE_QUEUE_DELAY < System.currentTimeMillis();
    }

    private void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageSent(String message) {
        ChatUtilsMod.LOGGER.info("Detected user message, updated last user message timestamp");
        lastUserMessageTimestamp = System.currentTimeMillis();
    }

}
