package de.j3ramy.edomui.screen;

import de.j3ramy.edomui.component.input.Dropdown;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.view.ViewRegistry;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.component.text.Text;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

import java.awt.*;
import java.util.ArrayList;
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

        this.view.addWidget(new Text(10, 10, "Hey! EdomUI is working :D", FontSize.BASE, Color.BLUE));

        view.addWidget(new Dropdown(new ArrayList<>(List.of("Test 1", "Test 2")), 10, 10, 60, 13, "Test",
                System.out::println));
    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
