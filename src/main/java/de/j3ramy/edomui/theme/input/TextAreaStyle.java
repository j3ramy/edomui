package de.j3ramy.edomui.theme.input;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class TextAreaStyle extends TextFieldStyle {
    public TextAreaStyle(WidgetStyle style) {
        super(style);

        this.setFontSize(FontSize.XS);
        this.setHoverBackgroundColor(this.getBackgroundColor());
        this.setDisabledBackgroundColor(this.getBackgroundColor());
        this.setTextHoverColor(this.getTextColor());
        this.setTextDisabledColor(this.getTextColor());
    }
}