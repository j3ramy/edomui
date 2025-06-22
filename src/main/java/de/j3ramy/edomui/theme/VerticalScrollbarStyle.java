package de.j3ramy.edomui.theme;

import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class VerticalScrollbarStyle extends WidgetStyle {
    private int scrollbarTrackWidth = 3;
    private int thumbColor = Color.GRAY;

    public int getThumbColor() {
        return thumbColor;
    }

    public int getScrollbarTrackWidth() {
        return scrollbarTrackWidth;
    }

    public void setScrollbarTrackWidth(int scrollbarTrackWidth) {
        this.scrollbarTrackWidth = scrollbarTrackWidth;
    }

    public void setThumbColor(int thumbColor) {
        this.thumbColor = thumbColor;
    }

    public VerticalScrollbarStyle(WidgetStyle style) {
        super(style);
    }
}
