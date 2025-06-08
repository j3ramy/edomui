package de.j3ramy.edomui.widgets;

import de.j3ramy.edomui.interfaces.IAction;

import javax.annotation.Nullable;

public class NumberField extends TextField {
    private boolean isFloatInputEnabled, allowNegative;

    public boolean isFloatInputEnabled() {
        return isFloatInputEnabled;
    }

    public void setFloatInputEnabled(boolean floatInputEnabled) {
        isFloatInputEnabled = floatInputEnabled;
    }

    public boolean isAllowNegative() {
        return allowNegative;
    }

    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    public NumberField(int x, int y, int width, int height, String placeholderText, @Nullable IAction onTextChangeAction,
                       @Nullable IAction onPressEnterAction){
        super(x, y, width, height, placeholderText, onTextChangeAction, onPressEnterAction);

        this.setMaxLength(10);
    }

    public NumberField(int x, int y, int width, int height, String placeholderText){
        this(x, y, width, height, placeholderText, null, null);
    }

    @Override
    public void charTyped(char codePoint) {
        if(this.isFocused()){
            if(codePoint == '.' && this.getText().contains(".")){
                return;
            }

            if(this.isFloatInputEnabled && this.getText().contains(".")){
                String[] numberParts = this.getText().split("\\.");
                if(numberParts.length > 1 && numberParts[1].length() == 2){
                    return;
                }
            }

            if(NumberField.isNumeric(codePoint, this.isFloatInputEnabled, this.allowNegative)){
                super.charTyped(codePoint);
            }
        }
    }

    private static boolean isNumeric(char c, boolean isDecimalAllowed, boolean isNegativeAllowed) {
        boolean isDigit = c >= '0' && c <= '9';

        return isDigit || isDecimalAllowed && (c == '.' || c == ':' || c == ' ') || isNegativeAllowed && c == '-';
    }
}

