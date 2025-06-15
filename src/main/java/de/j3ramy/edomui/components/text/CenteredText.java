package de.j3ramy.edomui.components.text;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.GuiPresets;

public final class CenteredText extends Text {
    private final java.awt.Rectangle area;

    public CenteredText(java.awt.Rectangle area, String text, FontSize fontSize, int maxTextLength, int color, int hoverColor, int disabledColor){
        super(0, 0, text, fontSize, maxTextLength, color, hoverColor, disabledColor);

        this.area = area;

        this.centerHorizontally(area);
        this.centerVertically(area);
    }

    public CenteredText(java.awt.Rectangle area, String text, FontSize fontSize, int color, int hoverColor, int disabledColor){
        this(area, text, fontSize, area.width - 2 * GuiPresets.INPUT_LABEL_LEFT_MARGIN, color, hoverColor, disabledColor);
    }

    public CenteredText(java.awt.Rectangle area, String text, FontSize fontSize, int color){
        this(area, text, fontSize, area.width - 2 * GuiPresets.INPUT_LABEL_LEFT_MARGIN, color, GuiPresets.TEXT_DEFAULT_HOVER,
                GuiPresets.TEXT_DEFAULT_DISABLED);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.centerHorizontally(this.area);
    }
}
