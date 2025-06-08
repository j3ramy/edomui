
package de.j3ramy.edomui.widgets;

import de.j3ramy.edomui.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.interfaces.IInputPopupAction;
import de.j3ramy.edomui.utils.GuiPresets;

public final class InputPopUp extends PopUp {
    private final Button confirmButton;
    private final TextField inputField;

    public void addForbiddenCharacter(char[] chars){
        this.inputField.addForbiddenCharacter(chars);
    }

    public InputPopUp(View view, int xPos, int yPos, String title, String confirmButtonTitle, String cancelButtonTitle, String inputValue,
                      String inputPlaceholder, IInputPopupAction confirmAction, boolean isPassword){
        super(xPos - GuiPresets.POPUP_WIDTH / 2, yPos - GuiPresets.POPUP_HEIGHT / 2, title, "", PopUpType.NOTICE);

        if(isPassword){
            this.view.addWidget(this.inputField = new PasswordField( this.getLeftPos() + this.getWidth() / 2 - 45,
                    this.getTopPos() + this.getHeight() / 2 - 7, 90, 13, inputPlaceholder));
        }
        else{
            this.view.addWidget(this.inputField = new TextField( this.getLeftPos() + this.getWidth() / 2 - 45,
                    this.getTopPos() + this.getHeight() / 2 - 7, 90, 13, inputPlaceholder));
        }

        if(!inputValue.isEmpty()){
            this.inputField.setText(inputValue);
        }
        this.inputField.setFocused(true);

        this.view.addWidget(this.confirmButton = new Button(this.getLeftPos() + GuiPresets.POPUP_BUTTON_MARGIN_X,
                this.getTopPos() + this.getHeight() - GuiPresets.POPUP_BUTTON_HEIGHT - GuiPresets.POPUP_BUTTON_MARGIN_BOTTOM,
                GuiPresets.POPUP_BUTTON_WIDTH, GuiPresets.POPUP_BUTTON_HEIGHT, confirmButtonTitle, () -> {
            confirmAction.execute(this.inputField.getText());
            view.getWidgets().remove(this);
        }));

        this.view.addWidget(new Button(this.getLeftPos() + this.getWidth() - GuiPresets.POPUP_BUTTON_WIDTH - GuiPresets.POPUP_BUTTON_MARGIN_X,
                this.getTopPos() + this.getHeight() - GuiPresets.POPUP_BUTTON_HEIGHT - GuiPresets.POPUP_BUTTON_MARGIN_BOTTOM,
                GuiPresets.POPUP_BUTTON_WIDTH, GuiPresets.POPUP_BUTTON_HEIGHT, cancelButtonTitle, () -> view.getWidgets().remove(this)));

        for(Widget widget : this.view.getWidgets()){
            if(widget instanceof TextArea){
                this.view.removeWidget(widget);
            }
        }
    }

    @Override
    public void update(int x, int y) {
        if(this.isHidden()){
            return;
        }

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

