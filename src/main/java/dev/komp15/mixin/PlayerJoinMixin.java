package dev.komp15.mixin;

import dev.komp15.ChatUtilsMod;
import dev.komp15.listeners.ListenerManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerManager.class)
public class PlayerJoinMixin {

    @Inject(method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection; Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("HEAD"), cancellable = true)
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo callbackInfo) {
        ChatUtilsMod.LOGGER.info("Detected player connection " + player);
    }




}
