package de.j3ramy.edomui.screen;

import de.j3ramy.edomui.EdomUiMod;
import de.j3ramy.edomui.components.basic.Line;
import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.components.chart.BarChart;
import de.j3ramy.edomui.components.chart.LineChart;
import de.j3ramy.edomui.components.image.Image;
import de.j3ramy.edomui.components.input.*;
import de.j3ramy.edomui.components.input.Checkbox;
import de.j3ramy.edomui.components.input.TextArea;
import de.j3ramy.edomui.components.input.TextField;
import de.j3ramy.edomui.components.popup.*;
import de.j3ramy.edomui.components.presentation.Grid;
import de.j3ramy.edomui.components.presentation.ScrollableList;
import de.j3ramy.edomui.components.presentation.Table;
import de.j3ramy.edomui.enums.DateFormat;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.enums.TimeFormat;
import de.j3ramy.edomui.util.chart.DataPoint;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.view.ViewRegistry;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.components.text.Text;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
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

        this.view.addWidget(new Text(5, 5, "Test", FontSize.BASE, Color.RED));

        TextArea textArea;
        view.addWidget(textArea = new TextArea(250, 100, 150, 100));

        textArea.setText("Hallo Jaimy,\n\nmein Name ist DIngsbums\n\n\nhahahaha");

        Dropdown dropdown;
        view.addWidget(dropdown = new Dropdown(new ArrayList<>(List.of("Option 1 consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore ", "Option 2")), 250, 5, 100, 13, "Choose...", Color.RED,
                (value) -> System.out.println("Hallo, " + value)));
    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
