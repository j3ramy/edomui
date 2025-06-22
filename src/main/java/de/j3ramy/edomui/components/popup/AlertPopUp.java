package de.j3ramy.edomui.components.popup;

import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.components.button.Button;

public final class AlertPopUp extends PopUp {
    private final Button closeButton;

    public Button getCloseButton() {
        return closeButton;
    }

    public AlertPopUp(View hostView, int xPos, int yPos, String title, String content, String buttonTitle, PopUpType type) {
        super(xPos - GuiPresets.POPUP_WIDTH / 2, yPos - GuiPresets.POPUP_HEIGHT / 2, title, content, type);

        this.view.addWidget(this.closeButton = new Button((this.getLeftPos() + this.getWidth() / 2) - GuiPresets.POPUP_BUTTON_WIDTH / 2,
                this.getTopPos() + this.getHeight() - GuiPresets.POPUP_BUTTON_HEIGHT - GuiPresets.POPUP_BUTTON_MARGIN_BOTTOM,
                GuiPresets.POPUP_BUTTON_WIDTH, GuiPresets.POPUP_BUTTON_HEIGHT, buttonTitle, () -> hostView.getWidgets().removeIf(w -> w == this)));
    }

    public AlertPopUp(View hostView, int xPos, int yPos, String title, String content, String buttonTitle) {
        this(hostView, xPos, yPos, title, content, buttonTitle, PopUpType.DEFAULT);
    }
}

