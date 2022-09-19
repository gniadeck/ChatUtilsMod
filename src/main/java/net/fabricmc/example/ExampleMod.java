package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.commands.*;
import net.fabricmc.example.config.ModConfig;
import net.fabricmc.example.config.SimpleConfig;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer, ClientModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

	    onInitializeClient();
		LOGGER.info("Hello Fabric world!");
	}

	@Override
	public void onInitializeClient() {
		ModConfig.registerConfigs();
		MessageAll.register(ClientCommandManager.DISPATCHER);
		(new PayAll()).init();
		(new TpaHereAll()).init();
		(new GiveawayCommand()).register();
		(new RandomPlayer()).register();
		(new AllPlayers()).init();
	}
}
