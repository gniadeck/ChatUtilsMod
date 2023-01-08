package dev.komp15.mixin;

import dev.komp15.listeners.ListenerManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class SendMessageMixin {

    @Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo callbackInfo){
        ListenerManager.notifyMessageSentListeners(message);
    }

}
