package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class SpeedWidget extends Menu.Statement {

    public SpeedWidget(int x, int y) {
        super("Speed", "Shows your current movement speed in blocks per second.", x, y);
    }

    @Override
    public void render(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        // actual horizontal displacement per tick * 20 = blocks per second
        double dx = mc.player.getX() - mc.player.lastRenderX;
        double dz = mc.player.getZ() - mc.player.lastRenderZ;
        double speed = Math.sqrt(dx * dx + dz * dz) * 20;

        context.drawTextWithShadow(
                mc.textRenderer,
                String.format("Speed: %.2f bps", speed),
                x, y, 0xFFFFFFFF
        );
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}