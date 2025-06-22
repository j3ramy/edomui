package de.j3ramy.edomui.components.popup;

import de.j3ramy.edomui.components.input.PasswordField;
import de.j3ramy.edomui.components.input.TextArea;
import de.j3ramy.edomui.components.input.TextField;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.components.button.Button;

public final class InputPopUp extends PopUp {
    private final Button confirmButton;
    private final TextField inputField;

    public void addForbiddenCharacter(char[] chars) {
        this.inputField.addForbiddenCharacters(chars);
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public TextField getInputField() {
        return inputField;
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      String inputValue, String inputPlaceholder, PopUpType popUpType, IValueAction confirmAction, boolean isPassword) {
        super(xPos - GuiPresets.POPUP_WIDTH / 2, yPos - GuiPresets.POPUP_HEIGHT / 2, title, "", popUpType);

        int inputX = this.getLeftPos() + this.getWidth() / 2 - 45;
        int inputY = this.getTopPos() + this.getHeight() / 2 - 7;

        if (isPassword) {
            this.view.addWidget(this.inputField = new PasswordField(inputX, inputY, 90, 13, inputPlaceholder));
        } else {
            this.view.addWidget(this.inputField = new TextField(inputX, inputY, 90, 13, inputPlaceholder));
        }

        if (!inputValue.isEmpty()) {
            this.inputField.setText(inputValue);
        }

        this.inputField.setFocused();

        int buttonY = this.getTopPos() + this.getHeight() - GuiPresets.POPUP_BUTTON_HEIGHT - GuiPresets.POPUP_BUTTON_MARGIN_BOTTOM;

        this.view.addWidget(this.confirmButton = new Button(
                this.getLeftPos() + GuiPresets.POPUP_BUTTON_MARGIN_X,
                buttonY,
                GuiPresets.POPUP_BUTTON_WIDTH,
                GuiPresets.POPUP_BUTTON_HEIGHT,
                confirmButtonTitle,
                () -> {
                    confirmAction.execute(this.inputField.getText());
                    hostView.getWidgets().remove(this);
                }
        ));

        this.view.addWidget(new Button(
                this.getLeftPos() + this.getWidth() - GuiPresets.POPUP_BUTTON_WIDTH - GuiPresets.POPUP_BUTTON_MARGIN_X,
                buttonY,
                GuiPresets.POPUP_BUTTON_WIDTH,
                GuiPresets.POPUP_BUTTON_HEIGHT,
                cancelButtonTitle,
                () -> hostView.getWidgets().remove(this)
        ));

        this.view.getWidgets().removeIf(widget -> widget instanceof TextArea);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      String inputValue, String inputPlaceholder, PopUpType popUpType, IValueAction confirmAction){
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, inputValue, inputPlaceholder, popUpType, confirmAction, false);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle, IValueAction confirmAction) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, "", "", PopUpType.NOTICE, confirmAction, false);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      IValueAction confirmAction, boolean isPassword) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, "", "", PopUpType.NOTICE, confirmAction, isPassword);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      String inputValue, IValueAction confirmAction) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, inputValue, "", PopUpType.NOTICE, confirmAction, false);
    }

    public InputPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                      String inputValue, String inputPlaceholder, IValueAction confirmAction) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, inputValue, inputPlaceholder, PopUpType.NOTICE, confirmAction, false);
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
}