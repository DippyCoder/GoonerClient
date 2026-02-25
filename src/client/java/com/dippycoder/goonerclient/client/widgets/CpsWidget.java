package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayDeque;
import java.util.Deque;

public class CpsWidget extends Menu.Statement {

    private final Deque<Long> leftClicks = new ArrayDeque<>();
    private final Deque<Long> rightClicks = new ArrayDeque<>();

    public CpsWidget(int x, int y) {
        super("CPS", "Tracks your left and right clicks per second.", x, y);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            long now = System.currentTimeMillis();
            leftClicks.removeIf(t -> now - t > 1000);
            rightClicks.removeIf(t -> now - t > 1000);
        });
    }

    public void registerClick(boolean right) {
        if (right) rightClicks.add(System.currentTimeMillis());
        else leftClicks.add(System.currentTimeMillis());
    }

    @Override
    public void render(DrawContext context) {
        context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                "CPS: " + leftClicks.size() + " | R: " + rightClicks.size(),
                x, y, 0xFFFFFFFF
        );
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}