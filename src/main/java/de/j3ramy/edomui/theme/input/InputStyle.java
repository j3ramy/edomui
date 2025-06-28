package de.j3ramy.edomui.theme.input;

import de.j3ramy.edomui.theme.basic.ButtonStyle;
import de.j3ramy.edomui.util.style.WidgetStyle;

public abstract class InputStyle extends ButtonStyle {
    private int padding = 3;

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public InputStyle(WidgetStyle style) {
        super(style);
    }
}
