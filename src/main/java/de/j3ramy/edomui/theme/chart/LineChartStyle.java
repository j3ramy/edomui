package de.j3ramy.edomui.theme.chart;

import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class LineChartStyle extends ChartStyle {
    private int dataPointRadius = 2;
    private int lineThickness = 1;
    private int lineColor = Color.BLACK;

    public int getDataPointRadius() {
        return dataPointRadius;
    }

    public int getLineColor() {
        return lineColor;
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setDataPointRadius(int dataPointRadius) {
        this.dataPointRadius = dataPointRadius;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    public LineChartStyle(WidgetStyle style) {
        super(style);
    }
}
