package de.j3ramy.edomui.component.popup;

import de.j3ramy.edomui.component.button.Button;
import de.j3ramy.edomui.component.input.Dropdown;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.view.View;

import java.awt.*;
import java.util.ArrayList;

public final class DropdownPopUp extends PopUp {
    private final Button confirmButton;
    private final Button cancelButton;
    private final Dropdown dropdown;

    public DropdownPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                         ArrayList<String> dropdownValues, String inputPlaceholder, PopUpType popUpType, IValueAction confirmAction, boolean isPassword) {
        super(xPos, yPos, title, "", popUpType);

        int inputX = this.getLeftPos() + (this.getWidth() - this.popUpStyle.getInputWidth()) / 2;
        int inputY = this.getTopPos() + this.popUpStyle.getTitleHeight() + this.popUpStyle.getMargin();

        this.dropdown = new Dropdown(inputX, inputY, this.popUpStyle.getInputWidth(), this.popUpStyle.getWidgetHeight(), inputPlaceholder);

        if (dropdownValues != null && !dropdownValues.isEmpty()) {
            this.dropdown.setOptions(dropdownValues, Color.LIGHT_GRAY);
            this.dropdown.setOption(dropdownValues.get(0));
        }
        else{
            this.dropdown.setEnabled(false);
        }

        this.view.addWidget(this.dropdown);

        int buttonY = this.getTopPos() + this.getHeight() - this.popUpStyle.getWidgetHeight() - this.popUpStyle.getMargin();

        this.confirmButton = new Button(this.getLeftPos() + this.popUpStyle.getMargin(), buttonY, this.popUpStyle.getButtonWidth(),
                this.popUpStyle.getWidgetHeight(), confirmButtonTitle, () -> {
                    confirmAction.execute(this.dropdown.getSelectedText());
                    hostView.getWidgets().remove(this);
                }
        );

        this.cancelButton = new Button(
                this.getLeftPos() + this.getWidth() - this.popUpStyle.getButtonWidth() - this.popUpStyle.getMargin(), buttonY, this.popUpStyle.getButtonWidth(),
                this.popUpStyle.getWidgetHeight(), cancelButtonTitle, () -> hostView.getWidgets().remove(this)
        );

        this.view.addWidget(this.confirmButton);
        this.view.addWidget(this.cancelButton);
    }

    public DropdownPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                         ArrayList<String> dropdownValues, String inputPlaceholder, PopUpType popUpType, IValueAction confirmAction){
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, dropdownValues, inputPlaceholder, popUpType, confirmAction, false);
    }

    public DropdownPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                         ArrayList<String> dropdownValues, IValueAction confirmAction) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, dropdownValues, "", PopUpType.NOTICE, confirmAction, false);
    }

    public DropdownPopUp(View hostView, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle,
                         ArrayList<String> dropdownValues, String inputPlaceholder, IValueAction confirmAction) {
        this(hostView, xPos, yPos, title, confirmButtonTitle, cancelButtonTitle, dropdownValues, inputPlaceholder, PopUpType.NOTICE, confirmAction, false);
    }

    @Override
    public void update(int x, int y) {
        if (this.isHidden()) return;

        super.update(x, y);
        this.confirmButton.setEnabled(this.dropdown.hasSelection());
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

    public Dropdown getDropdown() {
        return dropdown;
    }
}