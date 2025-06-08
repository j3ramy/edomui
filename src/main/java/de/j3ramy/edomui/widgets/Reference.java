package de.j3ramy.edomui.widgets;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IAction;

public final class Reference extends Text {
    private final IAction clickAction;

    public Reference(int x, int y, String title, FontSize fontSize, int textColor, int hoverColor, IAction clickAction) {
        super(x, y, title, fontSize, 0, textColor, hoverColor, textColor);

        this.clickAction = clickAction;
        this.setHoverable(true);
    }

    @Override
    public void onClick(int mouseButton) {
        if(this.isMouseOver() && this.clickAction != null){
            this.clickAction.execute();
        }
    }
}
