package dev.komp15.commands;

import dev.komp15.config.ModConfig;

public class MsgAll extends AllPlayersCommand{

    @Override
    protected String getCommandToInvoke(String player, String arg) {
        return ModConfig.MESSAGEALL_PREFIX + " " + player + " " + arg;
    }

    @Override
    protected String getCommandName() {
        return "msgall";
    }

}
