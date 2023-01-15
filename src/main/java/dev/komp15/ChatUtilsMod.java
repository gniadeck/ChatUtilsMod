package dev.komp15;

import dev.komp15.commands.*;
import dev.komp15.config.ModConfig;
import dev.komp15.model.messages.GlobalMessageQueue;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatUtilsMod implements ModInitializer, ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ChatUtils");

	@Override
	public void onInitialize() {
	    onInitializeClient();
	}

	@Override
	public void onInitializeClient() {
		ModConfig.registerConfigs();
		MessageAll.register(ClientCommandManager.DISPATCHER);
		(new PayAll()).onInit();
		(new TpaHereAll()).onInit();
		(new GiveawayCommand()).register();
		(new RandomPlayer()).register();
		(new AllPlayers()).onInit();
		(new WelcomeNewPlayersCommand()).register();
		(new HelloAll()).onInit();
		(new GlobalMessageQueue()).init();
		(new MsgAll()).onInit();
	}
}
