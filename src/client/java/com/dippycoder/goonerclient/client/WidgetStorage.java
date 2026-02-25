package com.dippycoder.goonerclient.client;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;

public class WidgetStorage {

    private static final Path SAVE_FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("goonerclient_widgets.json");

    public static void save(Menu menu) {
        JsonArray array = new JsonArray();
        for (Menu.Statement s : menu.getStatements()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", s.getName());
            obj.addProperty("x", s.getX());
            obj.addProperty("y", s.getY());
            obj.addProperty("enabled", s.isEnabled());
            array.add(obj);
        }
        try (Writer writer = Files.newBufferedWriter(SAVE_FILE)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(array, writer);
        } catch (IOException e) {
            System.err.println("Failed to save widget state: " + e.getMessage());
        }
    }

    public static void load(Menu menu) {
        if (!Files.exists(SAVE_FILE)) return;
        try (Reader reader = Files.newBufferedReader(SAVE_FILE)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement el : array) {
                JsonObject obj = el.getAsJsonObject();
                String name = obj.get("name").getAsString();
                int x = obj.get("x").getAsInt();
                int y = obj.get("y").getAsInt();
                boolean enabled = obj.get("enabled").getAsBoolean();
                for (Menu.Statement s : menu.getStatements()) {
                    if (s.getName().equals(name)) {
                        s.setPosition(x, y);
                        if (!enabled) s.setEnabled(false);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load widget state: " + e.getMessage());
        }
    }
}