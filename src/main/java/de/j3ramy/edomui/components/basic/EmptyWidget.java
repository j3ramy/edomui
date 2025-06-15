package de.j3ramy.edomui.components.basic;

import de.j3ramy.edomui.components.Widget;

public final class EmptyWidget extends Widget {
    public EmptyWidget(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);
    }

    public EmptyWidget(){
        this(0, 0, 0, 0);
    }
}
