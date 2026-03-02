package com.dippycoder.goonerclient.client;

import com.dippycoder.goonerclient.client.ui.GuiTheme;
import com.dippycoder.goonerclient.client.ui.GuiUtils;
import com.dippycoder.goonerclient.client.ui.HoverButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.stream.Collectors;

public class WidgetMenuScreen extends Screen {

    private static final int CARD_W = 120;
    private static final int CARD_H = 50;
    private static final int CARD_GAP = 8;
    private static final int PADDING = 12;
    private static final int HEADER_H = 56; // title + search bar
    private static final int FOOTER_H = 30;
    private static final int SEARCH_H = 16;
    private static final int BTN_SIZE = 16; // square layout button

    private boolean useCategoryLayout = true;
    private static final int CAT_LABEL_H = 20;
    private static final List<String> CATEGORY_ORDER = List.of("World", "Counters", "Utility");

    private int panelX, panelY, panelW, panelH;
    private int cols;
    private int scrollOffset = 0;
    private int maxScroll = 0;

    private HoverButton closeButton;
    private String searchQuery = "";
    private boolean searchFocused = false;
    private int searchX, searchY, searchW;

    // cursor blink
    private long cursorTimer = 0;

    public WidgetMenuScreen() {
        super(Text.empty());
    }

    private static final Identifier LAYOUT_ICON =
            Identifier.of("goonerclient", "textures/gui/layout_icon.png");

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

        searchW = panelW - PADDING * 2 - BTN_SIZE - CARD_GAP;
        searchX = panelX + PADDING;
        searchY = panelY + 36;

        recalcScroll();

