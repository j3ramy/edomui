package de.j3ramy.edomui.component.popup;

import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.component.button.Button;

public final class AlertPopUp extends PopUp {
    private final Button closeButton;

    public AlertPopUp(View hostView, int xPos, int yPos, String title, String content, String buttonTitle, PopUpType type) {
        super(xPos, yPos, title, content, type);

        int buttonX = this.getLeftPos() + (this.getWidth() - this.popUpStyle.getButtonWidth()) / 2;
        int buttonY = this.getTopPos() + this.getHeight() - this.popUpStyle.getWidgetHeight() - this.popUpStyle.getMargin();

        this.closeButton = new Button(buttonX, buttonY, this.popUpStyle.getButtonWidth(), this.popUpStyle.getWidgetHeight(), buttonTitle,
                () -> hostView.getWidgets().removeIf(w -> w == this)
        );

        this.view.addWidget(this.closeButton);
    }

    public AlertPopUp(View hostView, int xPos, int yPos, String title, String content, String buttonTitle) {
        this(hostView, xPos, yPos, title, content, buttonTitle, PopUpType.DEFAULT);
    }

    public Button getCloseButton() {
        return closeButton;
    }
}

