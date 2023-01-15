package dev.komp15.mixin;

import dev.komp15.ChatUtilsMod;
import dev.komp15.listeners.ListenerManager;
import net.minecraft.client.MinecraftClientGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClientGame.class)
public class LeaveServerMixin {

    @Inject(method = "onLeaveGameSession()V", at = @At("HEAD"), cancellable = true)
    public void onLeaveGameSession(CallbackInfo callbackInfo){
        ChatUtilsMod.LOGGER.info("Detected leaving server");
        ListenerManager.notifyServerLeftListeners();
    }

}
