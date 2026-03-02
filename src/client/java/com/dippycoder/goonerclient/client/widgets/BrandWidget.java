package com.dippycoder.goonerclient.client.widgets;

import com.dippycoder.goonerclient.client.Menu;
import com.dippycoder.goonerclient.client.WidgetSettingsScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class BrandWidget extends Menu.Statement {

    public static final Identifier ICON =
            Identifier.of("goonerclient", "textures/gui/icon.png");

    public static boolean active = false;

    public BrandWidget(int x, int y) {
        super("Brand Icons", "Shows the GoonerClient icon above your head, in tablist and chat. (Work in Progress)", "Utility", x, y);
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
    public void render(DrawContext context) {}

    @Override
    public void buildSettingsScreen(WidgetSettingsScreen screen) {}
}