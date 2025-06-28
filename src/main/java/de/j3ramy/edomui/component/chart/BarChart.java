package de.j3ramy.edomui.component.chart;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.theme.chart.BarChartStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.util.chart.DataPoint;
import de.j3ramy.edomui.component.basic.HorizontalLine;
import de.j3ramy.edomui.component.basic.Rectangle;
import de.j3ramy.edomui.component.text.Text;
import de.j3ramy.edomui.component.text.Tooltip;
import de.j3ramy.edomui.component.Widget;

import java.util.ArrayList;
import java.util.List;

public final class BarChart extends Widget {
    private final BarChartStyle barChartStyle;

    private List<DataPoint> dataPoints = new ArrayList<>();
    private int yAxisMin = 0;
    private int yAxisMax = 100;
    private int numberOfTicks = 5;
    private String tooltipSuffix = "";

    private final View view = new View();

    public BarChart(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        this.barChartStyle = ThemeManager.getDefaultBarChartStyle();
        this.setStyle(barChartStyle);
    }

    @Override
    public BarChartStyle getStyle() {
        return this.barChartStyle;
    }


    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;
        super.render(poseStack);
        view.render(poseStack);
    }

    @Override
    public void update(int x, int y) {
        if (isHidden()) return;
        super.update(x, y);
        view.update(x, y);
    }

    private float getYAxisScale() {
        return (float) getHeight() / (yAxisMax - yAxisMin);
    }

    private void addYAxisLabels() {
        float yScale = getYAxisScale();
        float tickSpacing = (yAxisMax - yAxisMin) / (float) numberOfTicks;

        for (int i = 0; i <= numberOfTicks; i++) {
            int yValue = yAxisMin + Math.round(tickSpacing * i);
            int y = getTopPos() + getHeight() - Math.round((yValue - yAxisMin) * yScale);

            view.addWidget(new Text(getLeftPos() + this.barChartStyle.getYAxisLabelOffset(), y - 1, String.valueOf(yValue),
                    this.barChartStyle.getFontSize(), this.barChartStyle.getLabelColor()));

            if (i > 0 && i < numberOfTicks) {
                view.addWidget(new HorizontalLine(getLeftPos(), y, getWidth(), 1, this.barChartStyle.getBorderColor()));
            }
        }
    }

    private void addXAxisLabels(int barWidth) {
        int xSpacing = barWidth + this.barChartStyle.getBarSpacing();

        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint point = dataPoints.get(i);
            int x = getLeftPos() + i * xSpacing;
            Text tempText = new Text(0, 0, point.getXLabel(), this.barChartStyle.getFontSize(), this.barChartStyle.getLabelColor());
            int textWidth = tempText.getWidth();
            int textX = x + (barWidth - textWidth) / 2;
            int y = getTopPos() + getHeight() + this.barChartStyle.getXAxisLabelOffset();

            view.addWidget(new Text(textX, y, point.getXLabel(), this.barChartStyle.getFontSize(), this.barChartStyle.getLabelColor()));
        }
    }

    public void clear() {
        this.dataPoints.clear();
        this.view.clear();
    }

    public void addDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = new ArrayList<>(dataPoints);
        refresh();
    }

    public void refresh() {
        this.view.clear();

        if (dataPoints.isEmpty()) return;

        int totalSpacing = (dataPoints.size() - 1) * this.barChartStyle.getBarSpacing();
        int availableWidth = getWidth() - totalSpacing;
        int barWidth = Math.max(availableWidth / dataPoints.size(), 1);

        int maxValue = dataPoints.stream()
                .mapToInt(DataPoint::getYValue)
                .max()
                .orElse(yAxisMax);
        setYAxisRange(0, ((maxValue + 99) / 100) * 100);

        addXAxisLabels(barWidth);
        addYAxisLabels();

        float yScale = getYAxisScale();
        int xSpacing = barWidth + this.barChartStyle.getBarSpacing();

        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint point = dataPoints.get(i);
            int x = getLeftPos() + i * xSpacing;
            int barHeight = Math.max(Math.round((point.getYValue() - yAxisMin) * yScale), this.barChartStyle.getMinBarHeight());
            int y = getTopPos() + getHeight() - barHeight;

            Rectangle bar = new Rectangle(x, y, barWidth, barHeight, this.barChartStyle.getBarColor());
            this.view.addWidget(bar);

            String suffix = tooltipSuffix != null ? tooltipSuffix : "";
            this.view.addWidget(new Tooltip(point.getXLabel() + ": " + point.getYValue() + " " + suffix, bar));
        }
    }

    public void setYAxisRange(int min, int max) {
        this.yAxisMin = min;
        this.yAxisMax = max;
    }

    public void setNumberOfTicks(int numberOfTicks) {
        this.numberOfTicks = numberOfTicks;
    }

    public void setTooltipSuffix(String tooltipSuffix) {
        this.tooltipSuffix = tooltipSuffix;
    }
}