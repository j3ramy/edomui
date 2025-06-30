package de.j3ramy.edomui.theme.input;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.theme.basic.ButtonStyle;
import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public class CheckboxStyle extends ButtonStyle {
   private int checkMargin = 2;
   private int labelLeftMargin = 5;
   private Color checkColor = Color.BLACK;

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

        if (style instanceof CheckboxStyle other) {
            this.checkMargin = other.checkMargin;
            this.labelLeftMargin = other.labelLeftMargin;
            this.checkColor = other.checkColor;
        } else {
            this.setFontSize(FontSize.S);
        }
    }
}
