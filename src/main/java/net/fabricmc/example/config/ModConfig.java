package net.fabricmc.example.config;

import com.mojang.datafixers.util.Pair;

public class ModConfig {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;
    public static Integer PAYALL_DELAY;
    public static String PAYALL_COMMAND;
    public static Integer ALLPLAYERS_DELAY;
    public static String MESSAGEALL_PREFIX;
    public static Integer MESSAGEALL_DELAY;
    public static String TPAHEREALL_MESSAGE_PREFIX;
    public static String GIVEAWAY_WIN_MESSAGE;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of("SeverFunMod" + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("payall.delay", 1000), "int");
        configs.addKeyValuePair(new Pair<>("payall.command", "/pay"), "String");
        configs.addKeyValuePair(new Pair<>("allplayers.delay", 7000), "int");
        configs.addKeyValuePair(new Pair<>("messageall.prefix", "/msg"), "String");
        configs.addKeyValuePair(new Pair<>("messageall.delay", 1000), "int");
        configs.addKeyValuePair(new Pair<>("tpahereall.message.prefix", "/msg"), "String");
        configs.addKeyValuePair(new Pair<>("giveaway.winmessage", "Congratulations, %p you won %a$!"), "String");
    }

    private static void assignConfigs() {
        PAYALL_DELAY = CONFIG.getOrDefault("payall.delay", 1000);
        PAYALL_COMMAND = CONFIG.getOrDefault("payall.command", "/pay");
        ALLPLAYERS_DELAY = CONFIG.getOrDefault("allplayers.delay", 7000);
        MESSAGEALL_PREFIX = CONFIG.getOrDefault("messageall.prefix", "/msg");
        MESSAGEALL_DELAY = CONFIG.getOrDefault("messageall.delay", 1000);
        TPAHEREALL_MESSAGE_PREFIX = CONFIG.getOrDefault("tpahereall.message.prefix", "/msg");
        GIVEAWAY_WIN_MESSAGE = CONFIG.getOrDefault("giveaway.winmessage", "Congratulations, %p you won %a$!");
        System.out.println("Loaded properties " + configs.getConfigsList());
        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
}
