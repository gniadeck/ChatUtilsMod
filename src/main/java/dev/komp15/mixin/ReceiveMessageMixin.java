package dev.komp15.mixin;

import dev.komp15.ChatUtilsMod;
import dev.komp15.listeners.ListenerManager;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public class ReceiveMessageMixin {

    @Shadow @Final List<ChatHudLine<OrderedText>> visibleMessages;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    public void addMessage(Text message, CallbackInfo callbackInfo) {
        ListenerManager.notifyChatListeners(message);
    }

}
