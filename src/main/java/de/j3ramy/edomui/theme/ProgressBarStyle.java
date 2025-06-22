package de.j3ramy.edomui.theme;

import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class ProgressBarStyle extends WidgetStyle {
    private int barColor = Color.BLUE;

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public ProgressBarStyle(WidgetStyle style) {
        super(style);
    }
}
