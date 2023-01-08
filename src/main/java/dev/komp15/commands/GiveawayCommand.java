package dev.komp15.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import dev.komp15.config.ModConfig;
import dev.komp15.listeners.ListenerManager;
import dev.komp15.model.messages.GlobalMessageQueue;
import dev.komp15.utils.PlayerFacade;
import dev.komp15.listeners.GiveawayChatListener;
import dev.komp15.utils.PlayerLogger;
import dev.komp15.utils.PlayerMessageUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.util.*;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class GiveawayCommand extends ExecutorBasedCommand{

    private String password;
    private static final Object lock = new Object();
    private Text lastMessage;

    public void register(){
        ClientCommandManager.DISPATCHER.register(literal("giveawayy")
                .then(argument("arg", string())
                        .executes(handleStop())
                        .then(argument("amount", integer())
                        .then(argument("message", greedyString())
                                .executes(this::handleCommand)))));
    }

    private Command<FabricClientCommandSource> handleStop(){
        return c -> {
            if(getString(c, "arg").equalsIgnoreCase("stop")){
                onStop().run(c);
            } else {
                PlayerLogger.playerLog("Unrecognized option: " + getString(c, "arg"));
            }
            return 1;
        };
    }

    public int handleCommand(CommandContext<FabricClientCommandSource> c){
        if(password != null || executorIsRunning()){
            PlayerLogger.playerLog("Giveaway is in progress! Password: " + password, c);
        }
        password= getString(c, "arg");

        setAndRunExecutor(new Thread(() -> {
            GiveawayChatListener listener = new GiveawayChatListener(getString(c, "arg"), this);
            ListenerManager.CHAT_LISTENERS.add(listener);
            PlayerMessageUtils.sendPublicMessage(getMessage(getString(c, "message"), getString(c, "arg"), String.valueOf(getInteger(c, "amount"))));
            lockThread();
            GlobalMessageQueue.queueMessage(ModConfig.GIVEAWAY_WIN_MESSAGE.replace("%p",
                    getUsername(lastMessage, c)).replace("%a", String.valueOf(getInteger(c, "amount"))));
            PlayerMessageUtils.sendPublicMessage(ModConfig.PAYALL_COMMAND + " " + getUsername(lastMessage, c) + " " + getInteger(c, "amount"));
            password = null;
        }));

        return 1;
    }

    private void lockThread(){
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void unlockThreads(){
        synchronized (lock){
            lock.notifyAll();
        }
    }

    public void passMessage(Text message){
        if(parseChatMessage(message).equals(password)){
            lastMessage = message;
            unlockThreads();
        }
    }

    private String getMessage(String message, String password, String amount){
        return message.replace("%p", password).replace("%a", amount);
    }

    private String getUsername(Text message, CommandContext<FabricClientCommandSource> c){
        Set<String> players = new HashSet<>(PlayerFacade.getFilteredServerPlayers(c));
        List<String> messageParts = Arrays.asList(message.getString().split(" "));
        for(String messagePart : messageParts){
                if(players.contains(messagePart)) return messagePart;
        }
        PlayerLogger.playerLog("Cannot identify player from message " + message.getString(), c);
        return "";
    }

    private String parseChatMessage(Text message){
        if(!message.getString().contains(":")) return message.getString();
        return message.getString().substring(message.getString().lastIndexOf(":")+1).strip().trim();
    }

    @Override
    protected String getCommandName() {
        return "giveawayy";
    }
}
