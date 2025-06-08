
package de.j3ramy.edomui.widgets;

import de.j3ramy.edomui.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.utils.GuiPresets;

public final class ConfirmPopUp extends PopUp {
    public ConfirmPopUp(View view, int xPos, int yPos, String title, String content, String confirmButtonTitle, String cancelButtonTitle,
                        PopUpType type, IAction confirmAction){
        super(xPos - GuiPresets.POPUP_WIDTH / 2, yPos - GuiPresets.POPUP_HEIGHT / 2, title, content, type);

        this.view.addWidget(new Button(this.getLeftPos() + GuiPresets.POPUP_BUTTON_MARGIN_X,
                this.getTopPos() + this.getHeight() - GuiPresets.POPUP_BUTTON_HEIGHT - GuiPresets.POPUP_BUTTON_MARGIN_BOTTOM,
                GuiPresets.POPUP_BUTTON_WIDTH, GuiPresets.POPUP_BUTTON_HEIGHT, confirmButtonTitle, () -> {
            confirmAction.execute();
            view.getWidgets().remove(this);
        }));

        this.view.addWidget(new Button(this.getLeftPos() + this.getWidth() - GuiPresets.POPUP_BUTTON_WIDTH - GuiPresets.POPUP_BUTTON_MARGIN_X,
                this.getTopPos() + this.getHeight() - GuiPresets.POPUP_BUTTON_HEIGHT - GuiPresets.POPUP_BUTTON_MARGIN_BOTTOM,
                GuiPresets.POPUP_BUTTON_WIDTH, GuiPresets.POPUP_BUTTON_HEIGHT, cancelButtonTitle, () -> view.getWidgets().remove(this)));
    }
}

