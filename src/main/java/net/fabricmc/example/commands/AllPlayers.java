package net.fabricmc.example.commands;

import net.fabricmc.example.config.ModConfig;

public class AllPlayers extends AllPlayersCommand{
    @Override
    protected String getCommandToInvoke(String player, String arg) {
        return arg.replace("%u", player);
    }

    @Override
    protected String getCommandName() {
        return "allplayers";
    }

    @Override
    protected long getSleepTimeBetweenInvocations() {
        return ModConfig.ALLPLAYERS_DELAY;
    }
}
