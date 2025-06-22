package de.j3ramy.edomui.theme.input;

import de.j3ramy.edomui.theme.ButtonStyle;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class CheckboxStyle extends ButtonStyle {
   private int checkMargin = 2;
   private int labelLeftMargin = 5;
   private int checkColor = Color.WHITE;

    public int getCheckMargin() {
        return checkMargin;
    }

    public int getCheckColor() {
        return checkColor;
    }

    public int getLabelLeftMargin() {
        return labelLeftMargin;
    }

    public void setCheckMargin(int checkMargin) {
        this.checkMargin = checkMargin;
    }

    public void setCheckColor(int checkColor) {
        this.checkColor = checkColor;
    }

    public void setLabelLeftMargin(int labelLeftMargin) {
        this.labelLeftMargin = labelLeftMargin;
    }

    public CheckboxStyle(WidgetStyle style) {
        super(style);
    }
}
