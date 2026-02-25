package com.dippycoder.goonerclient.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.DrawContext.HoverType;
import net.minecraft.client.gui.widget.ButtonWidget;

public class SimpleButton extends ButtonWidget {
    public SimpleButton(int x, int y, int width, int height, String label, PressAction onPress) {
        super(x, y, width, height, net.minecraft.text.Text.literal(label), onPress, narration -> net.minecraft.text.Text.literal(label));
    }

    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawButton(context);
        this.drawLabel(context.getHoverListener(this, HoverType.NONE));
    }
}