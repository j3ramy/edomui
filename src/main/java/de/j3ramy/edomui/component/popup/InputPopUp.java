package de.j3ramy.edomui.component.popup;

import de.j3ramy.edomui.component.input.PasswordField;
import de.j3ramy.edomui.component.input.TextField;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.component.button.Button;

public final class InputPopUp extends PopUp {
    private final Button confirmButton;
    private final Button cancelButton;
    private final TextField inputField;

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      String inputValue, String inputPlaceholder, PopUpType popUpType, IValueAction confirmAction, IAction cancelAction, boolean isPassword) {
        super(0, 0, title, "", popUpType);

        int centeredX = xPos - this.getWidth() / 2;
        int centeredY = yPos - this.getHeight() / 2;
        this.setLeftPos(centeredX);
        this.setTopPos(centeredY);

        int inputX = this.getLeftPos() + (this.getWidth() - this.popUpStyle.getInputWidth()) / 2;
        int inputY = this.getTopPos() + this.popUpStyle.getTitleHeight() + this.popUpStyle.getMargin();

        if (isPassword) {
            this.inputField = new PasswordField(inputX, inputY, this.popUpStyle.getInputWidth(),
                    this.popUpStyle.getWidgetHeight(), inputPlaceholder);
        } else {
            this.inputField = new TextField(inputX, inputY, this.popUpStyle.getInputWidth(),
                    this.popUpStyle.getWidgetHeight(), inputPlaceholder);
        }

        if (!inputValue.isEmpty()) {
            this.inputField.setText(inputValue);
        }

        this.inputField.setFocused();
        this.view.addWidget(this.inputField);

        int buttonY = this.getTopPos() + this.getHeight() - this.popUpStyle.getWidgetHeight() - this.popUpStyle.getMargin();

        this.confirmButton = new Button(this.getLeftPos() + this.popUpStyle.getMargin(), buttonY, this.popUpStyle.getButtonWidth(),
                this.popUpStyle.getWidgetHeight(), confirmButtonTitle, () -> {
                    confirmAction.execute(this.inputField.getText());
                    hostView.getWidgets().remove(this);
                }
        );

        this.cancelButton = new Button(
                this.getLeftPos() + this.getWidth() - this.popUpStyle.getButtonWidth() - this.popUpStyle.getMargin(), buttonY, this.popUpStyle.getButtonWidth(),
                this.popUpStyle.getWidgetHeight(), cancelButtonTitle, () -> {
                    cancelAction.execute();
                    hostView.getWidgets().remove(this);
                }
        );

        this.view.addWidget(this.confirmButton);
        this.view.addWidget(this.cancelButton);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      String inputValue, String inputPlaceholder, PopUpType popUpType, IValueAction confirmAction){
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, inputValue, inputPlaceholder, popUpType, confirmAction, null, false);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle, IValueAction confirmAction) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, "", "", PopUpType.NOTICE, confirmAction, null, false);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      IValueAction confirmAction, boolean isPassword) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, "", "", PopUpType.NOTICE, confirmAction, null, isPassword);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      String inputValue, IValueAction confirmAction) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, inputValue, "", PopUpType.NOTICE, confirmAction, null, false);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      String inputValue, String inputPlaceholder, IValueAction confirmAction) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, inputValue, inputPlaceholder, PopUpType.NOTICE, confirmAction, null, false);
    }

    @Override
    public void update(int x, int y) {
        if (this.isHidden()) return;

        super.update(x, y);
        this.confirmButton.setEnabled(!this.inputField.isEmpty());
    }

    @Override
    public void charTyped(char codePoint) {
        super.charTyped(codePoint);
        this.view.charTyped(codePoint);
    }

    @Override
    public void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        this.view.keyPressed(keyCode);
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public TextField getInputField() {
        return inputField;
    }
}