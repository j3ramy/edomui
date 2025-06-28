package de.j3ramy.edomui.theme.input;

import de.j3ramy.edomui.util.style.WidgetStyle;

public class TextAreaStyle extends TextFieldStyle {
    public TextAreaStyle(WidgetStyle style) {
        super(style);

        this.setHoverBackgroundColor(this.getBackgroundColor());
    }
}