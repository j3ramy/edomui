package de.j3ramy.edomui.theme.basic;

import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public class ProgressBarStyle extends WidgetStyle {
    private Color barColor = Color.BLACK;

    public Color getBarColor() {
        return barColor;
    }

    public void setBarColor(Color barColor) {
        this.barColor = barColor;
    }

    public ProgressBarStyle(WidgetStyle style) {
        super(style);

        if (style instanceof ProgressBarStyle other) {
            this.barColor = other.barColor;
        }
    }
}
