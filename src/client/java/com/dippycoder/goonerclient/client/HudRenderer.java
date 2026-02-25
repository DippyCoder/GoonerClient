package com.dippycoder.goonerclient.client;

import com.dippycoder.goonerclient.client.GoonerclientClient;
import com.dippycoder.goonerclient.client.Menu;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class HudRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        if (GoonerclientClient.menu != null) {
            GoonerclientClient.menu.renderAll(context);
        }
    }

    public static void register() {
        HudRenderCallback.EVENT.register(new HudRenderer());
    }
}