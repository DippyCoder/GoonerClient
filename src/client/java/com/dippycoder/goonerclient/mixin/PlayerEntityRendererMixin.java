package com.dippycoder.goonerclient.mixin;

import com.dippycoder.goonerclient.client.widgets.BrandWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"))
    private void renderBrandIcon(EntityRenderState state, MatrixStack matrices,
                                 OrderedRenderCommandQueue queue,
                                 CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (!BrandWidget.active) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (!(state instanceof PlayerEntityRenderState)) return;
        if (state.displayName == null) return;
        if (!state.displayName.getString().contains(mc.player.getName().getString())) return;

        Vec3d pos = state.nameLabelPos;
        if (pos == null) return;

        // render icon above the nametag (positive y offset = higher up)
        queue.submitLabel(
                matrices,
                pos,
                -20, // y offset pushes it above the nametag
                Text.literal("⚜ ").withColor(0xAA55FF),
                !state.sneaking, // notSneaking
                state.light,
                state.squaredDistanceToCamera,
                cameraRenderState
        );
    }
}