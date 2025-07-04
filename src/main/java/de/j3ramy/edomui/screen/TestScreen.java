package de.j3ramy.edomui.screen;

import de.j3ramy.edomui.component.chart.BarChart;
import de.j3ramy.edomui.component.chart.LineChart;
import de.j3ramy.edomui.util.chart.DataPoint;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.view.ViewRegistry;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.component.text.Text;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

import java.awt.*;
import java.util.List;

public class TestScreen extends Screen {
    private View view;

    public TestScreen() {
        super(new TextComponent("Test Screen"));
    }

    @Override
    protected void init() {
        super.init();

        ViewRegistry.registerView(this.view = new View());

        this.view.addWidget(new Text(10, 10, "Hey there! Greetings from EdomUI", FontSize.BASE, Color.BLUE));

        LineChart barChart = new LineChart(5, 10, 120, 80);
        barChart.getStyle().setLineColor(Color.MAGENTA);
        view.addWidget(barChart);
        barChart.addDataPoints(List.of(new DataPoint("Ausweis", 20),
                new DataPoint("Baugenehmigung", 9), new DataPoint("Bier", 15)));
    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
