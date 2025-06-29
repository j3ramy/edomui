package de.j3ramy.edomui.theme.presentation;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.WidgetStyle;

import java.awt.*;

public class TableStyle extends ScrollableListStyle {

    public TableStyle(WidgetStyle style) {
        super(style);

        this.setFontSize(FontSize.XS);
    }

    @Override
    public void setSelectionColor(Color selectionColor) {
        super.setSelectionColor(selectionColor);
    }
}