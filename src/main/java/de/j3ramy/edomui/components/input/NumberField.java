package de.j3ramy.edomui.components.input;

import de.j3ramy.edomui.interfaces.IValueAction;

import javax.annotation.Nullable;

public final class NumberField extends TextField {
    private boolean isFloatInputEnabled, allowNegative;

    public boolean isFloatInput() {
        return isFloatInputEnabled;
    }

    public void setFloatInput() {
        isFloatInputEnabled = true;
    }

    public boolean isAllowNegative() {
        return allowNegative;
    }

    public void allowNegative() {
        this.allowNegative = true;
    }

    public NumberField(int x, int y, int width, int height, String placeholderText, @Nullable IValueAction onTextChangeAction,
                       @Nullable IValueAction onPressEnterAction){
        super(x, y, width, height, placeholderText, onTextChangeAction, onPressEnterAction);
        this.setMaxLength(10);
    }

    public NumberField(int x, int y, int width, int height, String placeholderText, @Nullable IValueAction onTextChangeAction){
        this(x, y, width, height, placeholderText, onTextChangeAction, null);
    }

    public NumberField(int x, int y, int width, int height, String placeholderText){
        this(x, y, width, height, placeholderText, null, null);
    }

    public NumberField(int x, int y, int width, int height){
        this(x, y, width, height, "", null, null);
    }

    @Override
    public void charTyped(char codePoint) {
        if (this.isFocused()) {
            String c = String.valueOf(codePoint);

            if (!isCharAllowed(c)) return;

            if (codePoint == '.' && this.getText().contains(".")) return;

            if (isFloatInputEnabled && this.getText().contains(".")) {
                String[] parts = this.getText().split("\\.");
                if (parts.length > 1 && parts[1].length() == 2) return;
            }

            super.charTyped(codePoint);
        }
    }

    private boolean isCharAllowed(String c) {
        if (c.matches("[0-9]")) return true;
        if (c.equals(".") && isFloatInputEnabled) return true;
        return c.equals("-") && allowNegative && this.getText().isEmpty();
    }
}