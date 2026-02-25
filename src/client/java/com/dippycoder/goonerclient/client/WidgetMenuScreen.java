package com.dippycoder.goonerclient.client;

import com.dippycoder.goonerclient.client.ui.GuiTheme;
import com.dippycoder.goonerclient.client.ui.GuiUtils;
import com.dippycoder.goonerclient.client.ui.HoverButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WidgetMenuScreen extends Screen {

    private static final int CARD_W = 120;
    private static final int CARD_H = 50;
    private static final int CARD_GAP = 8;
    private static final int PADDING = 12;
    private static final int HEADER_H = 30;
    private static final int FOOTER_H = 30;

    private int panelX, panelY, panelW, panelH;
    private int cols;
    private int scrollOffset = 0;
    private int maxScroll = 0;
    private int contentH;

    private HoverButton closeButton;

    public WidgetMenuScreen() {
        super(Text.empty());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        panelW = Math.min(width - 40, 700);
        panelH = Math.min(height - 60, 400);
        panelX = width / 2 - panelW / 2;
        panelY = height / 2 - panelH / 2;

        cols = Math.max(1, (panelW - PADDING * 2 + CARD_GAP) / (CARD_W + CARD_GAP));

        int rows = (int) Math.ceil(GoonerclientClient.menu.getStatements().size() / (double) cols);
        contentH = rows * CARD_H + (rows - 1) * CARD_GAP;

        int visibleH = panelH - HEADER_H - FOOTER_H;
        maxScroll = Math.max(0, contentH - visibleH);
        scrollOffset = Math.min(scrollOffset, maxScroll);

        closeButton = new HoverButton(
                panelX + (panelW - 80) / 2,
                panelY + panelH - FOOTER_H + 6,
                80, 18, "Close",
                () -> MinecraftClient.getInstance().setScreen(new MenuScreen())
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // main panel
        GuiUtils.drawGlassPanel(context, panelX, panelY, panelW, panelH, GuiTheme.CORNER);

        // header
        String title = "Widgets";
        context.drawTextWithShadow(textRenderer, title,
                panelX + (panelW - textRenderer.getWidth(title)) / 2,
                panelY + 10, GuiTheme.ACCENT_BRIGHT);
        context.fill(panelX + 10, panelY + HEADER_H - 1,
                panelX + panelW - 10, panelY + HEADER_H, GuiTheme.ACCENT_DIM);

        // scissor to clip card area only
        int clipX1 = panelX + 1;
        int clipY1 = panelY + HEADER_H;
        int clipX2 = panelX + panelW - 1;
        int clipY2 = panelY + panelH - FOOTER_H;
        context.enableScissor(clipX1, clipY1, clipX2, clipY2);

        // draw cards
        List<Menu.Statement> statements = GoonerclientClient.menu.getStatements();
        for (int i = 0; i < statements.size(); i++) {
            Menu.Statement s = statements.get(i);
            int col = i % cols;
            int row = i / cols;
            int gridW = cols * CARD_W + (cols - 1) * CARD_GAP;
            int gridX = panelX + (panelW - gridW) / 2;
            int cx = gridX + col * (CARD_W + CARD_GAP);
            int cy = panelY + HEADER_H + PADDING + row * (CARD_H + CARD_GAP) - scrollOffset;
            drawWidgetCard(context, s, cx, cy, mouseX, mouseY);
        }

        context.disableScissor();

        // footer divider
        context.fill(panelX + 10, panelY + panelH - FOOTER_H,
                panelX + panelW - 10, panelY + panelH - FOOTER_H + 1, GuiTheme.ACCENT_DIM);

        // scrollbar
        if (maxScroll > 0) {
            int trackX = panelX + panelW - 6;
            int trackY1 = panelY + HEADER_H;
            int trackH = panelH - HEADER_H - FOOTER_H;
            int thumbH = Math.max(20, trackH * trackH / (contentH + PADDING * 2));
            int thumbY = trackY1 + (int)((trackH - thumbH) * ((float) scrollOffset / maxScroll));
            context.fill(trackX, trackY1, trackX + 3, trackY1 + trackH, GuiTheme.BORDER_OFF);
            context.fill(trackX, thumbY, trackX + 3, thumbY + thumbH, GuiTheme.ACCENT);
        }

        closeButton.render(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);

        // tooltip AFTER super.render so it's always on top
        List<Menu.Statement> statements2 = GoonerclientClient.menu.getStatements();
        for (int i = 0; i < statements2.size(); i++) {
            Menu.Statement s = statements2.get(i);
            int col = i % cols;
            int row = i / cols;
            int gridW = cols * CARD_W + (cols - 1) * CARD_GAP;
            int gridX = panelX + (panelW - gridW) / 2;
            int cx = gridX + col * (CARD_W + CARD_GAP);
            int cy = panelY + HEADER_H + PADDING + row * (CARD_H + CARD_GAP) - scrollOffset;

            boolean hovered = mouseX >= cx && mouseX <= cx + CARD_W
                    && mouseY >= cy && mouseY <= cy + CARD_H;
            if (hovered && s.getDescription() != null && !s.getDescription().isEmpty()) {
                drawTooltip(context, s.getDescription(), mouseX, mouseY);
                break; // only one tooltip at a time
            }
        }
    }

    private void drawWidgetCard(DrawContext context, Menu.Statement s,
                                int cx, int cy, int mouseX, int mouseY) {
        boolean on = s.isEnabled();
        boolean hovered = mouseX >= cx && mouseX <= cx + CARD_W
                && mouseY >= cy && mouseY <= cy + CARD_H;

        int bg = on ? GuiTheme.BG_WIDGET_ON : GuiTheme.BG_WIDGET_OFF;
        int border = on ? GuiTheme.BORDER_ON : (hovered ? GuiTheme.BORDER_HOVER : GuiTheme.BORDER_OFF);

        GuiUtils.drawRoundedRect(context, cx, cy, CARD_W, CARD_H, GuiTheme.CORNER, bg);
        context.fill(cx + GuiTheme.CORNER, cy + 1,
                cx + CARD_W - GuiTheme.CORNER, cy + 2, 0x18FFFFFF);
        GuiUtils.drawRoundedBorder(context, cx, cy, CARD_W, CARD_H, GuiTheme.CORNER, border);

        if (on) {
            context.fill(cx + GuiTheme.CORNER, cy,
                    cx + CARD_W - GuiTheme.CORNER, cy + 2, GuiTheme.ACCENT);
        }

        String name = s.getName();
        int tx = cx + (CARD_W - textRenderer.getWidth(name)) / 2;
        context.drawTextWithShadow(textRenderer, name, tx, cy + 12,
                on ? GuiTheme.TEXT_PRIMARY : GuiTheme.TEXT_DIM);

        String status = on ? "Enabled" : "Disabled";
        int statusColor = on ? 0xFF55FF55 : 0xFF555555;
        int stx = cx + (CARD_W - textRenderer.getWidth(status)) / 2;
        context.drawTextWithShadow(textRenderer, status, stx, cy + 26, statusColor);

        String hint = "Right click: settings";
        int htx = cx + (CARD_W - textRenderer.getWidth(hint)) / 2;
        context.drawTextWithShadow(textRenderer, hint, htx, cy + CARD_H - 12, 0xFF444466);
        // tooltip removed from here - now handled after super.render()
    }

    private void drawTooltip(DrawContext context, String text, int mouseX, int mouseY) {
        int maxLineW = 120;

        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            String test = current.isEmpty() ? word : current + " " + word;
            if (textRenderer.getWidth(test) > maxLineW) {
                if (!current.isEmpty()) lines.add(current.toString());
                current = new StringBuilder(word);
            } else {
                current = new StringBuilder(test);
            }
        }
        if (!current.isEmpty()) lines.add(current.toString());

        int lineH = 10;
        int tooltipW = 0;
        for (String line : lines) tooltipW = Math.max(tooltipW, textRenderer.getWidth(line));
        tooltipW += 10;
        int tooltipH = lines.size() * lineH + 8;

        int tx = mouseX + 6;
        int ty = mouseY - tooltipH - 4;

        if (tx + tooltipW > width - 4) tx = mouseX - tooltipW - 6;
        if (ty < 4) ty = mouseY + 10;

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0, 0);

        GuiUtils.drawRoundedRect(context, tx, ty, tooltipW, tooltipH, GuiTheme.CORNER, 0xF00D0D1A);
        GuiUtils.drawRoundedBorder(context, tx, ty, tooltipW, tooltipH, GuiTheme.CORNER, GuiTheme.ACCENT);

        for (int i = 0; i < lines.size(); i++) {
            context.drawTextWithShadow(textRenderer, lines.get(i),
                    tx + 5, ty + 4 + i * lineH, GuiTheme.TEXT_PRIMARY);
        }

        context.getMatrices().popMatrix();
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (closeButton.onClick((int) click.x(), (int) click.y())) return true;

        List<Menu.Statement> statements = GoonerclientClient.menu.getStatements();
        for (int i = 0; i < statements.size(); i++) {
            Menu.Statement s = statements.get(i);
            int col = i % cols;
            int row = i / cols;
            int gridW = cols * CARD_W + (cols - 1) * CARD_GAP;
            int gridX = panelX + (panelW - gridW) / 2;
            int cx = gridX + col * (CARD_W + CARD_GAP);
            int cy = panelY + HEADER_H + PADDING + row * (CARD_H + CARD_GAP) - scrollOffset;

            if (click.x() >= cx && click.x() <= cx + CARD_W
                    && click.y() >= cy && click.y() <= cy + CARD_H) {
                if (click.button() == 0) {
                    s.toggle();
                    WidgetStorage.save(GoonerclientClient.menu);
                    return true;
                } else if (click.button() == 1) {
                    MinecraftClient.getInstance().setScreen(new WidgetSettingsScreen(s));
                    return true;
                }
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset -= (int)(verticalAmount * 10);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
        return true;
    }
}