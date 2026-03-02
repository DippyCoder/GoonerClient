package com.dippycoder.goonerclient.client;

import com.dippycoder.goonerclient.client.widgets.CpsWidget;
import com.dippycoder.goonerclient.client.widgets.FullbrightWidget;
import com.dippycoder.goonerclient.client.widgets.ZoomWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;

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

        for (Menu.Statement s : menu.getStatements()) {
            if (s instanceof FullbrightWidget f) {
                FullbrightWidget.active = f.isEnabled();
            }
        }

        // save on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> WidgetStorage.save(menu)));

        HudRenderer.register();

        KeyBinding menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open GoonerClient Menu",
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

        KeyBinding zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Zoom",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                KeyBinding.Category.MISC
        ));

        AtomicBoolean wasZooming = new AtomicBoolean(false);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            ZoomWidget zoomer = null;
            for (Menu.Statement s : menu.getStatements()) {
                if (s instanceof ZoomWidget z && s.isEnabled()) { zoomer = z; break; }
            }
            boolean isZooming = zoomer != null && zoomKey.isPressed();

            if (wasZooming.get() && !isZooming) {
                ZoomWidget.onZoomStop();
            }
            wasZooming.set(isZooming);
            ZoomWidget.zooming = isZooming;
            ZoomWidget.tick();
        });

        System.out.println("Gooner Client ClientInit loaded!");
    }
}