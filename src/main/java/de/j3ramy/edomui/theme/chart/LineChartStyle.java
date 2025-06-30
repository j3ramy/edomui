package de.j3ramy.edomui.theme.chart;

import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public class LineChartStyle extends ChartStyle {
    private int dataPointRadius = 2;
    private int lineThickness = 1;
    private Color lineColor = Color.BLACK;

    public int getDataPointRadius() {
        return dataPointRadius;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setDataPointRadius(int dataPointRadius) {
        this.dataPointRadius = dataPointRadius;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    public LineChartStyle(WidgetStyle style) {
        super(style);

        if (style instanceof LineChartStyle other) {
            this.dataPointRadius = other.dataPointRadius;
            this.lineThickness = other.lineThickness;
            this.lineColor = other.lineColor;
        }
    }
}
