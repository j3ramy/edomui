package de.j3ramy.edomui.theme;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class ButtonStyle extends WidgetStyle {
    private int textColor = Color.WHITE;
    private int textHoverColor = Color.GRAY;
    private int textDisabledColor = Color.GRAY;
    private FontSize fontSize = FontSize.S;

    public int getTextColor() {
        return textColor;
    }

    public int getTextDisabledColor() {
        return textDisabledColor;
    }

    public int getTextHoverColor() {
        return textHoverColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextDisabledColor(int textDisabledColor) {
        this.textDisabledColor = textDisabledColor;
    }

    public void setTextHoverColor(int textHoverColor) {
        this.textHoverColor = textHoverColor;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public ButtonStyle(WidgetStyle style) {
        super(style);
    }
}
