package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.gui.DrawContext;

public class FullbrightWidget extends Menu.Statement {

    public static boolean active = false;

    public FullbrightWidget(int x, int y) {
        super("Fullbright", "Gives you Night Vision.", "Utility", x, y);
    }

    @Override
    public void render(DrawContext context) {
        // no HUD display needed, purely functional
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        active = enabled;
    }

    @Override
    public void toggle() {
        super.toggle();
        active = isEnabled();
    }

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}