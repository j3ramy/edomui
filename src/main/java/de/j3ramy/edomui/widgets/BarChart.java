package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.View;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.charts.DataPoint;

import java.util.ArrayList;

public final class BarChart extends Widget {
    private ArrayList<DataPoint> dataPoints = new ArrayList<>();
    private int barColor = Color.BLACK;
    private int yAxisMin = 0;
    private int yAxisMax = 100;
    private int numberOfTicks = 5;
    private int barSpacing = 5;
    private int barWidth;
    private String tooltipSuffix;

    private final View view = new View();

    public BarChart(int xPos, int yPos, int width, int height) {
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

        // Gesamtbreite für die Balken berechnen (inkl. der Abstände)
        int totalSpacing = (dataPoints.size() - 1) * this.barSpacing;
        int availableWidth = this.getWidth() - totalSpacing;
        this.barWidth = availableWidth / dataPoints.size();

        // Stelle sicher, dass barWidth mindestens 1 ist
        this.barWidth = Math.max(this.barWidth, 1);

        int maxValue = 0;
        for(DataPoint p : dataPoints){
            if(p.getYValue() > maxValue){
                maxValue = p.getYValue();
            }
        }
        this.setYAxisRange(0, ((maxValue + 99) / 100) * 100);

        addXAxisLabels();
        addYAxisLabels();

        float yScale = getYAxisScale();
        int xSpacing = barWidth + barSpacing;
        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint point = dataPoints.get(i);
            int xPosition = getLeftPos() + i * xSpacing;
            int barHeight = Math.max(Math.round((point.getYValue() - yAxisMin) * yScale), 2);
            int yPosition = getTopPos() + getHeight() - barHeight;

            Rectangle bar;
            this.view.addWidget(bar = new Rectangle(xPosition, yPosition, barWidth, barHeight, barColor));
            this.view.addWidget(new Tooltip( point.getXLabel() + ": " + point.getYValue() + " " + this.tooltipSuffix, bar.toRect()));
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

            if(i > 0 && i < this.numberOfTicks){
                this.view.addWidget(new HorizontalLine(this.getLeftPos(), yPosition, this.getWidth(), 1, Color.GRAY));
            }
        }
    }

    private void addXAxisLabels() {
        int xSpacing = barWidth + barSpacing;

        for (int i = 0; i < dataPoints.size(); i++) {
            DataPoint point = dataPoints.get(i);
            int xPosition = getLeftPos() + i * xSpacing;

            Text tempText = new Text(0, 0, point.getXLabel(), FontSize.XS, Color.BLACK);
            int textWidth = tempText.getWidth();
            int textXPosition = xPosition + (barWidth - textWidth) / 2;
            int yPosition = getTopPos() + getHeight() + 5;

            this.view.addWidget(new Text(textXPosition, yPosition, point.getXLabel(), FontSize.XS, Color.BLACK));

        }
    }
}


