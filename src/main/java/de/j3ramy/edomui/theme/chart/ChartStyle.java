package de.j3ramy.edomui.theme.chart;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public abstract class ChartStyle extends WidgetStyle {
    private int xAxisLabelOffset = 5;
    private int yAxisLabelOffset = -15;
    private Color labelColor = Color.BLACK;
    private FontSize fontSize = FontSize.XS;

    public int getXAxisLabelOffset() {
        return xAxisLabelOffset;
    }

    public int getYAxisLabelOffset() {
        return yAxisLabelOffset;
    }

    public Color getLabelColor() {
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

    public void setLabelColor(Color labelColor) {
        this.labelColor = labelColor;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public ChartStyle(WidgetStyle other) {
        super(other);

        if (other instanceof ChartStyle chartStyle) {
            this.xAxisLabelOffset = chartStyle.xAxisLabelOffset;
            this.yAxisLabelOffset = chartStyle.yAxisLabelOffset;
            this.labelColor = chartStyle.labelColor;
            this.fontSize = chartStyle.fontSize;
        }
    }
}
