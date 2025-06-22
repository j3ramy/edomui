package de.j3ramy.edomui.theme.chart;

import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class BarChartStyle extends ChartStyle {
    private int minBarHeight = 2;
    private int barColor = Color.BLACK;
    private int barSpacing = 5;

    public int getMinBarHeight() {
        return minBarHeight;
    }

    public int getBarColor() {
        return barColor;
    }

    public int getBarSpacing() {
        return barSpacing;
    }

    public void setMinBarHeight(int minBarHeight) {
        this.minBarHeight = minBarHeight;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public void setBarSpacing(int barSpacing) {
        this.barSpacing = barSpacing;
    }

    public BarChartStyle(WidgetStyle style) {
        super(style);
    }
}
