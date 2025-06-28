package de.j3ramy.edomui.component.input;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.util.style.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import javax.annotation.Nullable;

public final class PasswordField extends TextField {
    public PasswordField(int x, int y, int width, int height, String placeholderText, @Nullable IValueAction onTextChangeAction,
                         @Nullable IValueAction onPressEnterAction) {
        super(x, y, width, height, placeholderText, onTextChangeAction, onPressEnterAction);
    }

    public PasswordField(int x, int y, int width, int height, String placeholderText, @Nullable IValueAction onTextChangeAction) {
        super(x, y, width, height, placeholderText, onTextChangeAction, null);
    }

    public PasswordField(int x, int y, int width, int height, String placeholderText) {
        this(x, y, width, height, placeholderText, null, null);
    }

    public PasswordField(int x, int y, int width, int height) {
        this(x, y, width, height, "", null, null);
    }

    @Override
    protected int getCaretRenderX() {
        String maskedTextBeforeCaret = "*".repeat(Math.max(0, caretCharPos - scrollOffset));
        float scale = GuiUtils.getFontScale(this.textFieldStyle.getFontSize());
        int scaledWidth = Math.round(Minecraft.getInstance().font.width(maskedTextBeforeCaret) * scale);

        return getLeftPos() + this.textFieldStyle.getPadding() + scaledWidth;
    }

    @Override
    protected void updateVisibleText() {
        String realText = getText();
        String maskedText = "*".repeat(realText.length());

        int fieldWidth = getWidth() - 2 * this.textFieldStyle.getPadding();

        while (caretCharPos < scrollOffset && scrollOffset > 0) {
            scrollOffset--;
        }

        while (caretCharPos > scrollOffset &&
                Minecraft.getInstance().font.width(maskedText.substring(scrollOffset, caretCharPos)) *
                        GuiUtils.getFontScale(this.textFieldStyle.getFontSize()) > fieldWidth) {
            scrollOffset++;
        }

        int end = scrollOffset;
        while (end < maskedText.length() &&
                Minecraft.getInstance().font.width(maskedText.substring(scrollOffset, end + 1)) *
                        GuiUtils.getFontScale(this.textFieldStyle.getFontSize()) <= fieldWidth) {
            end++;
        }

        visibleText.setText(maskedText.substring(scrollOffset, end));

        boolean isEmpty = realText.isEmpty();
        getTitle().setHidden(!isEmpty);
        getTitle().getStyle().setTextColor(isEmpty ? this.textFieldStyle.getPlaceholderColor() : visibleText.getStyle().getTextColor());
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        if (this.isShowBackground()) {
            renderBorder(poseStack);
            renderBackground(poseStack);
        }

        if (title != null) {
            title.render(poseStack);
        }

        int padding = this.textFieldStyle.getPadding();
        if (selectionStart != -1 && caretCharPos != selectionStart) {
            int from = Math.min(caretCharPos, selectionStart);
            int to = Math.max(caretCharPos, selectionStart);

            int clampedFrom = Math.max(from, scrollOffset);
            int clampedTo = Math.max(clampedFrom, Math.min(to, text.length()));

            String maskedTextFromStart = "*".repeat(clampedFrom - scrollOffset);
            String maskedTextToStart = "*".repeat(clampedTo - scrollOffset);

            int left = getLeftPos() + padding + Math.round(Minecraft.getInstance().font.width(
                    maskedTextFromStart) * GuiUtils.getFontScale(textFieldStyle.getFontSize()));

            int right = getLeftPos() + padding + Math.round(Minecraft.getInstance().font.width(
                    maskedTextToStart) * GuiUtils.getFontScale(textFieldStyle.getFontSize()));

            AbstractContainerScreen.fill(poseStack, left, getTopPos() + padding,
                    right, getTopPos() + getHeight() - padding,
                    this.textFieldStyle.getSelectionColor().getRGB());
        }

        visibleText.render(poseStack);
        if (this.isFocused() && caretVisible) {
            int caretX = getCaretRenderX();
            AbstractContainerScreen.fill(poseStack, caretX, getTopPos() + padding,
                    caretX + 1, getTopPos() + getHeight() - padding, this.textFieldStyle.getTextColor().getRGB());
        }
    }
}