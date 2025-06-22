package de.j3ramy.edomui.components.chart;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.theme.chart.LineChartStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.util.chart.DataPoint;
import de.j3ramy.edomui.components.Widget;
import de.j3ramy.edomui.components.basic.Circle;
import de.j3ramy.edomui.components.basic.Line;
import de.j3ramy.edomui.components.basic.VerticalLine;
import de.j3ramy.edomui.components.text.Text;
import de.j3ramy.edomui.components.text.Tooltip;

import java.util.ArrayList;
import java.util.List;

public final class LineChart extends Widget {
    private final View view = new View();
    private final LineChartStyle lineChartStyle;

    private List<DataPoint> dataPoints = new ArrayList<>();
    private int yAxisMin = 0;
    private int yAxisMax = 100;
    private int numberOfTicks = 5;
    private String tooltipSuffix = "";

    public LineChart(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        this.lineChartStyle = ThemeManager.getDefaultLineChartStyle();
        this.setStyle(lineChartStyle);
    }

    @Override
    public LineChartStyle getStyle() {
        return this.lineChartStyle;
    }

    public void clear(){
        this.dataPoints.clear();
        this.view.clear();
    }

    public void addDataPoints(List<DataPoint> dataPoints) {
        this.view.clear();
        this.dataPoints = dataPoints;

        if (this.dataPoints.size() < 2) return;

        int maxValue = 0;
        for (DataPoint p : dataPoints) {
            maxValue = Math.max(maxValue, p.getYValue());
        }

        int adjustedMax = Math.max(100, ((maxValue + 99) / 100) * 100);
        this.setYAxisRange(0, adjustedMax);

        this.addXAxisLabels();
        this.addYAxisLabels();

        for (int i = 0; i < dataPoints.size() - 1; i++) {
            DataPoint start = dataPoints.get(i);
            DataPoint end = dataPoints.get(i + 1);

            int startX = getXForIndex(i);
            int startY = getYForValue(start.getYValue());
            int endX = getXForIndex(i + 1);
            int endY = getYForValue(end.getYValue());

            this.view.addWidget(new Line(startX, startY, endX, endY, this.lineChartStyle.getLineThickness(),
                    this.lineChartStyle.getLineColor()));

            Circle c;
            int radius = this.lineChartStyle.getDataPointRadius();
            this.view.addWidget(c = new Circle(startX + radius / 2, startY + radius / 2, radius, this.lineChartStyle.getLineColor()));
            this.view.addWidget(new Tooltip(start.getYValue() + " " + tooltipSuffix, c));
        }

        DataPoint last = dataPoints.get(dataPoints.size() - 1);
        int lx = getXForIndex(dataPoints.size() - 1);
        int ly = getYForValue(last.getYValue());
        Circle c;
        this.view.addWidget(c = new Circle(lx, ly, 2, this.lineChartStyle.getLineColor()));
        this.view.addWidget(new Tooltip(last.getYValue() + " " + tooltipSuffix, c));
    }

    public void setTooltipSuffix(String tooltipSuffix) {
        this.tooltipSuffix = tooltipSuffix;
    }

    public void setYAxisRange(int min, int max) {
        this.yAxisMin = min;
        this.yAxisMax = max;
    }

    public void setNumberOfTicks(int numberOfTicks) {
        this.numberOfTicks = numberOfTicks;
    }

    private float getYAxisScale() {
        return (float) getHeight() / (yAxisMax - yAxisMin);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(this.isHidden()){
            return;
        }

        super.render(poseStack);

        if (!this.dataPoints.isEmpty()) {
            this.view.render(poseStack);
        }
    }

    @Override
    public void update(int x, int y) {
        if(this.isHidden()){
            return;
        }

        super.update(x, y);

        if (!this.dataPoints.isEmpty()) {
            this.view.update(x, y);
        }
    }

    private void addYAxisLabels() {
        float yScale = getYAxisScale();
        float tickSpacing = (yAxisMax - yAxisMin) / (float) numberOfTicks;

        for (int i = 0; i <= numberOfTicks; i++) {
            int yValue = yAxisMin + Math.round(tickSpacing * i);
            int yPosition = getTopPos() + getHeight() - Math.round((yValue - yAxisMin) * yScale);

            this.view.addWidget(new Text(getLeftPos() + this.lineChartStyle.getYAxisLabelOffset(), yPosition - 1, String.valueOf(yValue),
                   this.lineChartStyle.getFontSize(), this.lineChartStyle.getLabelColor()));
        }
    }

    private void addXAxisLabels() {
        int xSpacing = getWidth() / (this.dataPoints.size() - 1);

        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint point = dataPoints.get(i);
            int xPosition = getLeftPos() + i * xSpacing;
            int yPosition = getTopPos() + getHeight() + this.lineChartStyle.getXAxisLabelOffset();

            this.view.addWidget(new Text(xPosition - 5, yPosition, point.getXLabel(),
                    this.lineChartStyle.getFontSize(), this.lineChartStyle.getLabelColor()));

            if(i > 0 && i < this.dataPoints.size() -1){
                this.view .addWidget(new VerticalLine(xPosition, this.getTopPos(), 1, this.getHeight(),
                        this.lineChartStyle.getBorderColor()));
            }
        }
    }

    private int getXForIndex(int i) {
        if (dataPoints.size() < 2) return getLeftPos();
        int xSpacing = getWidth() / (dataPoints.size() - 1);
        return getLeftPos() + i * xSpacing;
    }

    private int getYForValue(int yValue) {
        float yScale = getYAxisScale();
        return getTopPos() + getHeight() - Math.round((yValue - yAxisMin) * yScale);
    }

}

