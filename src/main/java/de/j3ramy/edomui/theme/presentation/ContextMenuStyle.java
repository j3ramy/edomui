package de.j3ramy.edomui.theme.presentation;

import de.j3ramy.edomui.util.style.GuiUtils;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class ContextMenuStyle extends ScrollableListStyle {
    private int minWidth = 100;

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public ContextMenuStyle(WidgetStyle style) {
        super(style);

        this.setTextColor(GuiUtils.getContrastColor(this.getBackgroundColor()));
    }
}
