package de.j3ramy.edomui.theme.chart;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.WidgetStyle;

public abstract class ChartStyle extends WidgetStyle {
    private int xAxisLabelOffset = 5;
    private int yAxisLabelOffset = -15;
    private int labelColor = Color.BLACK;
    private FontSize fontSize = FontSize.XS;

    public int getXAxisLabelOffset() {
        return xAxisLabelOffset;
    }

    public int getYAxisLabelOffset() {
        return yAxisLabelOffset;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setXAxisLabelOffset(int xAxisLabelOffset) {
        this.xAxisLabelOffset = xAxisLabelOffset;
    }

    public void setYAxisLabelOffset(int yAxisLabelOffset) {
        this.yAxisLabelOffset = yAxisLabelOffset;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public ChartStyle(WidgetStyle other) {
        super(other);
    }
}
