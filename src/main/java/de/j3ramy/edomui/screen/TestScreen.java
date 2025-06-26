package de.j3ramy.edomui.screen;

import de.j3ramy.edomui.EdomUiMod;
import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.components.chart.BarChart;
import de.j3ramy.edomui.components.chart.LineChart;
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

        Table table = (Table) new Table(250, 100, 150, 100, Color.MAGENTA)
                .enableDynamicContextMenu().addContextDeleteAction().addContextAction("Test", () -> System.out.println("TEST"));
        view.addWidget(table);
        table.addHeader(Arrays.asList("Buenos Aires", "Córdoba", "La Plata"));
        //table.setEnabled(false);

        for(int i = 0; i < 10; i++){
            table.addRow(Arrays.asList("Buenos Aires" + i, "Córdoba" + i, "La Plata" + i));
        }
    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
