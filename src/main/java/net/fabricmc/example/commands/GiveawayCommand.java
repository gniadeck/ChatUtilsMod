package net.fabricmc.example.commands;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.example.listeners.GiveawayChatListener;
import net.fabricmc.example.listeners.ListenerManager;
import net.fabricmc.example.utils.PlayerMessageUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.*;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class GiveawayCommand {

    private String password;
    private static final Object lock = new Object();
    private Text lastMessage;

    public void register(){

        ClientCommandManager.DISPATCHER.register(literal("giveawayy")
                .then(argument("arg", string())
                        .then(argument("amount", integer())
                        .then(argument("message", greedyString())
                                .executes(c -> {
                                    if(password != null) throw new RuntimeException("Giveaway in progress");
                                    password=getString(c, "arg");
                                    Thread thread = new Thread(() -> {
                                        GiveawayChatListener listener = new GiveawayChatListener(getString(c, "arg"), this);
                                        ListenerManager.CHAT_LISTENERS.add(listener);
                                        long invocationTime = System.currentTimeMillis();
                                        PlayerMessageUtils.sendPublicMessage(getMessage(getString(c, "message"), getString(c, "arg"), String.valueOf(getInteger(c, "amount"))));
                                        synchronized (lock){
                                            try {
                                                lock.wait();
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
//
                                        PlayerMessageUtils.sendPublicMessage("Gratulacje, " + getUsername(lastMessage, c) + " wygrales " + getInteger(c, "amount") + "$!");
                                        PlayerMessageUtils.sendPublicMessage("/pay " + getUsername(lastMessage, c) + " " + getInteger(c, "amount"));
                                        password = null;
                                    });
                                    thread.start();

                                    return 1;
                                })))));



    }

    public void passMessage(Text message){
        System.out.println("Message: " + parseChatMessage(message));
        System.out.println("Password: " + password);
        System.out.println(parseChatMessage(message).equals(password));
        if(parseChatMessage(message).equals(password)){
            lastMessage = message;
            synchronized (lock){
                lock.notifyAll();
            }
        }
    }

    private String getMessage(String message, String password, String amount){
        return message.replace("%p%", password).replace("%a%", amount);
    }

    private String getUsername(Text message, CommandContext<FabricClientCommandSource> c){
        Set<String> players = new HashSet<>(c.getSource().getPlayerNames());
        List<String> messageParts = Arrays.asList(message.getString().split(" "));

        for(String messagePart : messageParts){
                if(players.contains(messagePart)) return messagePart;
        }

        throw new RuntimeException("Cannot identify player from message " + message.getString());
    }

    private String parseChatMessage(Text message){
        if(!message.getString().contains(":")) return message.getString();
        return message.getString().substring(message.getString().lastIndexOf(":")+1).strip().trim();
    }



}
