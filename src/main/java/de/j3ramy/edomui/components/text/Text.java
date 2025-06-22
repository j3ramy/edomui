package de.j3ramy.edomui.components.text;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.GuiUtils;
import de.j3ramy.edomui.components.Widget;
import de.j3ramy.edomui.theme.ThemeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;


public class Text extends Widget {
    private static final int LETTER_HEIGHT = 7;

    private final int maxTextWidth;

    private FontSize fontSize;
    private int textColor;
    private int hoverTextColor;
    private int disabledTextColor;
    private StringBuilder text;
    private boolean truncateLabel = true;

    public void setText(String text) {
        this.text = new StringBuilder(text != null ? text : "");
        autoWidth();
        autoHeight();
    }

    public StringBuilder getString() {
        return text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
        autoWidth();
        autoHeight();
    }

    public int getHoverTextColor() {
        return hoverTextColor;
    }

    public void setHoverTextColor(int hoverTextColor) {
        this.hoverTextColor = hoverTextColor;
    }

    public int getDisabledTextColor() {
        return disabledTextColor;
    }

    public void setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
    }

    public Text(int x, int y, String text, FontSize fontSize, int maxTextWidth, int color, int hoverColor, int disabledColor) {
        super(x, y, 0, 0);
        this.text = new StringBuilder(text != null ? text : "");
        this.fontSize = fontSize;
        this.maxTextWidth = Math.max(maxTextWidth, 1);
        this.textColor = color;
        this.hoverTextColor = hoverColor;
        this.disabledTextColor = disabledColor;
        this.hideBackground();
        autoWidth();
        autoHeight();
    }

    public Text(int x, int y, String text, FontSize fontSize) {
        this(x, y, text, fontSize, 2048, Color.WHITE, Color.WHITE, Color.GRAY);
    }

    public Text(int x, int y, String text, FontSize fontSize, int color) {
        this(x, y, text, fontSize, 2048, color, color, color);
    }

    public Text(int x, int y, String text, FontSize fontSize, int color, int hoverColor) {
        this(x, y, text, fontSize, 2048, color, hoverColor, color);
    }

    public Text(int x, int y, String text, FontSize fontSize, int color, int hoverColor, int disabledColor) {
        this(x, y, text, fontSize, 2048, color, hoverColor, disabledColor);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (this.isHidden()) return;

        super.render(poseStack);

        int renderColor = isEnabled() ? (isHoverable() && isMouseOver() ? hoverTextColor : textColor) : disabledTextColor;
        float scale = GuiUtils.getFontScale(this.fontSize);
        float ratio = 1 / scale;

        String[] lines = text.toString().split("\n");

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);

        int yOffset = 0;
        for (String line : lines) {
            String label = truncateLabel ? GuiUtils.getTruncatedLabel(line, scale, maxTextWidth) : line;
            Minecraft.getInstance().font.draw(poseStack, label, getLeftPos() * ratio, (getTopPos() + yOffset) * ratio, renderColor);
            yOffset += LETTER_HEIGHT;
        }

        poseStack.popPose();
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
        float scale = GuiUtils.getFontScale(this.fontSize);
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
        float scale = GuiUtils.getFontScale(this.fontSize);

        for (String line : lines) {
            int fullWidth = Minecraft.getInstance().font.width(line);
            if (fullWidth * scale > maxTextWidth) {
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

    protected void autoWidth() {
        int maxLineWidth = 0;
        String[] lines = text.toString().split("\n");
        for (String line : lines) {
            int width = (int) getSubstringTextWidth(0, line.length());
            if (width > maxLineWidth) maxLineWidth = width;
        }
        setWidth(maxLineWidth);
    }

    private void autoHeight() {
        int lines = Math.max(1, text.toString().split("\n").length);
        setHeight((int) (LETTER_HEIGHT * GuiUtils.getFontScale(this.fontSize) * lines));
    }
}
