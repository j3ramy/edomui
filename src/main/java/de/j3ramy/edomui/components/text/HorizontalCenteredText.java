package de.j3ramy.edomui.components.text;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.GuiPresets;

public final class HorizontalCenteredText extends Text {
    private java.awt.Rectangle area;

    public HorizontalCenteredText(java.awt.Rectangle area, int yPos, String text, FontSize fontSize, int maxTextLength, int color, int hoverColor, int disabledColor){
        super(0, yPos, text, fontSize, maxTextLength, color, hoverColor, disabledColor);

        this.area = area;
        this.centerHorizontally(area);
    }

    public HorizontalCenteredText(java.awt.Rectangle area, int yPos, String text, FontSize fontSize, int maxTextLength, int color){
        this(area, yPos, text, fontSize, maxTextLength, color, GuiPresets.TEXT_DEFAULT_HOVER, GuiPresets.TEXT_DEFAULT_DISABLED);
    }

    public HorizontalCenteredText(java.awt.Rectangle area, int yPos, String text, FontSize fontSize, int color){
        this(area, yPos, text, fontSize, area.width, color, GuiPresets.TEXT_DEFAULT_HOVER, GuiPresets.TEXT_DEFAULT_DISABLED);
    }

    public HorizontalCenteredText(java.awt.Rectangle area, int yPos, String text, FontSize fontSize) {
        this(area, yPos, text, fontSize, area.width, GuiPresets.TEXT_DEFAULT, GuiPresets.TEXT_DEFAULT_HOVER, GuiPresets.TEXT_DEFAULT_DISABLED);
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
