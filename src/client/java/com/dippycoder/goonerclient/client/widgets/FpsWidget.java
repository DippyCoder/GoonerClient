package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FpsWidget extends Menu.Statement {

    public FpsWidget(int x, int y) {
        super("FPS", "Shows your current frames per second.", x, y);
    }

    @Override
    public void render(DrawContext context) {
        int fps = MinecraftClient.getInstance().getCurrentFps();
        context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                "FPS: " + fps,
                x, y, 0xFFFFFFFF
        );
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}