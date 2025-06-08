package de.j3ramy.edomui.widgets;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.utils.GuiPresets;

public class VerticalCenteredText extends Text {

    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize, int maxTextLength, int color, int hoverColor, int disabledColor){
        super(xPos, 0, text, fontSize, maxTextLength, color, hoverColor, disabledColor);
        this.centerVertically(area);
    }

    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize, int color){
        this(area, xPos, text, fontSize, area.width, color, GuiPresets.TEXT_DEFAULT_HOVER, GuiPresets.TEXT_DEFAULT_DISABLED);
    }
}
