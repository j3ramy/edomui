package de.j3ramy.edomui.view;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewRegistry {
    private static final CopyOnWriteArrayList<View> views = new CopyOnWriteArrayList<>();

    public static void registerView(View view) {
        if (view != null) {
            views.add(view);
        }
    }

    public static void unregisterView(View view) {
        if (view != null) {
            views.remove(view);
        }
    }

    public static List<View> getRegisteredViews() {
        return List.copyOf(views);
    }
}
