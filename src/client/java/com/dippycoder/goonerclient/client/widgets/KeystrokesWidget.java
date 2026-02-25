package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.RenderHelper;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class KeystrokesWidget extends Menu.Statement {

    public boolean showJump = true;
    public boolean showSprint = true;
    public boolean showSneak = true;

    private static final int KEY_SIZE = 14;
    private static final int KEY_GAP = 2;

    public KeystrokesWidget(int x, int y) {
        super("Keystrokes", "Displays your WASD keys and optionally jump, sprint and sneak.", x, y);
    }

    @Override
    public void render(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        boolean w = mc.options.forwardKey.isPressed();
        boolean s = mc.options.backKey.isPressed();
        boolean a = mc.options.leftKey.isPressed();
        boolean d = mc.options.rightKey.isPressed();
        boolean jump = mc.options.jumpKey.isPressed();
        boolean sprint = mc.options.sprintKey.isPressed();
        boolean sneak = mc.options.sneakKey.isPressed();

        int col = KEY_SIZE + KEY_GAP;

        // W centered above A S D
        drawKey(context, mc, "W", x + col, y, w);
        // A S D row
        drawKey(context, mc, "A", x, y + col, a);
        drawKey(context, mc, "S", x + col, y + col, s);
        drawKey(context, mc, "D", x + col * 2, y + col, d);

        int extraY = y + col * 2 + KEY_GAP;
        if (showJump) {
            // space bar wider
            drawWideKey(context, mc, "SPC", x, extraY, jump);
            extraY += col;
        }
        if (showSprint) {
            drawWideKey(context, mc, "SPR", x, extraY, sprint);
            extraY += col;
        }
        if (showSneak) {
            drawWideKey(context, mc, "SNK", x, extraY, sneak);
        }
    }

    private void drawKey(DrawContext context, MinecraftClient mc, String label, int kx, int ky, boolean pressed) {
        int bg = pressed ? 0xAAFFFFFF : 0xAA333333;
        int fg = pressed ? 0xFF000000 : 0xFFFFFFFF;
        context.fill(kx, ky, kx + KEY_SIZE, ky + KEY_SIZE, bg);
        RenderHelper.drawBorder(context, kx, ky, KEY_SIZE, KEY_SIZE, 0xFFAAAAAA);
        int tx = kx + (KEY_SIZE - mc.textRenderer.getWidth(label)) / 2;
        int ty = ky + (KEY_SIZE - 8) / 2;
        context.drawTextWithShadow(mc.textRenderer, label, tx, ty, fg);
    }

    private void drawWideKey(DrawContext context, MinecraftClient mc, String label, int kx, int ky, boolean pressed) {
        int w = KEY_SIZE * 3 + KEY_GAP * 2;
        int bg = pressed ? 0xAAFFFFFF : 0xAA333333;
        int fg = pressed ? 0xFF000000 : 0xFFFFFFFF;
        context.fill(kx, ky, kx + w, ky + KEY_SIZE, bg);
        RenderHelper.drawBorder(context, kx, ky, w, KEY_SIZE, 0xFFAAAAAA);
        int tx = kx + (w - mc.textRenderer.getWidth(label)) / 2;
        int ty = ky + (KEY_SIZE - 8) / 2;
        context.drawTextWithShadow(mc.textRenderer, label, tx, ty, fg);
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {
        // toggles added below - we'll hook into this once settings UI is built
    }
}