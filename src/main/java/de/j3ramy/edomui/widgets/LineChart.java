package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.View;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.charts.DataPoint;

import java.awt.*;
import java.util.ArrayList;

public final class LineChart extends Widget {
    private ArrayList<DataPoint> dataPoints = new ArrayList<>();
    private int lineColor = Color.BLACK;
    private int lineWidth = 1;
    private int yAxisMin = 0;
    private int yAxisMax = 100;
    private int numberOfTicks = 5;
    private String tooltipSuffix = "";

    private final View view = new View();

    public LineChart(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);
    }

    public void clear(){
        this.dataPoints.clear();
        this.view.clear();
    }

    public void addDataPoints(ArrayList<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;

        if(this.dataPoints.isEmpty()){
            return;
        }

        int maxValue = 0;
        for(DataPoint p : dataPoints){
            if(p.getYValue() > maxValue){
                maxValue = p.getYValue();
            }
        }
        this.setYAxisRange(0, ((maxValue + 99) / 100) * 100);

        this.addXAxisLabels();
        this.addYAxisLabels();

        float yScale = getYAxisScale();
        int xSpacing = getWidth() / (this.dataPoints.size() - 1);
        for (int i = 0; i < this.dataPoints.size() - 1; i++) {
            DataPoint start = this.dataPoints.get(i);
            DataPoint end = this.dataPoints.get(i + 1);

            int startX = getLeftPos() + i * xSpacing;
            int startY = getTopPos() + getHeight() - Math.round((start.getYValue() - yAxisMin) * yScale);
            int endX = getLeftPos() + (i + 1) * xSpacing;
            int endY = getTopPos() + getHeight() - Math.round((end.getYValue() - yAxisMin) * yScale);

            this.view.addWidget(new Line(new Point(startX, startY), new Point(endX, endY), lineWidth, lineColor));

            Circle circle;
            this.view.addWidget(circle = new Circle(startX, startY, 2, lineColor));
            this.view.addWidget(new Tooltip(start.getYValue() + " " + this.tooltipSuffix, circle.toRect()));
        }

        // Letzten Punkt hinzufügen
        DataPoint lastPoint = this.dataPoints.get(this.dataPoints.size() - 1);
        int lastX = getLeftPos() + (this.dataPoints.size() - 1) * xSpacing;
        int lastY = getTopPos() + getHeight() - Math.round((lastPoint.getYValue() - yAxisMin) * yScale);
        Circle circle;
        this.view.addWidget(circle = new Circle(lastX, lastY, 2, lineColor));
        this.view.addWidget(new Tooltip(lastPoint.getYValue() + " " + this.tooltipSuffix, circle.toRect()));
    }

    public void setTooltipSuffix(String tooltipSuffix) {
        this.tooltipSuffix = tooltipSuffix;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
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

            this.view.addWidget(new Text(getLeftPos() - 15, yPosition - 1, String.valueOf(yValue), FontSize.XS, Color.BLACK));
        }
    }

    private void addXAxisLabels() {
        int xSpacing = getWidth() / (this.dataPoints.size() - 1);

        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint point = dataPoints.get(i);
            int xPosition = getLeftPos() + i * xSpacing;
            int yPosition = getTopPos() + getHeight() + 5;

            this.view.addWidget(new Text(xPosition - 3, yPosition, point.getXLabel(), FontSize.XS, Color.BLACK));

            if(i > 0 && i < this.dataPoints.size() -1){
                this.view .addWidget(new VerticalLine(xPosition, this.getTopPos(), 1, this.getHeight(), Color.GRAY));
            }
        }
    }
}

