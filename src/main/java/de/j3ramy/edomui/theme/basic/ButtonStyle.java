package de.j3ramy.edomui.theme.basic;

import de.j3ramy.edomui.theme.text.TextStyle;
import de.j3ramy.edomui.util.style.GuiUtils;
import de.j3ramy.edomui.util.style.WidgetStyle;

import java.awt.*;

public class ButtonStyle extends TextStyle {

    @Override
    public void setBackgroundColor(Color c) {
        super.setBackgroundColor(c);
        this.setTextColor(GuiUtils.getContrastColor(this.getBackgroundColor()));
    }

    @Override
    public void setHoverBackgroundColor(Color c) {
        super.setHoverBackgroundColor(c);
        this.setTextHoverColor(GuiUtils.getContrastColor(this.getHoverBackgroundColor()));
    }

    public ButtonStyle(WidgetStyle style) {
        super(style);

        this.setTextColor(GuiUtils.getContrastColor(this.getBackgroundColor()));
        this.setTextHoverColor(GuiUtils.getContrastColor(this.getHoverBackgroundColor()));
    }
}
