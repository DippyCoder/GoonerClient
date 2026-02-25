package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeWidget extends Menu.Statement {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public TimeWidget(int x, int y) {
        super("Time", "Shows your current system time.", x, y);
    }

    @Override
    public void render(DrawContext context) {
        String time = LocalTime.now().format(FORMATTER);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                "Time: " + time,
                x, y, 0xFFFFFFFF);
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}