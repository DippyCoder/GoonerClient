package com.dippycoder.goonerclient.mixin;

import com.dippycoder.goonerclient.client.widgets.FullbrightWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleOption.class)
public class GammaMixin<T> {

    /*
     * Adapted from GammaUtils by Sjouwer
     * https://github.com/sjouwer/gamma-utils
     * Licensed under LGPL-3.0
     */

    @Shadow
    @Final
    Text text;

    @Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
    public void overrideGamma(CallbackInfoReturnable<T> cir) {
        if (isGammaOption() && FullbrightWidget.active) {
            cir.setReturnValue((T)(Double) 16.0);
        }
    }

    @Unique
    private boolean isGammaOption() {
        if (text.getContent() instanceof TranslatableTextContent t) {
            return t.getKey().equals("options.gamma");
        }
        return false;
    }
}