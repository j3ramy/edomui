package de.j3ramy.edomui.theme.chart;

import de.j3ramy.edomui.util.style.WidgetStyle;

import java.awt.*;

public class BarChartStyle extends ChartStyle {
    private int minBarHeight = 2;
    private Color barColor = Color.BLACK;
    private int barSpacing = 5;

    public int getMinBarHeight() {
        return minBarHeight;
    }

    public Color getBarColor() {
        return barColor;
    }

    public int getBarSpacing() {
        return barSpacing;
    }

    public void setMinBarHeight(int minBarHeight) {
        this.minBarHeight = minBarHeight;
    }

    public void setBarColor(Color barColor) {
        this.barColor = barColor;
    }

    public void setBarSpacing(int barSpacing) {
        this.barSpacing = barSpacing;
    }

    public BarChartStyle(WidgetStyle style) {
        super(style);
    }
}
