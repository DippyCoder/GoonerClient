package com.dippycoder.goonerclient.mixin;

import com.dippycoder.goonerclient.client.widgets.BrandWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void injectBrandPrefix(PlayerListEntry entry,
                                   CallbackInfoReturnable<Text> cir) {
        if (!BrandWidget.active) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (!entry.getProfile().id().equals(mc.player.getUuid())) return;

        MutableText prefix = Text.literal("⚜ ").withColor(0xAA55FF);
        Text modified = prefix.append(cir.getReturnValue());
        cir.setReturnValue(modified);
    }
}