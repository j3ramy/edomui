package de.j3ramy.edomui.component.text;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.theme.text.TextStyle;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.util.style.GuiUtils;
import de.j3ramy.edomui.component.Widget;
import de.j3ramy.edomui.theme.ThemeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.lwjgl.glfw.GLFW;

import java.awt.*;


public class Text extends Widget {
    private final int maxTextWidth;
    private final TextStyle textStyle;

    private StringBuilder text;
    private boolean truncateLabel = true, isSelectable;
    private int caretCharPos = 0;
    private int selectionStart = -1;
    private long lastClickTime = 0;
    private int lastClickCaretChar = -1;
    private int clickCount = 0;

    public Text(int x, int y, String text, FontSize fontSize, int maxTextWidth, Color color, Color hoverColor, Color disabledColor) {
        super(x, y, 0, 0);
        this.textStyle = new TextStyle(ThemeManager.getDefaultTextStyle());
        this.setStyle(textStyle);

        this.text = new StringBuilder(text != null ? text : "");
        this.textStyle.setFontSize(fontSize);
        this.maxTextWidth = Math.max(maxTextWidth, 1);
        this.textStyle.setTextColor(color);
        this.textStyle.setTextDisabledColor(disabledColor);
        this.textStyle.setTextHoverColor(hoverColor);
        this.hideBackground();
        autoWidth();
        autoHeight();
    }

    public Text(int x, int y, String text, FontSize fontSize, Color color) {
        this(x, y, text, fontSize, GuiPresets.MAX_TEXT_LENGTH, color, color, color);
    }

    public Text(int x, int y, String text, FontSize fontSize, Color color, Color hoverColor) {
        this(x, y, text, fontSize, GuiPresets.MAX_TEXT_LENGTH, color, hoverColor, color);
    }

    public Text(int x, int y, String text, FontSize fontSize, Color color, Color hoverColor, Color disabledColor) {
        this(x, y, text, fontSize, GuiPresets.MAX_TEXT_LENGTH, color, hoverColor, disabledColor);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (this.isHidden()) return;

        super.render(poseStack);

        Color renderColor = textStyle.getTextColor();
        float scale = GuiUtils.getFontScale(this.textStyle.getFontSize());
        float ratio = 1 / scale;

        if (selectionStart != -1 && caretCharPos != selectionStart) {
            int from = Math.min(caretCharPos, selectionStart);
            int to = Math.max(caretCharPos, selectionStart);
            int left = getLeftPos() + (int) getSubstringTextWidth(0, from);
            int right = getLeftPos() + (int) getSubstringTextWidth(0, to);
            AbstractContainerScreen.fill(poseStack, left, getTopPos(), right, getTopPos() + (int)(GuiPresets.LETTER_HEIGHT * scale),
                    this.getStyle().getSelectionColor().getRGB());
        }

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        Minecraft.getInstance().font.draw(poseStack, text.toString(), getLeftPos() * ratio, getTopPos() * ratio, renderColor.getRGB());
        poseStack.popPose();
    }

    @Override
    public void onClick(int mouseButton) {
        if (mouseButton != 0 || !this.isSelectable || !this.isMouseOver()) {
            this.selectionStart = -1;
            return;
        }

        int mouseX = getMousePosition().x;
        int clickX = mouseX - getLeftPos();
        this.caretCharPos = getCharIndexFromPixel(clickX);

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
        } else {
            selectionStart = -1;
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        boolean ctrl = Screen.hasControlDown();
        if (ctrl && keyCode == GLFW.GLFW_KEY_C) {
            copy();
        }
    }

    @Override
    public TextStyle getStyle() {
        return this.textStyle;
    }

    private void copy() {
        if (selectionStart != -1 && caretCharPos != selectionStart) {
            int from = Math.min(caretCharPos, selectionStart);
            int to = Math.max(caretCharPos, selectionStart);
            Minecraft.getInstance().keyboardHandler.setClipboard(text.substring(from, to));
        }
    }

    private void selectWordAtCaret() {
        if (text.isEmpty()) {
            return;
        }

        int start = caretCharPos;
        int end = caretCharPos;

        while (start > 0 && !Character.isWhitespace(text.charAt(start - 1))) start--;
        while (end < text.length() && !Character.isWhitespace(text.charAt(end))) end++;

        caretCharPos = end;
        selectionStart = start;
    }

    private void autoHeight() {
        int lines = Math.max(1, text.toString().split("\n").length);
        setHeight((int) (GuiPresets.LETTER_HEIGHT * GuiUtils.getFontScale(this.textStyle.getFontSize()) * lines));
    }

    protected void autoWidth() {
        int maxLineWidth = 0;
        String[] lines = text.toString().split("\n");
        for (String line : lines) {
            int width = (int) getSubstringTextWidth(0, line.length());
            if (width > maxLineWidth) maxLineWidth = width;
        }
        setWidth(maxLineWidth);
    }

    public void disableTruncate() {
        this.truncateLabel = false;
    }

    public void clear() {
        setText("");
    }

    public int getTextWidth() {
        return text.isEmpty() ? 0 : (int) getSubstringTextWidth(0, text.length());
    }

    public float getSubstringTextWidth(int from, int to) {
        to = Math.min(to, text.length());
        float scale = GuiUtils.getFontScale(this.textStyle.getFontSize());
        String sub = text.substring(from, to);
        String result = truncateLabel ? GuiUtils.getTruncatedLabel(sub, scale, maxTextWidth) : sub;
        return Math.max(Minecraft.getInstance().font.width(result) * scale, 0);
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }

    public boolean isTruncated() {
        if (!truncateLabel) return false;
        String[] lines = text.toString().split("\n");
        float scale = GuiUtils.getFontScale(this.textStyle.getFontSize());

        for (String line : lines) {
            String truncatedLabel = GuiUtils.getTruncatedLabel(line, scale, maxTextWidth);
            if (truncatedLabel.length() < line.length()) {
                return true;
            }
        }

        return false;
    }

    public int getCharIndexFromPixel(int pixelX) {
        int totalWidth = 0;
        String text = String.valueOf(this.getString());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int charWidth = this.getFont().width(String.valueOf(c));
            if (pixelX < totalWidth + charWidth / 2f) {
                return i;
            }
            totalWidth += charWidth;
        }
        return text.length();
    }

    public Font getFont() {
        return Minecraft.getInstance().font;
    }

    public void centerHorizontally(java.awt.Rectangle rect) {
        setLeftPos(rect.x + rect.width / 2 - getWidth() / 2);
    }

    public void centerVertically(java.awt.Rectangle rect) {
        setTopPos(rect.y + rect.height / 2 - getHeight() / 2);
    }

    public void setText(String text) {
        this.text = new StringBuilder(text != null ? text : "");
        autoWidth();
        autoHeight();
    }

    public StringBuilder getString() {
        return text;
    }

    public void setFontSize(FontSize fontSize) {
        this.textStyle.setFontSize(fontSize);
        autoWidth();
        autoHeight();
    }

    public void setSelectable(){
        this.isSelectable = true;
    }
}