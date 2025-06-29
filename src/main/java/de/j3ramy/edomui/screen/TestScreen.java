package de.j3ramy.edomui.screen;

import de.j3ramy.edomui.component.presentation.ScrollableList;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.view.ViewRegistry;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.component.text.Text;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

import java.awt.*;

public class TestScreen extends Screen {
    private View view;

    public TestScreen() {
        super(new TextComponent("Test Screen"));
    }

    @Override
    protected void init() {
        super.init();

        ViewRegistry.registerView(this.view = new View());

        this.view.addWidget(new Text(5, 5, "Hey there! Greetings from EdomUI", FontSize.BASE, Color.BLUE));

        ScrollableList list = new ScrollableList(5, 20, 100, 200, Color.RED);
        this.view.addWidget(list);

        for(int i = 0; i < 5; i++)
            list.addElement("TEST");

        list.getStyle().setBackgroundColor(Color.LIGHT_GRAY);
    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
