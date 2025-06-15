package de.j3ramy.edomui.components.basic;

import de.j3ramy.edomui.components.Widget;

public final class BackgroundWidget extends Widget {
    public BackgroundWidget(int xPos, int yPos, int width, int height, int backgroundColor, int borderColor){
        super(xPos, yPos, width, height);
        this.getStyle().setBackgroundColor(backgroundColor);
        this.getStyle().setBorderColor(borderColor);
    }

    public BackgroundWidget(int xPos, int yPos, int width, int height, int backgroundColor) {
        super(xPos, yPos, width, height);
        this.getStyle().setBackgroundColor(backgroundColor);
        this.noBorder();
    }
}
