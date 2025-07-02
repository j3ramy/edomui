package de.j3ramy.edomui.screen;

import de.j3ramy.edomui.component.button.StaticImageButton;
import de.j3ramy.edomui.component.presentation.ScrollableList;
import de.j3ramy.edomui.component.text.Tooltip;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.view.ViewRegistry;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.component.text.Text;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

import javax.swing.plaf.ToolTipUI;
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
    }

    @Override
    public void onClose() {
        ViewRegistry.unregisterView(this.view);

        super.onClose();
    }
}
