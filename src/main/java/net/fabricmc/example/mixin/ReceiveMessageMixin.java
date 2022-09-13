package net.fabricmc.example.mixin;

import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.listeners.ListenerManager;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.InGameHud;
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

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V", at = @At("HEAD"), cancellable = true)
    public void addMessage(Text message, CallbackInfo callbackInfo) {
        System.out.println("CHAT MESSAGE: " + message.getString());
        ListenerManager.notifyChatListeners(message);
    }

}
