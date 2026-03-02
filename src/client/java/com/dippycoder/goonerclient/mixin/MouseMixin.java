package com.dippycoder.goonerclient.mixin;

import com.dippycoder.goonerclient.client.GoonerclientClient;
import com.dippycoder.goonerclient.client.widgets.ZoomWidget;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, MouseInput input, int action, CallbackInfo ci) {
        if (action != 1) return; // only GLFW_PRESS
        if (GoonerclientClient.cpsWidget == null) return;
        if (input.button() == 0) GoonerclientClient.cpsWidget.registerClick(false);
        if (input.button() == 1) GoonerclientClient.cpsWidget.registerClick(true);
    }

    @ModifyArg(method = "updateMouse",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"),
            index = 0)
    private double scaleSensitivityX(double original) {
        if (ZoomWidget.currentFov >= ZoomWidget.DEFAULT_FOV - 0.5f) return original;
        return original * (ZoomWidget.currentFov / ZoomWidget.DEFAULT_FOV);
    }

    @ModifyArg(method = "updateMouse",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"),
            index = 1)
    private double scaleSensitivityY(double original) {
        if (ZoomWidget.currentFov >= ZoomWidget.DEFAULT_FOV - 0.5f) return original;
        return original * (ZoomWidget.currentFov / ZoomWidget.DEFAULT_FOV);
    }
}