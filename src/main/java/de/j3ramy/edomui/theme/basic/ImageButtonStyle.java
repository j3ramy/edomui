package de.j3ramy.edomui.theme.basic;

import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public class ImageButtonStyle extends ButtonStyle {
    public ImageButtonStyle(WidgetStyle style) {
        super(style);

        if (!(style instanceof ImageButtonStyle)) {
            this.setBackgroundColor(Color.LIGHT_GRAY);
            this.setHoverBackgroundColor(Color.GRAY);
        }
    }
}