        closeButton = new HoverButton(
                panelX + (panelW - 80) / 2,
                panelY + panelH - FOOTER_H + 6,
                80, 18, "Close",
                () -> MinecraftClient.getInstance().setScreen(new MenuScreen())
        );
    }

    private void recalcScroll() {
        int totalH = useCategoryLayout
                ? calcTotalContentHeight(getFilteredByCategory())
                : calcFlatContentHeight(getFilteredFlat());
        int visibleH = panelH - HEADER_H - FOOTER_H;
        maxScroll = Math.max(0, totalH - visibleH);
        scrollOffset = Math.min(scrollOffset, maxScroll);
    }

    // returns map of category -> filtered widgets
    private LinkedHashMap<String, List<Menu.Statement>> getFilteredByCategory() {
        List<Menu.Statement> all = GoonerclientClient.menu.getStatements();
        LinkedHashMap<String, List<Menu.Statement>> result = new LinkedHashMap<>();

        for (String cat : CATEGORY_ORDER) {
            List<Menu.Statement> filtered = all.stream()
                    .filter(s -> cat.equals(s.getCategory()))
                    .filter(s -> searchQuery.isEmpty() ||
                            s.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
            if (!filtered.isEmpty()) result.put(cat, filtered);
        }

        // catch any widgets not in CATEGORY_ORDER
        List<Menu.Statement> uncategorized = all.stream()
                .filter(s -> !CATEGORY_ORDER.contains(s.getCategory()))
                .filter(s -> searchQuery.isEmpty() ||
                        s.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());
        if (!uncategorized.isEmpty()) result.put("Other", uncategorized);

        return result;
    }

    private int calcTotalContentHeight(LinkedHashMap<String, List<Menu.Statement>> categorized) {
        int total = 0;
        for (Map.Entry<String, List<Menu.Statement>> entry : categorized.entrySet()) {
            total += CAT_LABEL_H; // category header
            int rows = (int) Math.ceil(entry.getValue().size() / (double) cols);
            total += rows * CARD_H + (rows - 1) * CARD_GAP + CARD_GAP;
        }
        return total;
    }

    private void drawLayoutButton(DrawContext context, int mouseX, int mouseY) {
        int bx = searchX + searchW + CARD_GAP;
        int by = searchY;
        boolean hovered = mouseX >= bx && mouseX <= bx + BTN_SIZE
                && mouseY >= by && mouseY <= by + BTN_SIZE;

        int bg = useCategoryLayout ? GuiTheme.BG_WIDGET_ON : GuiTheme.BG_WIDGET_OFF;
        int border = useCategoryLayout ? GuiTheme.BORDER_ON : (hovered ? GuiTheme.BORDER_HOVER : GuiTheme.BORDER_OFF);

        // button background + border
        GuiUtils.drawRoundedRect(context, bx, by, BTN_SIZE, BTN_SIZE, GuiTheme.CORNER, bg);
        GuiUtils.drawRoundedBorder(context, bx, by, BTN_SIZE, BTN_SIZE, GuiTheme.CORNER, border);

        // icon centered inside button (icon is 10x10 inside the 16x16 button)
        int iconSize = 10;
        int iconX = bx + (BTN_SIZE - iconSize) / 2;
        int iconY = by + (BTN_SIZE - iconSize) / 2;
        context.drawTexture(
                net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED,
                LAYOUT_ICON,
                iconX, iconY,
                0f, 0f,
                iconSize, iconSize,
                iconSize, iconSize
        );
    }

    private List<Menu.Statement> getFilteredFlat() {
        return GoonerclientClient.menu.getStatements().stream()
                .filter(s -> searchQuery.isEmpty() ||
                        s.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());
    }

    private int calcFlatContentHeight(List<Menu.Statement> filtered) {
        int rows = (int) Math.ceil(filtered.size() / (double) cols);
        return rows * CARD_H + (rows - 1) * CARD_GAP;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        GuiUtils.drawGlassPanel(context, panelX, panelY, panelW, panelH, GuiTheme.CORNER);

        // title
        String title = "Widgets";
        context.drawTextWithShadow(textRenderer, title,
                panelX + (panelW - textRenderer.getWidth(title)) / 2,
                panelY + 10, GuiTheme.ACCENT_BRIGHT);

        // divider UNDER title, ABOVE search
        context.fill(panelX + 10, panelY + 24,
                panelX + panelW - 10, panelY + 25, GuiTheme.ACCENT_DIM);

        // search bar
        drawSearchBar(context, mouseX, mouseY);

        // layout toggle button
        drawLayoutButton(context, mouseX, mouseY);

        // scissor clip
        context.enableScissor(panelX + 1, panelY + HEADER_H,
                panelX + panelW - 1, panelY + panelH - FOOTER_H);

        String hoveredDesc = null;

        if (useCategoryLayout) {
            // categories
            LinkedHashMap<String, List<Menu.Statement>> categorized = getFilteredByCategory();
            int drawY = panelY + HEADER_H + PADDING - scrollOffset;
            int gridW = cols * CARD_W + (cols - 1) * CARD_GAP;
            int gridX = panelX + (panelW - gridW) / 2;

            for (Map.Entry<String, List<Menu.Statement>> entry : categorized.entrySet()) {
                drawCategoryLabel(context, entry.getKey(), panelX + PADDING, drawY);
                drawY += CAT_LABEL_H;

                List<Menu.Statement> widgets = entry.getValue();
                for (int i = 0; i < widgets.size(); i++) {
                    Menu.Statement s = widgets.get(i);
                    int col = i % cols;
                    int row = i / cols;
                    int cx = gridX + col * (CARD_W + CARD_GAP);
                    int cy = drawY + row * (CARD_H + CARD_GAP);
                    drawWidgetCard(context, s, cx, cy, mouseX, mouseY);

                    boolean hovered = mouseX >= cx && mouseX <= cx + CARD_W
                            && mouseY >= cy && mouseY <= cy + CARD_H;
                    if (hovered && s.getDescription() != null && !s.getDescription().isEmpty())
                        hoveredDesc = s.getDescription();
                }
                int rows = (int) Math.ceil(widgets.size() / (double) cols);
                drawY += rows * CARD_H + (rows - 1) * CARD_GAP + CARD_GAP;
            }
        } else {
            // flat layout - original order
            List<Menu.Statement> filtered = getFilteredFlat();
            int gridW = cols * CARD_W + (cols - 1) * CARD_GAP;
            int gridX = panelX + (panelW - gridW) / 2;

            for (int i = 0; i < filtered.size(); i++) {
                Menu.Statement s = filtered.get(i);
                int col = i % cols;
                int row = i / cols;
                int cx = gridX + col * (CARD_W + CARD_GAP);
                int cy = panelY + HEADER_H + PADDING + row * (CARD_H + CARD_GAP) - scrollOffset;
                drawWidgetCard(context, s, cx, cy, mouseX, mouseY);

                boolean hovered = mouseX >= cx && mouseX <= cx + CARD_W
                        && mouseY >= cy && mouseY <= cy + CARD_H;
                if (hovered && s.getDescription() != null && !s.getDescription().isEmpty())
                    hoveredDesc = s.getDescription();
            }
        }

        context.disableScissor();

        // footer
        context.fill(panelX + 10, panelY + panelH - FOOTER_H,
                panelX + panelW - 10, panelY + panelH - FOOTER_H + 1, GuiTheme.ACCENT_DIM);

        // scrollbar
        if (maxScroll > 0) {
            int totalH = useCategoryLayout
                    ? calcTotalContentHeight(getFilteredByCategory())
                    : calcFlatContentHeight(getFilteredFlat());
            int trackX = panelX + panelW - 6;
            int trackY1 = panelY + HEADER_H;
            int trackH = panelH - HEADER_H - FOOTER_H;
            int thumbH = Math.max(20, trackH * trackH / (totalH + PADDING * 2));
            int thumbY = trackY1 + (int)((trackH - thumbH) * ((float) scrollOffset / maxScroll));
            context.fill(trackX, trackY1, trackX + 3, trackY1 + trackH, GuiTheme.BORDER_OFF);
            context.fill(trackX, thumbY, trackX + 3, thumbY + thumbH, GuiTheme.ACCENT);
        }

        closeButton.render(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);

        if (hoveredDesc != null) drawTooltip(context, hoveredDesc, mouseX, mouseY);
    }

    private void drawCategoryLabel(DrawContext context, String cat, int x, int y) {
        int lineY = y + 9;
        int labelW = textRenderer.getWidth(cat.toUpperCase());

        // left line
        context.fill(x, lineY, x + (panelW - PADDING * 2 - labelW - 8) / 2, lineY + 1, GuiTheme.ACCENT_DIM);
        // label
        context.drawTextWithShadow(textRenderer, cat.toUpperCase(),
                x + (panelW - PADDING * 2 - labelW) / 2, y, GuiTheme.TEXT_DIM);
        // right line
        int rightX = x + (panelW - PADDING * 2 + labelW + 8) / 2;
        context.fill(rightX, lineY, x + panelW - PADDING * 2 - 6, lineY + 1, GuiTheme.ACCENT_DIM);
    }

    private void drawSearchBar(DrawContext context, int mouseX, int mouseY) {
        boolean hovered = mouseX >= searchX && mouseX <= searchX + searchW
                && mouseY >= searchY && mouseY <= searchY + SEARCH_H;
        int border = searchFocused ? GuiTheme.ACCENT : (hovered ? GuiTheme.BORDER_HOVER : GuiTheme.BORDER_OFF);

        GuiUtils.drawRoundedRect(context, searchX, searchY, searchW, SEARCH_H, GuiTheme.CORNER, 0xCC050510);
        GuiUtils.drawRoundedBorder(context, searchX, searchY, searchW, SEARCH_H, GuiTheme.CORNER, border);

        String display = searchQuery.isEmpty() && !searchFocused ? "Search widgets..." : searchQuery;
        int textColor = searchQuery.isEmpty() && !searchFocused ? GuiTheme.TEXT_DIM : GuiTheme.TEXT_PRIMARY;

        // cursor blink
        String displayWithCursor = display;
        if (searchFocused) {
            long t = System.currentTimeMillis();
            if ((t / 500) % 2 == 0) displayWithCursor = display + "|";
        }

        context.drawTextWithShadow(textRenderer, displayWithCursor,
                searchX + 5, searchY + (SEARCH_H - 8) / 2, textColor);
    }

    private void drawWidgetCard(DrawContext context, Menu.Statement s,
                                int cx, int cy, int mouseX, int mouseY) {
        boolean on = s.isEnabled();
        boolean hovered = mouseX >= cx && mouseX <= cx + CARD_W
                && mouseY >= cy && mouseY <= cy + CARD_H;

        int bg = on ? GuiTheme.BG_WIDGET_ON : GuiTheme.BG_WIDGET_OFF;
        int border = on ? GuiTheme.BORDER_ON : (hovered ? GuiTheme.BORDER_HOVER : GuiTheme.BORDER_OFF);

        GuiUtils.drawRoundedRect(context, cx, cy, CARD_W, CARD_H, GuiTheme.CORNER, bg);
        GuiUtils.drawRoundedBorder(context, cx, cy, CARD_W, CARD_H, GuiTheme.CORNER, border);

        if (on) {
            context.fill(cx + GuiTheme.CORNER, cy,
                    cx + CARD_W - GuiTheme.CORNER, cy, GuiTheme.ACCENT);
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

        GuiUtils.drawRoundedRect(context, tx, ty, tooltipW, tooltipH, GuiTheme.CORNER, 0xF00D0D1A);
        GuiUtils.drawRoundedBorder(context, tx, ty, tooltipW, tooltipH, GuiTheme.CORNER, GuiTheme.ACCENT);
        for (int i = 0; i < lines.size(); i++) {
            context.drawTextWithShadow(textRenderer, lines.get(i),
                    tx + 5, ty + 4 + i * lineH, GuiTheme.TEXT_PRIMARY);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        // layout button click
        int bx = searchX + searchW + CARD_GAP;
        int by = searchY;
        if (click.x() >= bx && click.x() <= bx + BTN_SIZE
                && click.y() >= by && click.y() <= by + BTN_SIZE) {
            useCategoryLayout = !useCategoryLayout;
            recalcScroll();
            return true;
        }

        // search bar focus
        if (click.x() >= searchX && click.x() <= searchX + searchW
                && click.y() >= searchY && click.y() <= searchY + SEARCH_H) {
            searchFocused = true;
            return true;
        }
        searchFocused = false;

        if (closeButton.onClick((int) click.x(), (int) click.y())) return true;

        if (useCategoryLayout) {
            LinkedHashMap<String, List<Menu.Statement>> categorized = getFilteredByCategory();
            int drawY = panelY + HEADER_H + PADDING - scrollOffset;
            int gridW = cols * CARD_W + (cols - 1) * CARD_GAP;
            int gridX = panelX + (panelW - gridW) / 2;

            for (Map.Entry<String, List<Menu.Statement>> entry : categorized.entrySet()) {
                drawY += CAT_LABEL_H;
                List<Menu.Statement> widgets = entry.getValue();
                for (int i = 0; i < widgets.size(); i++) {
                    Menu.Statement s = widgets.get(i);
                    int col = i % cols;
                    int row = i / cols;
                    int cx = gridX + col * (CARD_W + CARD_GAP);
                    int cy = drawY + row * (CARD_H + CARD_GAP);

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
                int rows = (int) Math.ceil(widgets.size() / (double) cols);
                drawY += rows * CARD_H + (rows - 1) * CARD_GAP + CARD_GAP;
            }
        } else {
            List<Menu.Statement> filtered = getFilteredFlat();
            int gridW = cols * CARD_W + (cols - 1) * CARD_GAP;
            int gridX = panelX + (panelW - gridW) / 2;

            for (int i = 0; i < filtered.size(); i++) {
                Menu.Statement s = filtered.get(i);
                int col = i % cols;
                int row = i / cols;
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
        }

        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (searchFocused) {
            searchQuery += input.asString();
            recalcScroll();
            return true;
        }
        return super.charTyped(input);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (searchFocused) {
            if (input.key() == 259 && !searchQuery.isEmpty()) { // backspace
                searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
                recalcScroll();
                return true;
            }
            if (input.isEscape()) {
                searchFocused = false;
                return true;
            }
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset -= (int)(verticalAmount * 10);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
        return true;
    }
}