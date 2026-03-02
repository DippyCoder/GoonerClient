package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public class PingWidget extends Menu.Statement {

    public PingWidget(int x, int y) {
        super("Ping", "Shows your current server ping.", "Counters", x, y);
    }

    @Override
    public void render(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        PlayerListEntry entry = mc.getNetworkHandler()
                .getPlayerListEntry(mc.player.getUuid());
        if (entry == null) return;

        int ping = entry.getLatency();
        int color = ping < 80 ? 0xFF55FF55
                : ping < 150 ? 0xFFFFFF55
                : ping < 300 ? 0xFFFF8800
                : 0xFFFF5555;

        context.drawTextWithShadow(mc.textRenderer,
                "Ping: " + ping + "ms", x, y, color);
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}