package com.dippycoder.goonerclient.mixin;

import com.dippycoder.goonerclient.client.widgets.ZoomWidget;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class FovMixin {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void overrideFov(Camera camera, float tickProgress, boolean changingFov,
                             CallbackInfoReturnable<Float> cir) {
        if (ZoomWidget.currentFov != ZoomWidget.DEFAULT_FOV || ZoomWidget.prevFov != ZoomWidget.DEFAULT_FOV) {
            float smoothFov = ZoomWidget.prevFov + (ZoomWidget.currentFov - ZoomWidget.prevFov) * tickProgress;
            cir.setReturnValue(smoothFov);
        }
    }
}