package com.dippycoder.goonerclient.client;

import com.dippycoder.goonerclient.client.ui.GuiTheme;
import com.dippycoder.goonerclient.client.ui.GuiUtils;
import com.dippycoder.goonerclient.client.ui.HoverButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class WidgetSettingsScreen extends Screen {

    private final Menu.Statement statement;
    private HoverButton backButton;

    public WidgetSettingsScreen(Menu.Statement statement) {
        super(Text.empty());
        this.statement = statement;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        backButton = new HoverButton(
                width / 2 - 40, height / 2 + 50,
                80, 18, "Back",
                () -> MinecraftClient.getInstance().setScreen(new WidgetMenuScreen())
        );
        statement.buildSettingsScreen(this);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int panelW = 220, panelH = 160;
        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2;

        GuiUtils.drawGlassPanel(context, panelX, panelY, panelW, panelH, GuiTheme.CORNER);
        context.drawCenteredTextWithShadow(textRenderer,
                statement.getName() + " Settings",
                width / 2, panelY + 8, GuiTheme.ACCENT_BRIGHT);

        // divider
        context.fill(panelX + 10, panelY + 18,
                panelX + panelW - 10, panelY + 19, GuiTheme.ACCENT_DIM);

        backButton.render(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (backButton.onClick((int) click.x(), (int) click.y())) return true;
        return super.mouseClicked(click, doubled);
    }
}