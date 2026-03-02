package com.dippycoder.goonerclient.mixin;

import com.dippycoder.goonerclient.client.widgets.ZoomWidget;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class ScrollMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (ZoomWidget.zooming) {
            ZoomWidget.scroll(vertical);
            ci.cancel(); // prevent inventory scroll etc while zooming
        }
    }
}