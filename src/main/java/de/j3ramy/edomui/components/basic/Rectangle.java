package de.j3ramy.edomui.components.basic;

import de.j3ramy.edomui.components.Widget;

import java.awt.*;

public final class Rectangle extends Widget {

    public Rectangle(int x, int y, int width, int height, Color color) {
        this(x, y, width, height, color, false);
    }

    public Rectangle(int x, int y, int width, int height, Color color, boolean showBorder) {
        super(x, y, width, height);
        this.getStyle().setBackgroundColor(color);
        if (!showBorder) {
            this.noBorder();
        }
    }
}
