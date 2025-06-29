package de.j3ramy.edomui.component.input;
import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.button.Button;
import de.j3ramy.edomui.component.text.VerticalCenteredText;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.theme.input.TextFieldStyle;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.util.style.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TextField extends Button {
    private final Set<Character> forbiddenCharacters = new HashSet<>();
    private final IValueAction onTextChangeAction, onPressEnterAction;
    private final String placeholder;

    protected final VerticalCenteredText visibleText;
    protected final TextFieldStyle textFieldStyle;
    protected final StringBuilder text = new StringBuilder();

    protected int maxLength = GuiPresets.TEXT_FIELD_CHAR_LIMIT;

    protected boolean focused = false;
    protected int caretTickCounter = 0;
    protected long lastClickTime = 0;
    protected int lastClickCaretChar = -1;
    protected int clickCount = 0;
    protected int selectionStart = -1;
    protected boolean caretVisible = true, touched;
    protected int caretCharPos = 0;
    protected int scrollOffset = 0;

    public TextField(int x, int y, int width, int height, String placeholder, @Nullable IValueAction onTextChange,
                     @Nullable IValueAction onEnter) {
        super(x, y, width, height, placeholder, null, ButtonType.TEXT_FIELD);
        this.placeholder = placeholder;
        this.onTextChangeAction = onTextChange;
        this.onPressEnterAction = onEnter;
        this.setHoverable(true);

        this.textFieldStyle = new TextFieldStyle(ThemeManager.getDefaultTextFieldStyle());
        this.setStyle(this.textFieldStyle);

        this.visibleText = new VerticalCenteredText(this.toRect(), x + this.textFieldStyle.getPadding(), "",
                this.textFieldStyle.getFontSize(), this.textFieldStyle.getTextColor());
        this.visibleText.disableTruncate();

        this.title = this.createTitle(ButtonType.TEXT_FIELD, placeholder, this.textFieldStyle.getPadding());

        this.getTitle().getStyle().setTextColor(this.textFieldStyle.getPlaceholderColor());
        this.getTitle().setHidden(false);
        this.getTitle().setLeftPos(x + this.textFieldStyle.getPadding());
    }

    public TextField(int x, int y, int width, int height, String placeholder, @Nullable IValueAction onTextChange) {
        this(x, y, width, height, placeholder, onTextChange, null);
    }

    public TextField(int x, int y, int width, int height, String placeholder) {
        this(x, y, width, height, placeholder, null, null);
    }

    public TextField(int x, int y, int width, int height) {
        this(x, y, width, height, "", null, null);
    }

    @Override
    public TextFieldStyle getStyle() {
        return this.textFieldStyle;
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;
        super.render(poseStack);

        int padding = this.textFieldStyle.getPadding();
        if (selectionStart != -1 && caretCharPos != selectionStart) {
            int from = Math.min(caretCharPos, selectionStart);
            int to = Math.max(caretCharPos, selectionStart);

            int clampedFrom = Math.max(from, scrollOffset);
            int clampedTo = Math.max(clampedFrom, Math.min(to, text.length()));

            int left = getLeftPos() + padding + Math.round(Minecraft.getInstance().font.width(
                    text.substring(scrollOffset, clampedFrom)) * GuiUtils.getFontScale(textFieldStyle.getFontSize()));

            int right = getLeftPos() + padding + Math.round(Minecraft.getInstance().font.width(
                    text.substring(scrollOffset, clampedTo)) * GuiUtils.getFontScale(textFieldStyle.getFontSize()));

            AbstractContainerScreen.fill(poseStack, left, getTopPos() + padding,
                    right, getTopPos() + getHeight() - padding,
                    this.textFieldStyle.getSelectionColor().getRGB());
        }

        visibleText.render(poseStack);

        if (focused && caretVisible) {
            int caretX = getCaretRenderX();
            AbstractContainerScreen.fill(poseStack, caretX, getTopPos() + padding,
                    caretX + 1, getTopPos() + getHeight() - padding, this.textFieldStyle.getTextColor().getRGB());
        }
    }

    @Override
    public void tick() {
        if (!focused) return;
        caretTickCounter++;
        if (caretTickCounter >= GuiPresets.CURSOR_BLINK_TICK_TIME) {
            caretVisible = !caretVisible;
            caretTickCounter = 0;
        }
    }

    @Override
    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);
        updateVisibleText();
    }

    @Override
    public void onClick(int mouseButton) {
        if (mouseButton != 0) return;

        if (isMouseOver()) {
            if (this.isEnabled()) {
                touched = true;
                focused = true;
                caretVisible = true;
                caretTickCounter = 0;
            }

            int clickX = getMousePosition().x - getLeftPos() - this.textFieldStyle.getPadding();
            float scale = GuiUtils.getFontScale(this.textFieldStyle.getFontSize());
            int scaledClickX = Math.round(clickX / scale);
            int clickedCharIndex = visibleText.getCharIndexFromPixel(scaledClickX);
            caretCharPos = Math.min(scrollOffset + clickedCharIndex, text.length());

            long now = System.currentTimeMillis();
            if (now - lastClickTime < GuiPresets.DOUBLE_CLICK_THRESHOLD_FAST && caretCharPos == lastClickCaretChar) {
                clickCount++;
            } else {
                clickCount = 1;
            }

            lastClickTime = now;
            lastClickCaretChar = caretCharPos;

            if (clickCount == 2) {
                selectWordAtCaret();
            } else if (clickCount >= 3) {
                selectAll();
            } else {
                selectionStart = -1;
            }

        } else {
            if (this.isEnabled()) {
                focused = false;
            }
        }
    }

    @Override
    public void charTyped(char c) {
        if (!focused || !isCharAllowed(c) || text.length() >= this.maxLength) return;

        deleteSelectionIfNeeded();
        text.insert(caretCharPos, c);
        caretCharPos++;

        triggerTextChanged();
    }
    @Override
    public void keyPressed(int keyCode) {
        if (!focused && selectionStart == -1) return;

        boolean ctrl = Screen.hasControlDown();
        boolean shift = Screen.hasShiftDown();

        if (!this.isEnabled()) {
            if (ctrl) {
                switch (keyCode) {
                    case 65 -> selectAll();  // Ctrl+A
                    case 67 -> copy();       // Ctrl+C
                }
            }
            return;
        }

        switch (keyCode) {
            case 259 -> backspace();
            case 261 -> delete();
            case 263 -> moveCaret(-1, shift);
            case 262 -> moveCaret(1, shift);
            case 268 -> moveCaretToStart(shift);
            case 269 -> moveCaretToEnd(shift);
            case 257 -> {
                if (onPressEnterAction != null) onPressEnterAction.execute(this.getText());
            }
            default -> {
                if (ctrl) {
                    if (keyCode == 65) selectAll(); // Ctrl+A
                    if (keyCode == 67) copy();     // Ctrl+C
                    if (keyCode == 88) cut();      // Ctrl+X
                    if (keyCode == 86) paste();    // Ctrl+V
                }
            }
        }
    }

    @Override
    public void setLeftPos(int newLeftPos) {
        int delta = newLeftPos - this.getLeftPos();

        super.setLeftPos(newLeftPos);

        if (visibleText != null) {
            visibleText.setLeftPos(visibleText.getLeftPos() + delta);
        }

        if (getTitle() != null) {
            getTitle().setLeftPos(getTitle().getLeftPos() + delta);
        }
    }

    @Override
    public void setTopPos(int newTopPos) {
        int delta = newTopPos - this.getTopPos();

        super.setTopPos(newTopPos);

        if (visibleText != null) {
            visibleText.setTopPos(visibleText.getTopPos() + delta);
        }

        if (getTitle() != null) {
            getTitle().setTopPos(getTitle().getTopPos() + delta);
        }
    }

    protected int getCaretRenderX() {
        String textBeforeCaret = text.substring(scrollOffset, caretCharPos);
        float scale = GuiUtils.getFontScale(this.textFieldStyle.getFontSize());
        int scaledWidth = Math.round(Minecraft.getInstance().font.width(textBeforeCaret) * scale);

        return getLeftPos() + textFieldStyle.getPadding() + scaledWidth;
    }

    protected void backspace() {
        if (selectionStart != -1 && caretCharPos != selectionStart) {
            deleteSelectionIfNeeded();
        } else if (caretCharPos > 0) {
            text.deleteCharAt(caretCharPos - 1);
            caretCharPos--;
        }
        triggerTextChanged();
    }

    protected void delete() {
        if (selectionStart != -1 && caretCharPos != selectionStart) {
            deleteSelectionIfNeeded();
        } else if (caretCharPos < text.length()) {
            text.deleteCharAt(caretCharPos);
        }
        triggerTextChanged();
    }

    protected void paste() {
        String clip = Minecraft.getInstance().keyboardHandler.getClipboard();
        if (clip.isEmpty()) return;
        deleteSelectionIfNeeded();

        for (char c : clip.toCharArray()) {
            if (isCharAllowed(c) && text.length() < this.maxLength) {
                text.insert(caretCharPos++, c);
            }
        }
        triggerTextChanged();
    }

    protected void updateVisibleText() {
        int fieldWidth = getWidth() - 2 * this.textFieldStyle.getPadding();
        String fullText = text.toString();
        float scale = GuiUtils.getFontScale(this.textFieldStyle.getFontSize());

        while (caretCharPos < scrollOffset && scrollOffset > 0) {
            scrollOffset--;
        }

        if (caretCharPos == scrollOffset && scrollOffset > 0) {
            int maxScrollBack = Math.min(5, scrollOffset);
            for (int i = 1; i <= maxScrollBack; i++) {
                int testOffset = scrollOffset - i;
                if (testOffset >= 0) {
                    Font font = Minecraft.getInstance().font;
                    String testText = fullText.substring(testOffset);
                    if (font.width(testText) * scale <= fieldWidth) {
                        scrollOffset = testOffset;
                    } else {
                        break;
                    }
                }
            }
        }

        Font font = Minecraft.getInstance().font;
        while (caretCharPos > scrollOffset &&
                font.width(fullText.substring(scrollOffset, caretCharPos)) * scale > fieldWidth) {
            scrollOffset++;
        }

        int end = scrollOffset;
        while (end < fullText.length() &&
                font.width(fullText.substring(scrollOffset, end + 1)) * scale <= fieldWidth) {
            end++;
        }

        String visible = fullText.substring(scrollOffset, end);
        visibleText.setText(visible);

        boolean isEmpty = text.isEmpty();
        getTitle().setHidden(!isEmpty);
        getTitle().getStyle().setTextColor(isEmpty ? this.textFieldStyle.getPlaceholderColor() : this.textFieldStyle.getTextColor());
    }

    protected void deleteSelectionIfNeeded() {
        if (selectionStart != -1 && caretCharPos != selectionStart) {
            int from = Math.min(caretCharPos, selectionStart);
            int to = Math.max(caretCharPos, selectionStart);
            text.delete(from, to);
            caretCharPos = from;
            selectionStart = -1;
        }
    }

    protected void selectAll() {
        caretCharPos = text.length();
        selectionStart = 0;
    }

    protected void triggerTextChanged() {
        updateVisibleText();
        if (onTextChangeAction != null) onTextChangeAction.execute(this.getText());
    }

    protected boolean isCharAllowed(char c) {
        return !forbiddenCharacters.contains(c);
    }

    protected void selectWordAtCaret() {
        if (text.isEmpty()) return;

        int start = caretCharPos;
        int end = caretCharPos;

        while (start > 0 && !Character.isWhitespace(text.charAt(start - 1))) {
            start--;
        }
        while (end < text.length() && !Character.isWhitespace(text.charAt(end))) {
            end++;
        }

        caretCharPos = end;
        selectionStart = start;
    }

    private void moveCaret(int delta, boolean shift) {
        if (shift && selectionStart == -1) selectionStart = caretCharPos;
        if (!shift) selectionStart = -1;

        caretCharPos = Math.max(0, Math.min(text.length(), caretCharPos + delta));
    }

    private void moveCaretToStart(boolean shift) {
        if (shift) selectionStart = caretCharPos;
        else selectionStart = -1;
        caretCharPos = 0;
    }

    private void moveCaretToEnd(boolean shift) {
        if (shift) selectionStart = caretCharPos;
        else selectionStart = -1;
        caretCharPos = text.length();
    }

    private void copy() {
        if (selectionStart != -1 && caretCharPos != selectionStart) {
            int from = Math.min(caretCharPos, selectionStart);
            int to = Math.max(caretCharPos, selectionStart);
            Minecraft.getInstance().keyboardHandler.setClipboard(text.substring(from, to));
        }
    }

    private void cut() {
        copy();
        deleteSelectionIfNeeded();
        triggerTextChanged();
    }

    public boolean isEmpty(){
        return this.text.isEmpty();
    }

    public void addForbiddenCharacter(char c) {
        this.forbiddenCharacters.add(c);
    }

    public void addForbiddenCharacters(char[] chars) {
        for (char c : chars) {
            this.forbiddenCharacters.add(c);
        }
    }

    public void clear() {
        setText("");
        this.getTitle().setText(this.placeholder);
    }

    public void setForbiddenCharacters(Set<Character> characters) {
        this.forbiddenCharacters.clear();
        this.forbiddenCharacters.addAll(characters);
    }

    public String getText() {
        return text.toString();
    }

    public void setText(String newText) {
        text.setLength(0);
        text.append(newText);
        caretCharPos = newText.length();
        selectionStart = -1;

        if (newText.isEmpty()) {
            getTitle().setText(placeholder);
        }

        updateVisibleText();
    }

    public boolean isFocused() {
        return focused;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setFocused() {
        this.focused = true;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }
}