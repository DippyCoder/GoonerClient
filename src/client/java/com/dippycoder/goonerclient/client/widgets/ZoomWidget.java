package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ZoomWidget extends Menu.Statement {

    /*
     * Zoom implementation inspired by Wi Zoom by Alexander01998
     * https://github.com/Wi1mer/WiZoom
     * Licensed under GPL-3.0
     */

    public static boolean zooming = false;
    public static final float DEFAULT_FOV = 70f;
    public static float ZOOM_FOV = 15f;
    public static float prevFov = DEFAULT_FOV;
    public static float currentFov = DEFAULT_FOV;
    private static final float LERP_SPEED = 1f;
    private static final float MIN_FOV = 1f;
    private static final float MAX_FOV = 60f;
    private static final float RESET_FOV = 15f;

    public ZoomWidget(int x, int y) {
        super("Zoom", "Allows you to zoom.", "Utility", x, y);
    }

    public static void scroll(double amount) {
        if (!zooming) return;
        float factor = amount > 0 ? 0.9f : 1.1f; // zoom in = smaller FOV, zoom out = larger FOV
        ZOOM_FOV = (float) MathHelper.clamp(ZOOM_FOV * factor, MIN_FOV, DEFAULT_FOV);
    }

    public static void tick() {
        prevFov = currentFov;
        float target = zooming ? ZOOM_FOV : DEFAULT_FOV;
        currentFov = currentFov + (target - currentFov) * LERP_SPEED;
        if (Math.abs(currentFov - target) < 0.05f) currentFov = target;
    }

    public static String getZoomMultiplier() {
        float clamped = MathHelper.clamp(currentFov, MIN_FOV, DEFAULT_FOV);
        float multiplier = DEFAULT_FOV / clamped;
        return String.format("%.1fx", multiplier);
    }

    @Override
    public void render(DrawContext context) {
        if (zooming) {
            MinecraftClient mc = MinecraftClient.getInstance();
            mc.inGameHud.setOverlayMessage(Text.literal("Zoom " + getZoomMultiplier()), false);
        }
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}

    public static void onZoomStop() {
        ZOOM_FOV = RESET_FOV; // reset zoom level when C is released
    }
}