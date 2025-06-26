package de.j3ramy.edomui.theme;

import de.j3ramy.edomui.util.style.WidgetStyle;

import java.awt.*;

public class ProgressBarStyle extends WidgetStyle {
    private Color barColor = Color.BLUE;

    public Color getBarColor() {
        return barColor;
    }

    public void setBarColor(Color barColor) {
        this.barColor = barColor;
    }

    public ProgressBarStyle(WidgetStyle style) {
        super(style);
    }
}
