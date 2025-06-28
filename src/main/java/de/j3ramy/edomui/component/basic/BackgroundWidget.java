package de.j3ramy.edomui.component.basic;

import de.j3ramy.edomui.component.Widget;

import java.awt.*;

public final class BackgroundWidget extends Widget {
    public BackgroundWidget(int xPos, int yPos, int width, int height, Color backgroundColor, Color borderColor){
        super(xPos, yPos, width, height);
        this.getStyle().setBackgroundColor(backgroundColor);
        this.getStyle().setBorderColor(borderColor);
    }

    public BackgroundWidget(int xPos, int yPos, int width, int height, Color backgroundColor) {
        super(xPos, yPos, width, height);
        this.getStyle().setBackgroundColor(backgroundColor);
        this.noBorder();
    }
}
