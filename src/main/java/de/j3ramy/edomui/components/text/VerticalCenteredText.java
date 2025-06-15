package de.j3ramy.edomui.components.text;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.GuiPresets;

public final class VerticalCenteredText extends Text {

    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize, int maxTextLength, int color, int hoverColor, int disabledColor){
        super(xPos, 0, text, fontSize, maxTextLength, color, hoverColor, disabledColor);
        this.centerVertically(area);
    }

    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize, int color){
        this(area, xPos, text, fontSize, area.width, color, GuiPresets.TEXT_DEFAULT_HOVER, GuiPresets.TEXT_DEFAULT_DISABLED);
    }

    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize) {
        this(area, xPos, text, fontSize, area.width, GuiPresets.TEXT_DEFAULT, GuiPresets.TEXT_DEFAULT_HOVER, GuiPresets.TEXT_DEFAULT_DISABLED);
    }

    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize, int color, int hoverColor) {
        this(area, xPos, text, fontSize, area.width, color, hoverColor, GuiPresets.TEXT_DEFAULT_DISABLED);
    }
}
