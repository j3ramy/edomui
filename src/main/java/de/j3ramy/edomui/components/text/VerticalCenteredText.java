package de.j3ramy.edomui.components.text;

import de.j3ramy.edomui.enums.FontSize;

import java.awt.*;

public class VerticalCenteredText extends Text {
    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize, int maxTextLength, Color color, Color hoverColor, Color disabledColor){
        super(xPos, 0, text, fontSize, maxTextLength, color, hoverColor, disabledColor);
        this.centerVertically(area);
    }

    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize, Color color){
        this(area, xPos, text, fontSize, area.width, color, color, color);
    }

    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize, Color color, Color hoverColor) {
        this(area, xPos, text, fontSize, area.width, color, hoverColor, color);
    }

    public VerticalCenteredText(java.awt.Rectangle area, int xPos, String text, FontSize fontSize, Color color, Color hoverColor, Color disabledColor) {
        this(area, xPos, text, fontSize, area.width, color, hoverColor, disabledColor);
    }
}
