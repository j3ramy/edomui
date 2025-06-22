package de.j3ramy.edomui.theme.input;

import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class TextFieldStyle extends InputStyle {
    private int placeholderColor = Color.DARK_GRAY;
    private int selectionColor = Color.YELLOW;

    public int getPlaceholderColor() {
        return placeholderColor;
    }

    public int getSelectionColor() {
        return selectionColor;
    }

    public void setPlaceholderColor(int placeholderColor) {
        this.placeholderColor = placeholderColor;
    }

    public void setSelectionColor(int selectionColor) {
        this.selectionColor = selectionColor;
    }

    public TextFieldStyle(WidgetStyle style) {
        super(style);
    }
}
