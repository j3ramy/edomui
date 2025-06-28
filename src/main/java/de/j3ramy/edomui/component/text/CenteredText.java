package de.j3ramy.edomui.component.text;

import de.j3ramy.edomui.enums.FontSize;

import java.awt.*;

public class CenteredText extends Text {
    private final java.awt.Rectangle area;

    public CenteredText(java.awt.Rectangle area, String text, FontSize fontSize, int maxTextLength, Color color, Color hoverColor, Color disabledColor){
        super(0, 0, text, fontSize, maxTextLength, color, hoverColor, disabledColor);

        this.area = area;

        this.centerHorizontally(area);
        this.centerVertically(area);
    }

    public CenteredText(java.awt.Rectangle area, String text, FontSize fontSize, Color color, Color hoverColor, Color disabledColor){
        this(area, text, fontSize, area.width, color, hoverColor, disabledColor);
    }

    public CenteredText(java.awt.Rectangle area, String text, FontSize fontSize, Color color){
        this(area, text, fontSize, area.width, color, color, color);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.centerHorizontally(this.area);
    }
}
