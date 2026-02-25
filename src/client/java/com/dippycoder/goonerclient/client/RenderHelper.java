package com.dippycoder.goonerclient.client;

import net.minecraft.client.gui.DrawContext;

public class RenderHelper {
    public static void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
        context.drawStrokedRectangle(x, y, width, height, color);
    }
}