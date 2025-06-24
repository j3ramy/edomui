package de.j3ramy.edomui.components.popup;

import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.components.button.Button;

public final class AlertPopUp extends PopUp {
    private final Button closeButton;

    public Button getCloseButton() {
        return closeButton;
    }

    public AlertPopUp(View hostView, int xPos, int yPos, String title, String content, String buttonTitle, PopUpType type) {
        super(0, 0, title, type);

        int centeredX = xPos - this.getWidth() / 2;
        int centeredY = yPos - this.getHeight() / 2;
        this.setLeftPos(centeredX);
        this.setTopPos(centeredY);

        int buttonX = this.getLeftPos() + (this.getWidth() - this.popUpStyle.getButtonWidth()) / 2;
        int buttonY = this.getTopPos() + this.getHeight() - this.popUpStyle.getWidgetHeight() - this.popUpStyle.getMargin();

        this.closeButton = new Button(
                buttonX, buttonY,
                this.popUpStyle.getButtonWidth(),
                this.popUpStyle.getWidgetHeight(),
                buttonTitle,
                () -> hostView.getWidgets().removeIf(w -> w == this)
        );

        this.view.addWidget(this.closeButton);

        this.addContent(content);
    }

    public AlertPopUp(View hostView, int xPos, int yPos, String title, String content, String buttonTitle) {
        this(hostView, xPos, yPos, title, content, buttonTitle, PopUpType.DEFAULT);
    }
}

