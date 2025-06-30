package de.j3ramy.edomui.component;

import de.j3ramy.edomui.util.style.WidgetStyle;

public abstract class CompositeWidget extends Widget {
    private boolean needsStyleSync = true;

    public CompositeWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);

        if (needsStyleSync) {
            syncChildStyles();
            needsStyleSync = false;
        }
    }

    @Override
    public void setStyle(WidgetStyle style) {
        super.setStyle(style);
        needsStyleSync = true;
    }

    protected abstract void syncChildStyles();

    public void markStylesDirty() {
        this.needsStyleSync = true;
    }
}