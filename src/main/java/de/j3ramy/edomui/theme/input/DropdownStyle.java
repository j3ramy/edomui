package de.j3ramy.edomui.theme.input;

import de.j3ramy.edomui.util.style.WidgetStyle;

public class DropdownStyle extends InputStyle {
    private int maxVisibleElements = 4;
    private int optionHeight = 13;

    public int getMaxVisibleElements() {
        return maxVisibleElements;
    }

    public int getOptionHeight() {
        return optionHeight;
    }

    public void setMaxVisibleElements(int maxVisibleElements) {
        this.maxVisibleElements = maxVisibleElements;
    }

    public void setOptionHeight(int optionHeight) {
        this.optionHeight = optionHeight;
    }

    public DropdownStyle(WidgetStyle style) {
        super(style);
    }
}
