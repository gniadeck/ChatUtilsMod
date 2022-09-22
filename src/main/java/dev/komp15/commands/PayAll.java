package dev.komp15.commands;

import dev.komp15.config.ModConfig;

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
