package com.dippycoder.goonerclient.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class HoverButton {

    private final int x, w, h;
    private final String label;
    private final Runnable onClick;

    private float hoverProgress = 0f; // 0.0 to 1.0
    private static final float SPEED = 0.15f;
    private static final int LIFT = 3; // pixels to lift

    public HoverButton(int x, int y, int w, int h, String label, Runnable onClick) {
        this.x = x;
        this.baseY = y;
        this.w = w;
        this.h = h;
        this.label = label;
        this.onClick = onClick;
    }

    private int baseY;

    public void tick(int mouseX, int mouseY) {
        boolean hovered = isHovered(mouseX, mouseY);
        if (hovered && hoverProgress < 1f) hoverProgress = Math.min(1f, hoverProgress + SPEED);
        else if (!hovered && hoverProgress > 0f) hoverProgress = Math.max(0f, hoverProgress - SPEED);
    }

    public void render(DrawContext ctx, int mouseX, int mouseY) {
        tick(mouseX, mouseY);

        int liftY = (int)(hoverProgress * LIFT);
        int ry = baseY - liftY;

        int bgColor = hoverProgress > 0 ? GuiTheme.BTN_HOVER : GuiTheme.BTN_NORMAL;
        int borderColor = hoverProgress > 0 ? GuiTheme.BORDER_HOVER : GuiTheme.BTN_BORDER;

        GuiUtils.drawRoundedRect(ctx, x, ry, w, h, GuiTheme.CORNER, bgColor);
        // subtle gradient overlay
        ctx.fill(x + GuiTheme.CORNER, ry + 1, x + w - GuiTheme.CORNER, ry + h / 2, 0x11FFFFFF);
        GuiUtils.drawRoundedBorder(ctx, x, ry, w, h, GuiTheme.CORNER, borderColor);

        // label centered
        MinecraftClient mc = MinecraftClient.getInstance();
        int tx = x + (w - mc.textRenderer.getWidth(label)) / 2;
        int ty = ry + (h - 8) / 2;
        ctx.drawTextWithShadow(mc.textRenderer, label, tx, ty, GuiTheme.TEXT_PRIMARY);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        int liftY = (int)(hoverProgress * LIFT);
        int ry = baseY - liftY;
        return mouseX >= x && mouseX <= x + w && mouseY >= ry && mouseY <= ry + h;
    }

    public boolean onClick(int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) {
            onClick.run();
            return true;
        }
        return false;
    }
}