package de.j3ramy.edomui.theme.text;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public class TextStyle extends WidgetStyle {
    private Color textColor = Color.WHITE;
    private Color textHoverColor = Color.GRAY;
    private Color textDisabledColor = Color.GRAY;
    private Color selectionColor = Color.YELLOW;
    private FontSize fontSize = FontSize.S;

    public Color getTextColor() {
        return textColor;
    }

    public Color getTextDisabledColor() {
        return textDisabledColor;
    }

    public Color getTextHoverColor() {
        return textHoverColor;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setTextDisabledColor(Color textDisabledColor) {
        this.textDisabledColor = textDisabledColor;
    }

    public void setTextHoverColor(Color textHoverColor) {
        this.textHoverColor = textHoverColor;
    }

    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
    }

    public TextStyle(WidgetStyle style) {
        super(style);

        if (style instanceof TextStyle other) {
            this.textColor = other.textColor;
            this.textHoverColor = other.textHoverColor;
            this.textDisabledColor = other.textDisabledColor;
            this.selectionColor = other.selectionColor;
            this.fontSize = other.fontSize;
        }
    }
}
