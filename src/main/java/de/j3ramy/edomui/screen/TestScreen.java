package de.j3ramy.edomui.screen;

import de.j3ramy.edomui.EdomUiMod;
import de.j3ramy.edomui.components.button.Button;
import de.j3ramy.edomui.components.chart.BarChart;
import de.j3ramy.edomui.components.chart.LineChart;
import de.j3ramy.edomui.components.input.*;
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
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.components.text.Text;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

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

        textArea.setText("Hallo Jaimy,\n\nmein Name ist DIngsbums\n\n\nhahahaha");

        this.view.addWidget(new Checkbox(200, 5, 11, 11, "Ich bin eine Checkbox"));

        ScrollableList list = new ScrollableList(5, 20, 100, 100, 13, Color.RED).enableDynamicContextMenu().addContextDeleteAction();
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
        list.setHidden(true);

        Dropdown dropdown;
        view.addWidget(dropdown = new Dropdown(new ArrayList<>(List.of("Option 1 consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore ", "Option 2")), 250, 5, 100, 13, "Choose...", Color.RED,
                (value) -> System.out.println("Hallo, " + value)));

        NumberField numberField = new NumberField(120, 100, 80, 13, "Nummer",
                value -> System.out.println("Hallo, " + value), value -> System.out.println("Hallo, " + value));
        //numberField.allowNegative();
        //numberField.setFloatInput();
        view.addWidget(numberField);

        PasswordField passwordField;
        this.view.addWidget(passwordField = new PasswordField(120, 120, 80, 13, "Passwort",
                (value) -> System.out.println("Hallo CHANGE, " + value), value -> System.out.println("Hallo ENTER, " + value)));
        DateField dateField;
        this.view.addWidget(dateField = new DateField(120, 140, 80, 13, DateFormat.US));
        TimeField timeField;
        this.view.addWidget(timeField = new TimeField(120, 160, 80, 13,TimeFormat.HOURS_24));
        TextField textField;
        this.view.addWidget(textField = new TextField(120, 180, 80, 13, "HALLO"));

        Grid grid;
        view.addWidget(grid = new Grid(200, 100, 150, 150, 30, 30, 5)
                .enableDynamicContextMenu().addContextDeleteAction());
        for(int i = 0; i < 40; i++){
            int finalI = i;
            grid.add("Hallo ich bins Jaimy " + i, Color.WHITE, Color.DARK_GREEN, () -> System.out.println("HUHU " + finalI));
        }
        //grid.setHidden(true);

        Table table = (Table) new Table(250, 100, 150, 100, 13, Color.RED)
                .enableDynamicContextMenu().addContextDeleteAction().addContextAction("Test", () -> System.out.println("TEST"));
        view.addWidget(table);
        table.addHeader(Arrays.asList("Buenos Aires", "Córdoba", "La Plata"), Color.RED, Color.WHITE);
        table.setEnabled(false);

        for(int i = 0; i < 10; i++){
            table.addRow(Arrays.asList("Buenos Aires" + i, "Córdoba" + i, "La Plata" + i), Color.WHITE);
        }
        table.setHidden(true);

        ResourceLocation image = new ResourceLocation(EdomUiMod.MOD_ID, "sprite-example.png");
        //view.addWidget(new Image(100, 100, 100, 100, image));

        //view.addWidget(new SpriteImageButton(100, 100, 20, 20, 20, 20, 256, 256, image, ()->System.out.println("HIHI2")));

        BarChart barChart;
        view.addWidget(barChart = new BarChart(250, 100, 100, 60));
        barChart.setTooltipSuffix("Anzahl Verkaufe");
        barChart.addDataPoints(List.of(new DataPoint("Banane", 5),
                new DataPoint("Apfel", 75), new DataPoint("Gurke", 35), new DataPoint("Erdbeere", 23)));
        barChart.getStyle().setLabelColor(Color.WHITE);
        barChart.setHidden(true);

        LineChart lineChart;
        view.addWidget(lineChart = new LineChart(250, 100, 100, 60));
        lineChart.getStyle().setLineColor(Color.PURPLE);
        lineChart.setTooltipSuffix("€");
        lineChart.addDataPoints(List.of(new DataPoint("Januar", 56),
                new DataPoint("Februar", 75), new DataPoint("März", 35), new DataPoint("April", 23)));
        //lineChart.setHidden(true);

        //view.addWidget(new Line(5, 5, 10, 10, 1, Color.RED));

        Button button;
        this.view.addWidget(button = new Button(100, 5, 60, 13, "Hallo", ()->{
            dropdown.setEnabled(false);
            textArea.setEnabled(false);
            textField.setEnabled(false);
            numberField.setEnabled(false);
            passwordField.setEnabled(false);
            dateField.setEnabled(false);
            timeField.setEnabled(false);
        }));


                      /*
        this.view.addWidget(new ConfirmPopUp(view, 150, 100, "Der Computer wurde infiziert", "Der Computer wurde mit einem " +
                "Virus infiziert. Bitte schließen Sie alle Programme und schreiben Sie an folgende E-Mail Ihre Passwörter, " +
                "um diese sicher zu verwahren: passwoerter-sind-cool@scam.de", "Ja", "Nein", PopUpType.SUCCESS,
                () -> {System.out.println("JUHUUUU");}));


        this.view.addWidget(new InputPopUp(view, 150, 100, "Geben Sie Ihr Passwort ein", "Anmelden",
                "Abbrechen", "", "Passwort", PopUpType.NOTICE, System.out::println, true));

        this.view.addWidget(new InputPopUp(view, 150, 100, "Ordnername ändern", "Speichern",
                "Abbrechen", "folder-1", "Name", PopUpType.NOTICE, System.out::println));




        ProgressPopUp progressPopUp;
        this.view.addWidget(progressPopUp = new ProgressPopUp(view, 150, 100, "Lade Daten...", "Die Daten werden aus dem Archiv geladen.",
                PopUpType.NOTICE, 6, () -> System.out.println("FINISH")));
        progressPopUp.getProgressBar().getStyle().setBarColor(Color.DARK_RED);

               */

        this.view.addWidget(new AlertPopUp(view, 150, 100, "Fehler", "Ihr Passwort ist falsch",
                "Schließen", PopUpType.ERROR));


    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
