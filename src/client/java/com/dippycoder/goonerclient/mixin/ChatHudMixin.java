package com.dippycoder.goonerclient.mixin;

import com.dippycoder.goonerclient.client.widgets.BrandWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.text.Text;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void prependIcon(Text message, CallbackInfo ci) {
        if (!BrandWidget.active) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        String msg = message.getString();
        String name = mc.player.getName().getString();

        // only prepend to own messages
        if (msg.contains("<" + name + ">")) {
            Text modified = Text.literal("[GC] ").withColor(0xAA55FF)
                    .append(message);
            ((ChatHud)(Object)this).addMessage(modified);
            ci.cancel();
        }
    }
}