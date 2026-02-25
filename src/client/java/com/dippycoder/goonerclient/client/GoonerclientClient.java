package com.dippycoder.goonerclient.client;

import com.dippycoder.goonerclient.client.widgets.CpsWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class GoonerclientClient implements ClientModInitializer {

    public static Menu menu;
    public static CpsWidget cpsWidget;

    @Override
    public void onInitializeClient() {
        menu = new Menu();

        // register all widgets
        for (Menu.Statement s : WidgetRegistry.createAll()) {
            menu.addStatement(s);
            if (s instanceof CpsWidget cps) {
                cpsWidget = cps;
            }
        }

        // load saved positions/state
        WidgetStorage.load(menu);

        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (cpsWidget == null) return;
            if (client.mouse.wasLeftButtonClicked()) cpsWidget.registerClick(false);
            if (client.mouse.wasRightButtonClicked()) cpsWidget.registerClick(true);
        });

        // save on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> WidgetStorage.save(menu)));

        HudRenderer.register();

        KeyBinding menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.goonerclient.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (menuKey.wasPressed()) {
                if (client.currentScreen instanceof MenuScreen) {
                    client.setScreen(null);
                } else {
                    client.setScreen(new MenuScreen());
                }
            }
        });

        System.out.println("Gooner Client ClientInit geladen!");
    }
}