package de.j3ramy.edomui.components.chart;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.chart.DataPoint;
import de.j3ramy.edomui.components.basic.HorizontalLine;
import de.j3ramy.edomui.components.basic.Rectangle;
import de.j3ramy.edomui.components.text.Text;
import de.j3ramy.edomui.components.text.Tooltip;
import de.j3ramy.edomui.components.Widget;

import java.util.ArrayList;
import java.util.List;

public final class BarChart extends Widget {
    private static final int LABEL_OFFSET_LEFT = 15;
    private static final int LABEL_SPACING_BOTTOM = 5;
    private static final int MIN_BAR_HEIGHT = 2;

    private List<DataPoint> dataPoints = new ArrayList<>();
    private int barColor = Color.BLACK;
    private int yAxisMin = 0;
    private int yAxisMax = 100;
    private int numberOfTicks = 5;
    private int barSpacing = 5;
    private String tooltipSuffix = "";

    private final View view = new View();

    public BarChart(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);
    }

    public BarChart(int xPos, int yPos, int width, int height, int barColor, int barSpacing) {
        this(xPos, yPos, width, height);
        this.barColor = barColor;
        this.barSpacing = barSpacing;
    }

    public void clear() {
        this.dataPoints.clear();
        this.view.clear();
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = new ArrayList<>(dataPoints);
        refresh();
    }

    public void refresh() {
        this.view.clear();

        if (dataPoints.isEmpty()) return;

        int totalSpacing = (dataPoints.size() - 1) * barSpacing;
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
        int xSpacing = barWidth + barSpacing;

        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint point = dataPoints.get(i);
            int x = getLeftPos() + i * xSpacing;
            int barHeight = Math.max(Math.round((point.getYValue() - yAxisMin) * yScale), MIN_BAR_HEIGHT);
            int y = getTopPos() + getHeight() - barHeight;

            Rectangle bar = new Rectangle(x, y, barWidth, barHeight, barColor);
            this.view.addWidget(bar);

            String suffix = tooltipSuffix != null ? tooltipSuffix : "";
            this.view.addWidget(new Tooltip(point.getXLabel() + ": " + point.getYValue() + " " + suffix, bar.toRect()));
        }
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
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

    public void setBarSpacing(int barSpacing) {
        this.barSpacing = barSpacing;
    }

    private float getYAxisScale() {
        return (float) getHeight() / (yAxisMax - yAxisMin);
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

    private void addYAxisLabels() {
        float yScale = getYAxisScale();
        float tickSpacing = (yAxisMax - yAxisMin) / (float) numberOfTicks;

        for (int i = 0; i <= numberOfTicks; i++) {
            int yValue = yAxisMin + Math.round(tickSpacing * i);
            int y = getTopPos() + getHeight() - Math.round((yValue - yAxisMin) * yScale);

            view.addWidget(new Text(getLeftPos() - LABEL_OFFSET_LEFT, y - 1, String.valueOf(yValue), FontSize.XS, Color.BLACK));

            if (i > 0 && i < numberOfTicks) {
                view.addWidget(new HorizontalLine(getLeftPos(), y, getWidth(), 1, Color.GRAY));
            }
        }
    }

    private void addXAxisLabels(int barWidth) {
        int xSpacing = barWidth + barSpacing;

        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint point = dataPoints.get(i);
            int x = getLeftPos() + i * xSpacing;
            Text tempText = new Text(0, 0, point.getXLabel(), FontSize.XS, Color.BLACK);
            int textWidth = tempText.getWidth();
            int textX = x + (barWidth - textWidth) / 2;
            int y = getTopPos() + getHeight() + LABEL_SPACING_BOTTOM;

            view.addWidget(new Text(textX, y, point.getXLabel(), FontSize.XS, Color.BLACK));
        }
    }
}