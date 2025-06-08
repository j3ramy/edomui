package de.j3ramy.edomui.widgets;

public final class Rectangle extends Widget {
    public Rectangle(int x, int y, int width, int height, int color){
        super(x, y, width, height);
        this.setBackgroundColor(color);
        this.noBorder();
    }
}

