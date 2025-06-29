package de.j3ramy.edomui.component;

public abstract class CompositeWidget extends Widget {
    public CompositeWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);

        syncChildStyles();
    }

    protected abstract void syncChildStyles();
}
