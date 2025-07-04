package de.j3ramy.edomui.component.chart;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.theme.chart.LineChartStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.util.chart.DataPoint;
import de.j3ramy.edomui.component.Widget;
import de.j3ramy.edomui.component.basic.Circle;
import de.j3ramy.edomui.component.basic.Line;
import de.j3ramy.edomui.component.basic.VerticalLine;
import de.j3ramy.edomui.component.text.Text;
import de.j3ramy.edomui.component.text.Tooltip;

import java.util.ArrayList;
import java.util.List;

public final class LineChart extends Widget {
    private final View view = new View();
    private final LineChartStyle lineChartStyle;
    private final List<Tooltip> tooltips = new ArrayList<>();

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

    @Override
    public void render(PoseStack poseStack) {
        if(this.isHidden()){
            return;
        }

        super.render(poseStack);

        if (!this.dataPoints.isEmpty()) {
            this.view.render(poseStack);

            for (Tooltip tooltip : tooltips) {
                tooltip.render(poseStack);
            }
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

            for (Tooltip tooltip : tooltips) {
                tooltip.update(x, y);
            }
        }
    }

    public void clear(){
        this.dataPoints.clear();
        this.view.clear();
        this.tooltips.clear();
    }

    private float getYAxisScale() {
        return (float) getHeight() / (yAxisMax - yAxisMin);
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

            Text tempText = new Text(0, 0, point.getXLabel(), this.lineChartStyle.getFontSize(), this.lineChartStyle.getLabelColor());
            int textWidth = tempText.getWidth();
            int centeredX = xPosition - textWidth / 2;

            this.view.addWidget(new Text(centeredX, yPosition, point.getXLabel(),
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

    public void addDataPoints(List<DataPoint> dataPoints) {
        this.view.clear();
        this.tooltips.clear();
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

            Circle c = new Circle(startX - this.lineChartStyle.getDataPointRadius() / 2,
                    startY - this.lineChartStyle.getDataPointRadius() / 2,
                    this.lineChartStyle.getDataPointRadius(),
                    this.lineChartStyle.getLineColor());
            c.setHoverable(true);
            this.view.addWidget(c);

            Tooltip tooltip = new Tooltip(start.getYValue() + " " + tooltipSuffix, c);
            this.tooltips.add(tooltip);
        }

        DataPoint last = dataPoints.get(dataPoints.size() - 1);
        int lx = getXForIndex(dataPoints.size() - 1);
        int ly = getYForValue(last.getYValue());
        Circle lastCircle = new Circle(lx - this.lineChartStyle.getDataPointRadius() / 2,
                ly - this.lineChartStyle.getDataPointRadius() / 2,
                this.lineChartStyle.getDataPointRadius(),
                this.lineChartStyle.getLineColor());
        lastCircle.setHoverable(true);
        this.view.addWidget(lastCircle);

        Tooltip lastTooltip = new Tooltip(last.getYValue() + " " + tooltipSuffix, lastCircle);
        this.tooltips.add(lastTooltip);
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

    public View getView() {
        return view;
    }
}