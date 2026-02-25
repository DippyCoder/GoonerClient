package com.dippycoder.goonerclient.mixin;

import com.dippycoder.goonerclient.client.GoonerclientClient;
import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.widgets.ReachWidget;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class AttackMixin {

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttack(PlayerEntity player, Entity target, CallbackInfo ci) {
        for (Menu.Statement s : GoonerclientClient.menu.getStatements()) {
            if (s instanceof ReachWidget reach && s.isEnabled()) {
                reach.onHit(target);
            }
        }
    }
}