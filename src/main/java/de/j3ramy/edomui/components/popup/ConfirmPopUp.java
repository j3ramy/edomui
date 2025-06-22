
package de.j3ramy.edomui.components.popup;

import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.components.button.Button;

public final class ConfirmPopUp extends PopUp {
    private final Button confirmButton, cancelButton;

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public ConfirmPopUp(View hostView, int xPos, int yPos, String title, String content, String confirmButtonTitle, String cancelButtonTitle,
                        PopUpType type, IAction confirmAction){
        super(xPos - GuiPresets.POPUP_WIDTH / 2, yPos - GuiPresets.POPUP_HEIGHT / 2, title, content, type);

        int buttonY = this.getTopPos() + this.getHeight() - GuiPresets.POPUP_BUTTON_HEIGHT - GuiPresets.POPUP_BUTTON_MARGIN_BOTTOM;
        this.view.addWidget(this.confirmButton = new Button(this.getLeftPos() + GuiPresets.POPUP_BUTTON_MARGIN_X, buttonY,
                GuiPresets.POPUP_BUTTON_WIDTH, GuiPresets.POPUP_BUTTON_HEIGHT, confirmButtonTitle, () -> {
            confirmAction.execute();
            hostView.getWidgets().removeIf(w -> w == this);
        }));

        this.view.addWidget(this.cancelButton = new Button(this.getLeftPos() + this.getWidth() - GuiPresets.POPUP_BUTTON_WIDTH - GuiPresets.POPUP_BUTTON_MARGIN_X, buttonY,
                GuiPresets.POPUP_BUTTON_WIDTH, GuiPresets.POPUP_BUTTON_HEIGHT, cancelButtonTitle, () ->hostView.getWidgets().removeIf(w -> w == this)));
    }
}

