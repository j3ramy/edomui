package de.j3ramy.edomui.theme.presentation;

import de.j3ramy.edomui.theme.input.InputStyle;
import de.j3ramy.edomui.theme.WidgetStyle;

public class GridStyle extends InputStyle {
    public GridStyle(WidgetStyle style) {
        super(style);

        if (!(style instanceof GridStyle)) {
            this.setPadding(5);
        }
    }
}
