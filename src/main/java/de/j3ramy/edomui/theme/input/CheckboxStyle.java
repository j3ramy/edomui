package de.j3ramy.edomui.theme.input;

import de.j3ramy.edomui.theme.ButtonStyle;
import de.j3ramy.edomui.util.style.WidgetStyle;

import java.awt.*;

public class CheckboxStyle extends ButtonStyle {
   private int checkMargin = 2;
   private int labelLeftMargin = 5;
   private Color checkColor = Color.WHITE;

    public int getCheckMargin() {
        return checkMargin;
    }

    public Color getCheckColor() {
        return checkColor;
    }

    public int getLabelLeftMargin() {
        return labelLeftMargin;
    }

    public void setCheckMargin(int checkMargin) {
        this.checkMargin = checkMargin;
    }

    public void setCheckColor(Color checkColor) {
        this.checkColor = checkColor;
    }

    public void setLabelLeftMargin(int labelLeftMargin) {
        this.labelLeftMargin = labelLeftMargin;
    }

    public CheckboxStyle(WidgetStyle style) {
        super(style);
    }
}
