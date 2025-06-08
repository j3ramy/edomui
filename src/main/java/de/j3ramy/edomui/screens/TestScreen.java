package de.j3ramy.edomui.screens;

import de.j3ramy.edomui.View;
import de.j3ramy.edomui.ViewRegistry;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.widgets.Text;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

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
    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
