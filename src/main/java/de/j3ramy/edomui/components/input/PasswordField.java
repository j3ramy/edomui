package de.j3ramy.edomui.components.input;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.util.style.GuiPresets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

import javax.annotation.Nullable;

public final class PasswordField extends TextField {
    public PasswordField(int x, int y, int width, int height, String placeholderText, @Nullable IValueAction onTextChangeAction, @Nullable IValueAction onPressEnterAction) {
        super(x, y, width, height, placeholderText, FontSize.BASE, onTextChangeAction, onPressEnterAction);
    }

    public PasswordField(int x, int y, int width, int height, String placeholderText) {
        this(x, y, width, height, placeholderText, null, null);
    }

    @Override
    protected int getCaretRenderX() {
        int starsWidth = Minecraft.getInstance().font.width("*".repeat(Math.max(0, caretCharPos - scrollOffset)));
        return getLeftPos() + padding + starsWidth;
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.visibleText.setText("*".repeat(text.length()));
    }

    @Override
    protected void updateVisibleText() {
        String realText = getText();
        String masked = "*".repeat(realText.length());

        int fieldWidth = getWidth() - 2 * padding;

        while (caretCharPos < scrollOffset && scrollOffset > 0) scrollOffset--;

        Font font = Minecraft.getInstance().font;

        while (caretCharPos > scrollOffset &&
                font.width(masked.substring(scrollOffset, caretCharPos)) > fieldWidth) {
            scrollOffset++;
        }

        int end = scrollOffset;
        while (end < masked.length() &&
                font.width(masked.substring(scrollOffset, end + 1)) < fieldWidth) {
            end++;
        }

        visibleText.setText(masked.substring(scrollOffset, end));

        boolean isEmpty = realText.isEmpty();
        getTitle().setHidden(!isEmpty);
        getTitle().setTextColor(isEmpty ? GuiPresets.TEXT_FIELD_PLACEHOLDER_TEXT : visibleText.getTextColor());
    }
}
