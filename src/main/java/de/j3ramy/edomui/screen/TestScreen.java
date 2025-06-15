package de.j3ramy.edomui.screen;

import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.components.input.*;
import de.j3ramy.edomui.components.presentation.Grid;
import de.j3ramy.edomui.components.presentation.ScrollableList;
import de.j3ramy.edomui.components.presentation.Table;
import de.j3ramy.edomui.enums.DateFormat;
import de.j3ramy.edomui.enums.TimeFormat;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.view.ViewRegistry;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.components.text.Text;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

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
        textArea.setHidden(true);

        this.view.addWidget(new Button(100, 5, 60, 13, "Hallo", ()->System.out.println(textArea.getText())));

        textArea.setText("Hallo Jaimy,\n\nmein Name ist DIngsbums\n\n\nhahahaha");

        this.view.addWidget(new Checkbox(200, 5, 10, 10, "Ich bin eine Checkbox"));

        ScrollableList list = new ScrollableList(5, 20, 100, 100, 13, Color.RED);
        this.view.addWidget(list);
        for (int i = 0; i < 10; i++) {
            int finalI = i;

            if(i == 2){
                list.addElement("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
            }
            else{
                list.addElement("Lol " + i, () -> System.out.println("HUHU" + finalI));
            }
        }

        view.addWidget(new Dropdown(new ArrayList<>(List.of("Option 1 consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore ", "Option 2")), 250, 5, 100, 13, "Choose...", Color.RED,
                (value) -> System.out.println("Hallo, " + value)));

        NumberField numberField = new NumberField(120, 100, 80, 13, "Nummer",
                value -> System.out.println("Hallo, " + value), value -> System.out.println("Hallo, " + value));
        numberField.allowNegative();
        numberField.setFloatInput();
        view.addWidget(numberField);

        this.view.addWidget(new PasswordField(120, 120, 80, 13, "Passwort"));
        this.view.addWidget(new DateField(120, 140, 80, 13, DateFormat.US));
        this.view.addWidget(new TimeField(120, 160, 80, 13,TimeFormat.HOURS_24));

        Grid grid;
        view.addWidget(grid = new Grid(200, 100, 150, 150, 30, 30, 5));
        for(int i = 0; i < 40; i++){
            int finalI = i;
            grid.add("Hallo ich bins Jaimy " + i, Color.WHITE, Color.DARK_GREEN, () -> System.out.println("HUHU " + finalI));
        }
        grid.setHidden(true);

        Table table = (Table) new Table(250, 100, 150, 100, 13, Color.RED)
                .enableDynamicContextMenu().addContextDeleteAction().addContextAction("Test", () -> System.out.println("TEST"));
        view.addWidget(table);
        table.addHeader(Arrays.asList("Buenos Aires", "Córdoba", "La Plata"), Color.RED, Color.WHITE);

        for(int i = 0; i < 10; i++){
            table.addRow(Arrays.asList("Buenos Aires" + i, "Córdoba" + i, "La Plata" + i), Color.WHITE);
        }
        //table.setHidden(true);
    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
