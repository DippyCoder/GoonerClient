package com.dippycoder.goonerclient.client;

import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein Menu, das Widgets verwaltet.
 * Widgets sind "Statements" – kleine Klassen, die renderbar sind.
 */
public class Menu {

    private final List<Statement> statements;

    public Menu() {
        statements = new ArrayList<>();
    }

    public void addStatement(Statement s) {
        statements.add(s);
    }

    public void removeStatement(Statement s) {
        statements.remove(s);
    }

    public List<Statement> getStatements() {
        return statements;
    }

    // Wird vom HUD gerendert
    public void renderAll(DrawContext context) {
        for (Statement s : statements) {
            if (s.isEnabled()) {
                s.render(context);
            }
        }
    }

    // Statement Interface
    public static abstract class Statement {
        protected String name;
        protected String description;
        protected boolean enabled = true;
        protected int x, y;

        public Statement(String name, String description, int x, int y) {
            this.name = name;
            this.description = description;
            this.x = x;
            this.y = y;
        }

        public String getDescription() { return description; }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public abstract void render(DrawContext context);

        public void toggle() {
            enabled = !enabled;
        }

        public boolean isEnabled() { return enabled; }
        public String getName() { return name; }
        public int getX() { return x; }
        public int getY() { return y; }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void buildSettingsScreen(WidgetSettingsScreen screen) {
            // default: no settings
        }
    }
}