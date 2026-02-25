package com.dippycoder.goonerclient.client;

import com.dippycoder.goonerclient.client.ui.GuiTheme;
import com.dippycoder.goonerclient.client.ui.HoverButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class MoveWidgetsScreen extends Screen {

    private Menu.Statement dragging = null;
    private int dragOffsetX, dragOffsetY;
    private static final int PADDING = 4;
    private HoverButton doneButton;

    public MoveWidgetsScreen() {
        super(Text.empty());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        doneButton = new HoverButton(
                width / 2 - 40, height - 30,
                80, 18, "Done",
                () -> {
                    WidgetStorage.save(GoonerclientClient.menu);
                    MinecraftClient.getInstance().setScreen(new MenuScreen());
                }
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x88000000);

        List<Menu.Statement> statements = GoonerclientClient.menu.getStatements();
        for (Menu.Statement s : statements) {
            if (!s.isEnabled()) continue;

            s.render(context);

            int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(s.getName() + ": 000");
            int textHeight = 9;
            RenderHelper.drawBorder(
                    context,
                    s.getX() - PADDING,
                    s.getY() - PADDING,
                    textWidth + PADDING * 2,
                    textHeight + PADDING * 2,
                    dragging == s ? GuiTheme.ACCENT_BRIGHT : GuiTheme.BORDER_OFF
            );
        }

        doneButton.render(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (doneButton.onClick((int) click.x(), (int) click.y())) return true;

        if (click.button() == 0) {
            List<Menu.Statement> statements = GoonerclientClient.menu.getStatements();
            for (Menu.Statement s : statements) {
                if (!s.isEnabled()) continue;
                int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(s.getName() + ": 000");
                int textHeight = 9;
                int bx = s.getX() - PADDING;
                int by = s.getY() - PADDING;
                int bw = textWidth + PADDING * 2;
                int bh = textHeight + PADDING * 2;

                if (click.x() >= bx && click.x() <= bx + bw
                        && click.y() >= by && click.y() <= by + bh) {
                    dragging = s;
                    dragOffsetX = (int) click.x() - s.getX();
                    dragOffsetY = (int) click.y() - s.getY();
                    return true;
                }
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (dragging != null && click.button() == 0) {
            dragging.setPosition((int) click.x() - dragOffsetX, (int) click.y() - dragOffsetY);
            return true;
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (click.button() == 0) dragging = null;
        return super.mouseReleased(click);
    }
}