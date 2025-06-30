package de.j3ramy.edomui.theme.input;

import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public class TextFieldStyle extends InputStyle {
    private Color placeholderColor = Color.GRAY;
    private Color selectionColor = Color.ORANGE;
    private int lineSpacing = 5;

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
    }

    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public TextFieldStyle(WidgetStyle style) {
        super(style);

        if (style instanceof TextFieldStyle other) {
            this.placeholderColor = other.placeholderColor;
            this.selectionColor = other.selectionColor;
            this.lineSpacing = other.lineSpacing;
        }
    }
}
