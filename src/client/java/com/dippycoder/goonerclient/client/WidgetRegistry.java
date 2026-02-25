package com.dippycoder.goonerclient.client;

import com.dippycoder.goonerclient.client.widgets.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class WidgetRegistry {

    // To add a new widget: just add one line here
    private static final List<Supplier<Menu.Statement>> WIDGETS = List.of(
            () -> new FpsWidget(10, 100),
            () -> new SpeedWidget(10, 112),
            () -> new CpsWidget(10, 124),
            () -> new CoordinatesWidget(10, 136),
            () -> new TimeWidget(10, 148),
            () -> new PlaytimeWidget(10, 160),
            () -> new KeystrokesWidget(10, 172),
            () -> new ReachWidget(10, 220)

    );

    public static List<Menu.Statement> createAll() {
        List<Menu.Statement> list = new ArrayList<>();
        for (Supplier<Menu.Statement> supplier : WIDGETS) {
            list.add(supplier.get());
        }
        return list;
    }
}