package com.dippycoder.goonerclient.client.ui;

import com.dippycoder.goonerclient.client.RenderHelper;
import net.minecraft.client.gui.DrawContext;

public class GuiUtils {

    // Filled rectangle with faked rounded corners (cuts corner pixels)
    public static void drawRoundedRect(DrawContext ctx, int x, int y, int w, int h, int c, int color) {
        // main fill
        ctx.fill(x + c, y,     x + w - c, y + h,     color);
        ctx.fill(x,     y + c, x + c,     y + h - c, color);
        ctx.fill(x + w - c, y + c, x + w, y + h - c, color);
    }

    // Border for rounded rect
    public static void drawRoundedBorder(DrawContext ctx, int x, int y, int w, int h, int c, int color) {
        // top/bottom skipping corners
        ctx.fill(x + c, y,         x + w - c, y + 1,     color);
        ctx.fill(x + c, y + h - 1, x + w - c, y + h,     color);
        // left/right skipping corners
        ctx.fill(x,         y + c, x + 1,     y + h - c, color);
        ctx.fill(x + w - 1, y + c, x + w,     y + h - c, color);
        // corner pixels diagonal
        ctx.fill(x + 1,     y + 1,     x + c,     y + c,     color);
        ctx.fill(x + w - c, y + 1,     x + w - 1, y + c,     color);
        ctx.fill(x + 1,     y + h - c, x + c,     y + h - 1, color);
        ctx.fill(x + w - c, y + h - c, x + w - 1, y + h - 1, color);
    }

    // Glass-style panel: dark fill + subtle top highlight line
    public static void drawGlassPanel(DrawContext ctx, int x, int y, int w, int h, int c) {
        drawRoundedRect(ctx, x, y, w, h, c, GuiTheme.BG_PANEL);
        // top highlight shimmer
        ctx.fill(x + c, y + 1, x + w - c, y + 2, 0x22FFFFFF);
        drawRoundedBorder(ctx, x, y, w, h, c, GuiTheme.BORDER_OFF);
    }

    // Accent-bordered glass panel
    public static void drawAccentPanel(DrawContext ctx, int x, int y, int w, int h, int c, int borderColor) {
        drawRoundedRect(ctx, x, y, w, h, c, GuiTheme.BG_PANEL);
        ctx.fill(x + c, y + 1, x + w - c, y + 2, 0x22FFFFFF);
        drawRoundedBorder(ctx, x, y, w, h, c, borderColor);
    }
}