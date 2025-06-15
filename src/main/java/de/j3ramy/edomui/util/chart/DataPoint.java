package de.j3ramy.edomui.util.chart;

public class DataPoint {
    private final String xLabel;
    private final int yValue;

    public DataPoint(String xLabel, int yValue) {
        this.xLabel = xLabel;
        this.yValue = yValue;
    }

    public String getXLabel() {
        return xLabel;
    }

    public int getYValue() {
        return yValue;
    }
}
