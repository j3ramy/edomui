package de.j3ramy.edomui.widgets;

import de.j3ramy.edomui.interfaces.IAction;

import javax.annotation.Nullable;

public class PasswordField extends TextField {
    public PasswordField(int x, int y, int width, int height, String placeholderText, @Nullable IAction onTextChangeAction,
                         @Nullable IAction onPressEnterAction){
        super(x, y, width, height, placeholderText, onTextChangeAction, onPressEnterAction);
    }

    public PasswordField(int x, int y, int width, int height, String placeholderText){
        this(x, y, width, height, placeholderText, null, null);
    }

    @Override
    public void keyPressed(int keyCode) {
        if( this.isFocused()){
            super.keyPressed(keyCode);
            this.visibleText.setText("*".repeat(this.visibleText.getText().length()));
        }
    }

    @Override
    public void charTyped(char codePoint) {
        if(this.isFocused()){
            super.charTyped(codePoint);
            this.visibleText.setText("*".repeat(this.visibleText.getText().length()));
        }
    }
}

