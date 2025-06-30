package de.j3ramy.edomui.theme.basic;

import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public class VerticalScrollbarStyle extends WidgetStyle {
    private int scrollbarTrackWidth = 3;
    private Color thumbColor = Color.LIGHT_GRAY;

    public Color getThumbColor() {
        return thumbColor;
    }

    public int getScrollbarTrackWidth() {
        return scrollbarTrackWidth;
    }

    public void setScrollbarTrackWidth(int scrollbarTrackWidth) {
        this.scrollbarTrackWidth = scrollbarTrackWidth;
    }

    public void setThumbColor(Color thumbColor) {
        this.thumbColor = thumbColor;
    }

    public VerticalScrollbarStyle(WidgetStyle style) {
        super(style);

        if (style instanceof VerticalScrollbarStyle other) {
            this.scrollbarTrackWidth = other.scrollbarTrackWidth;
            this.thumbColor = other.thumbColor;
        }
    }
}
