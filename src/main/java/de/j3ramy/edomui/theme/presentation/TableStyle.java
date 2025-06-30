package de.j3ramy.edomui.theme.presentation;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.theme.WidgetStyle;

import java.awt.*;

public class TableStyle extends ScrollableListStyle {

    public TableStyle(WidgetStyle style) {
        super(style);

        if (!(style instanceof TableStyle)) {
            this.setFontSize(FontSize.XS);
        }
    }
}