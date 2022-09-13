package net.fabricmc.example.commands;

import net.fabricmc.example.listeners.GiveawayChatListener;
import net.fabricmc.example.listeners.ListenerManager;
import net.fabricmc.example.utils.PlayerMessageUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class GiveawayCommand {

    private Text lastMessage;
    private String password;
    private static final Boolean resolved = false;
    private static final Object lock = new Object();

    public void register(){

        ClientCommandManager.DISPATCHER.register(literal("giveawayy")
                .then(argument("arg", string())
                        .then(argument("amount", string()))
                        .then(argument("message", greedyString())
                                .executes(c -> {
                                    password=getString(c, "arg");
                                    Thread thread = new Thread(() -> {
                                        GiveawayChatListener listener = new GiveawayChatListener(getString(c, "arg"), this);
                                        ListenerManager.CHAT_LISTENERS.add(listener);
                                        long invocationTime = System.currentTimeMillis();
                                        synchronized (lock){
                                            try {
                                                lock.wait();
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
//
                                        PlayerMessageUtils.sendPublicMessage("pog");
                                        System.out.println("EXECUTED THREAD");
                                    });
                                    thread.start();

                                    return 1;
                                }))));



    }

    public void passMessage(Text message){
        System.out.println("Message: " + parseChatMessage(message));
        System.out.println("Password: " + password);
        System.out.println(parseChatMessage(message).equals(password));
        if(parseChatMessage(message).equals(password)){
            synchronized (lock){
                lock.notifyAll();
            }
        }
        lastMessage = message;
    }

    private String parseChatMessage(Text message){
        if(!message.getString().contains(":")) return message.getString();
        return message.getString().substring(message.getString().lastIndexOf(":")+1).strip().trim();
    }



}
