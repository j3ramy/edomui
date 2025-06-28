package de.j3ramy.edomui.component.text;

import de.j3ramy.edomui.enums.FontSize;

import java.awt.*;

public final class HorizontalCenteredText extends Text {
    private java.awt.Rectangle area;

    public HorizontalCenteredText(java.awt.Rectangle area, int yPos, String text, FontSize fontSize, int maxTextLength, Color color, Color hoverColor, Color disabledColor){
        super(0, yPos, text, fontSize, maxTextLength, color, hoverColor, disabledColor);

        this.area = area;
        this.centerHorizontally(area);
    }

    public HorizontalCenteredText(java.awt.Rectangle area, int yPos, String text, FontSize fontSize, int maxTextLength, Color color){
        this(area, yPos, text, fontSize, maxTextLength, color, color, color);
    }

    public HorizontalCenteredText(java.awt.Rectangle area, int yPos, String text, FontSize fontSize, Color color){
        this(area, yPos, text, fontSize, area.width, color, color, color);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.autoWidth();
        this.centerHorizontally(this.area);
    }

    public void setArea(java.awt.Rectangle area) {
        this.area = area;
        this.centerHorizontally(area);
    }
}
