package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class PlaytimeWidget extends Menu.Statement {

    private final long startTime = System.currentTimeMillis();

    public PlaytimeWidget(int x, int y) {
        super("Playtime", "Tracks how long you have been playing this session.", x, y);
    }

    @Override
    public void render(DrawContext context) {
        long elapsed = System.currentTimeMillis() - startTime;
        long seconds = (elapsed / 1000) % 60;
        long minutes = (elapsed / 60000) % 60;
        long hours = elapsed / 3600000;

        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                String.format("Playtime: %02d:%02d:%02d", hours, minutes, seconds),
                x, y, 0xFFFFFFFF);
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}