package de.j3ramy.edomui.theme.presentation;

import de.j3ramy.edomui.theme.input.InputStyle;
import de.j3ramy.edomui.util.style.GuiUtils;
import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public class ScrollableListStyle extends InputStyle {
    private Color selectionColor = Color.BLACK;
    private int elementHeight = 13;

    public int getElementHeight() {
        return elementHeight;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;

        updateTextColors();
    }

    public void setElementHeight(int elementHeight) {
        this.elementHeight = elementHeight;
    }

    private void updateTextColors() {
        if (selectionColor != null) {
            Color contrastColor = GuiUtils.getContrastColor(selectionColor);

            this.setTextHoverColor(contrastColor);

            if (this.getBackgroundColor() != null) {
                Color normalTextColor = GuiUtils.getContrastColor(this.getBackgroundColor());
                this.setTextColor(normalTextColor);
            }
        }
    }

    @Override
    public void setBackgroundColor(Color backgroundColor) {
        super.setBackgroundColor(backgroundColor);
        updateTextColors();
    }

    public ScrollableListStyle(WidgetStyle style) {
        super(style);

        if (style instanceof ScrollableListStyle other) {
            this.selectionColor = other.selectionColor;
            this.elementHeight = other.elementHeight;
        }

        updateTextColors();
    }
}