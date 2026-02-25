package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;

public class ReachWidget extends Menu.Statement {

    private double lastReach = 0;
    private long lastHitTime = 0;
    private static final long DISPLAY_DURATION = 3000; // show for 3 seconds after hit

    public ReachWidget(int x, int y) {
        super("Reach", "Shows the distance to the last entity you hit.", x, y);
    }

    public void onHit(Entity target) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        lastReach = mc.player.distanceTo(target);
        lastHitTime = System.currentTimeMillis();
    }

    @Override
    public void render(DrawContext context) {
        if (System.currentTimeMillis() - lastHitTime > DISPLAY_DURATION) return;
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                String.format("Reach: %.2f", lastReach),
                x, y, 0xFFFFFFFF);
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}