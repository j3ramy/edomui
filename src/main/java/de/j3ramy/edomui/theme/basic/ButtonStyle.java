package de.j3ramy.edomui.theme.basic;

import de.j3ramy.edomui.theme.text.TextStyle;
import de.j3ramy.edomui.util.style.GuiUtils;
import de.j3ramy.edomui.theme.WidgetStyle;

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

    @Override
    public void setDisabledBackgroundColor(Color c) {
        super.setDisabledBackgroundColor(c);
        this.setTextDisabledColor(GuiUtils.getContrastColor(this.getDisabledBackgroundColor()));
    }

    public ButtonStyle(WidgetStyle style) {
        super(style);

        if (!(style instanceof ButtonStyle)) {
            this.setTextColor(GuiUtils.getContrastColor(this.getBackgroundColor()));
            this.setTextHoverColor(GuiUtils.getContrastColor(this.getHoverBackgroundColor()));
            this.setTextDisabledColor(GuiUtils.getContrastColor(this.getDisabledBackgroundColor()));
        }
    }
}
