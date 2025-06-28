package de.j3ramy.edomui.component.popup;

import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.component.button.Button;

public final class ConfirmPopUp extends PopUp {
    private final Button confirmButton, cancelButton;

    public ConfirmPopUp(View hostView, int xPos, int yPos, String title, String content, String confirmButtonTitle, String cancelButtonTitle,
                        PopUpType type, IAction confirmAction){
        super(0, 0, title, type);

        int centeredX = xPos - this.getWidth() / 2;
        int centeredY = yPos - this.getHeight() / 2;
        this.setLeftPos(centeredX);
        this.setTopPos(centeredY);

        int buttonY = this.getTopPos() + this.getHeight() - this.popUpStyle.getWidgetHeight() - this.popUpStyle.getMargin();

        this.confirmButton = new Button(this.getLeftPos() + this.popUpStyle.getMargin(), buttonY, this.popUpStyle.getButtonWidth(),
                this.popUpStyle.getWidgetHeight(), confirmButtonTitle, () -> {
                    confirmAction.execute();
                    hostView.getWidgets().removeIf(w -> w == this);
                }
        );

        this.cancelButton = new Button(this.getLeftPos() + this.getWidth() - this.popUpStyle.getButtonWidth() - this.popUpStyle.getMargin(),
                buttonY, this.popUpStyle.getButtonWidth(), this.popUpStyle.getWidgetHeight(), cancelButtonTitle,
                () -> hostView.getWidgets().removeIf(w -> w == this)
        );

        this.view.addWidget(this.confirmButton);
        this.view.addWidget(this.cancelButton);

        this.addContent(content);
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}