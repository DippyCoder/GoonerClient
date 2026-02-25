package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class CoordinatesWidget extends Menu.Statement {

    public CoordinatesWidget(int x, int y) {
        super("Coordinates", "Displays your current X, Y, Z position in the world.", x, y);
    }

    @Override
    public void render(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        int px = (int) mc.player.getX();
        int py = (int) mc.player.getY();
        int pz = (int) mc.player.getZ();

        context.drawTextWithShadow(mc.textRenderer,
                String.format("XYZ: %d, %d, %d", px, py, pz),
                x, y, 0xFFFFFFFF);
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}