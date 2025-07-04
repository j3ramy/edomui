package de.j3ramy.edomui.theme.text;

import de.j3ramy.edomui.util.style.GuiUtils;
import de.j3ramy.edomui.theme.WidgetStyle;

public class TooltipStyle extends TextStyle {
    private int padding = 3;
    private int lineSpacing = 5;
    private int maxWidth = 250;
    private int minWidth = 5;
    private int minHeight = 20;

    public int getPadding() {
        return padding;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public TooltipStyle(WidgetStyle style) {
        super(style);

        if (style instanceof TooltipStyle other) {
            this.padding = other.padding;
            this.lineSpacing = other.lineSpacing;
            this.maxWidth = other.maxWidth;
            this.minWidth = other.minWidth;
            this.minHeight = other.minHeight;
        } else {
            this.setTextColor(GuiUtils.getContrastColor(this.getBackgroundColor()));
        }
    }
}
