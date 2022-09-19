package net.fabricmc.example.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.example.config.ModConfig;
import net.fabricmc.example.utils.PlayerLogger;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class PayAll extends AllPlayersCommand {
    @Override
    protected String getCommandToInvoke(String player, String arg) {
        return ModConfig.PAYALL_COMMAND +" " + player + " " + arg;
    }

    @Override
    protected String getCommandName() {
        return "payall";
    }

    @Override
    protected long getSleepTimeBetweenInvocations() {
        return ModConfig.PAYALL_DELAY;
    }
}
