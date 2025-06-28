package de.j3ramy.edomui.screen;

import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.view.ViewRegistry;
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
    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
