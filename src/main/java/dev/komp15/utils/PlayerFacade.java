package dev.komp15.utils;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.Collection;

public class PlayerFacade {
    public static String getInvokerName(CommandContext<FabricClientCommandSource> c){
        return c.getSource().getPlayer().getEntityName();
    }
    public static Collection<String> getFilteredServerPlayers(CommandContext<FabricClientCommandSource> c){
        return PlayerCollectionUtils.filterPlayerNames(c.getSource().getPlayerNames(), getInvokerName(c));
    }

}
