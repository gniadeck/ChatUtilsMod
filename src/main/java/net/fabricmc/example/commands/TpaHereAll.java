package net.fabricmc.example.commands;

public class TpaHereAll extends AllPlayersCommand{
    @Override
    protected String getCommandToInvoke(String player, String arg) {
        return "/tpahere " + player;
    }

    @Override
    protected String getCommandName() {
        return "tpahereall";
    }
}
