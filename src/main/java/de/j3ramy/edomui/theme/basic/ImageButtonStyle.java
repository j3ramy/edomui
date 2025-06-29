package de.j3ramy.edomui.theme.basic;

import de.j3ramy.edomui.util.style.WidgetStyle;

import java.awt.*;

public class ImageButtonStyle extends ButtonStyle {
    public ImageButtonStyle(WidgetStyle style) {
        super(style);

        this.setBackgroundColor(Color.LIGHT_GRAY);
        this.setHoverBackgroundColor(Color.GRAY);
    }
}
