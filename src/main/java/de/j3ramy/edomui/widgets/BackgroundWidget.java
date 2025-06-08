package de.j3ramy.edomui.widgets;

public final class BackgroundWidget extends Widget{
    public BackgroundWidget(int xPos, int yPos, int width, int height, int backgroundColor, int borderColor){
        super(xPos, yPos, width, height);
        this.setBackgroundColor(backgroundColor);
        this.setBorderColor(borderColor);
    }
}
