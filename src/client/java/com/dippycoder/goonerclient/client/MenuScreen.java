package com.dippycoder.goonerclient.client;

import com.dippycoder.goonerclient.client.ui.GuiTheme;
import com.dippycoder.goonerclient.client.ui.GuiUtils;
import com.dippycoder.goonerclient.client.ui.HoverButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends Screen {

    private static final int PANEL_W = 200;
    private static final int BANNER_H = 60;
    private static final int BTN_GAP = 8;
    private static final int PANEL_H = BANNER_H + BTN_GAP + GuiTheme.BTN_H + BTN_GAP + GuiTheme.BTN_H + BTN_GAP;

    // placeholder texture - replace with your own later
    private static final Identifier BANNER_TEXTURE =
            Identifier.of("goonerclient", "textures/gui/banner.png");

    private final List<HoverButton> buttons = new ArrayList<>();

    public MenuScreen() {
        super(Text.empty());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        buttons.clear();
        int panelX = width / 2 - PANEL_W / 2;
        int panelY = height / 2 - PANEL_H / 2;
        int btnX = panelX + (PANEL_W - GuiTheme.BTN_W) / 2;
        int btn1Y = panelY + BANNER_H + BTN_GAP;
        int btn2Y = btn1Y + GuiTheme.BTN_H + BTN_GAP;

        buttons.add(new HoverButton(btnX, btn1Y, GuiTheme.BTN_W, GuiTheme.BTN_H,
                "Widget Menu", () -> MinecraftClient.getInstance().setScreen(new WidgetMenuScreen())));
        buttons.add(new HoverButton(btnX, btn2Y, GuiTheme.BTN_W, GuiTheme.BTN_H,
                "Move Widgets", () -> MinecraftClient.getInstance().setScreen(new MoveWidgetsScreen())));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int panelX = width / 2 - PANEL_W / 2;
        int panelY = height / 2 - PANEL_H / 2;

        // main panel
        GuiUtils.drawGlassPanel(context, panelX, panelY, PANEL_W, PANEL_H, GuiTheme.CORNER);

        // banner section (placeholder purple gradient)
        context.fill(panelX + GuiTheme.CORNER, panelY,
                panelX + PANEL_W - GuiTheme.CORNER, panelY + BANNER_H, 0xFF1A0A2E);
        context.fill(panelX + GuiTheme.CORNER, panelY,
                panelX + PANEL_W - GuiTheme.CORNER, panelY + BANNER_H / 2, 0x22AA55FF);
        // placeholder label - swap with actual texture later
        String placeholder = "[ Banner Image ]";
        int tx = panelX + (PANEL_W - textRenderer.getWidth(placeholder)) / 2;
        context.drawTextWithShadow(textRenderer, placeholder,
                tx, panelY + BANNER_H / 2 - 4, GuiTheme.ACCENT_BRIGHT);

        // divider line
        context.fill(panelX + 10, panelY + BANNER_H,
                panelX + PANEL_W - 10, panelY + BANNER_H + 1, GuiTheme.ACCENT_DIM);

        // buttons
        for (HoverButton btn : buttons) {
            btn.render(context, mouseX, mouseY);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        for (HoverButton btn : buttons) {
            if (btn.onClick((int) click.x(), (int) click.y())) return true;
        }
        return super.mouseClicked(click, doubled);
    }
}