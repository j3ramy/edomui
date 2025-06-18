package de.j3ramy.edomui.components.chart;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.Color;
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
    private static final int LABEL_OFFSET_LEFT = 15;
    private static final int LABEL_SPACING_BOTTOM = 5;
    private static final int CIRCLE_RADIUS = 2;

    private List<DataPoint> dataPoints = new ArrayList<>();
    private int lineColor;
    private int lineWidth;
    private int yAxisMin = 0;
    private int yAxisMax = 100;
    private int numberOfTicks = 5;
    private String tooltipSuffix = "";

    private final View view = new View();

    public LineChart(int xPos, int yPos, int width, int height, int lineColor, int lineWidth) {
        super(xPos, yPos, width, height);

        this.lineColor = lineColor;
        this.lineWidth = lineWidth;
    }

    public LineChart(int xPos, int yPos, int width, int height, int lineColor) {
        this(xPos, yPos, width, height, lineColor, 1);
    }

    public LineChart(int xPos, int yPos, int width, int height) {
        this(xPos, yPos, width, height, Color.BLACK, 1);
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

            System.out.println(lineWidth);
            this.view.addWidget(new Line(startX, startY, endX, endY, lineWidth, lineColor));

            Circle c;
            this.view.addWidget(c = new Circle(startX + CIRCLE_RADIUS / 2, startY + CIRCLE_RADIUS / 2, CIRCLE_RADIUS, lineColor));
            this.view.addWidget(new Tooltip(start.getYValue() + " " + tooltipSuffix, c));
        }

        DataPoint last = dataPoints.get(dataPoints.size() - 1);
        int lx = getXForIndex(dataPoints.size() - 1);
        int ly = getYForValue(last.getYValue());
        Circle c;
        this.view.addWidget(c = new Circle(lx, ly, 2, lineColor));
        this.view.addWidget(new Tooltip(last.getYValue() + " " + tooltipSuffix, c));
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

    public void setTextColor(int textColor){
        for(Widget widget : this.view.getWidgets()){
            if(widget instanceof Text){
                ((Text)widget).setTextColor(textColor);
            }
        }
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

            this.view.addWidget(new Text(getLeftPos() - LABEL_OFFSET_LEFT, yPosition - 1, String.valueOf(yValue), FontSize.XS, Color.BLACK));
        }
    }

    private void addXAxisLabels() {
        int xSpacing = getWidth() / (this.dataPoints.size() - 1);

        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint point = dataPoints.get(i);
            int xPosition = getLeftPos() + i * xSpacing;
            int yPosition = getTopPos() + getHeight() + LABEL_SPACING_BOTTOM;

            this.view.addWidget(new Text(xPosition - 3, yPosition, point.getXLabel(), FontSize.XS, Color.BLACK));

            if(i > 0 && i < this.dataPoints.size() -1){
                this.view .addWidget(new VerticalLine(xPosition, this.getTopPos(), 1, this.getHeight(), Color.GRAY));
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

