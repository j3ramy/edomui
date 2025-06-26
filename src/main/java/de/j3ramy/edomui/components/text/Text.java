package de.j3ramy.edomui.components.text;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.theme.text.TextStyle;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.util.style.GuiUtils;
import de.j3ramy.edomui.components.Widget;
import de.j3ramy.edomui.theme.ThemeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

import java.awt.*;


public class Text extends Widget {
    private final int maxTextWidth;
    private final TextStyle textStyle;

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

    public void setFontSize(FontSize fontSize) {
        this.textStyle.setFontSize(fontSize);
        autoWidth();
        autoHeight();
    }

    @Override
    public TextStyle getStyle() {
        return this.textStyle;
    }

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

        Color renderColor = isEnabled() ? (isHoverable() && isMouseOver() ? this.textStyle.getTextHoverColor() :
                this.textStyle.getTextColor()) : this.textStyle.getTextDisabledColor();
        float scale = GuiUtils.getFontScale(this.textStyle.getFontSize());
        float ratio = 1 / scale;

        String[] lines = text.toString().split("\n");

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);

        int yOffset = 0;
        for (String line : lines) {
            String label = truncateLabel ? GuiUtils.getTruncatedLabel(line, scale, maxTextWidth) : line;
            Minecraft.getInstance().font.draw(poseStack, label, getLeftPos() * ratio, (getTopPos() + yOffset) * ratio, renderColor.getRGB());
            yOffset += GuiPresets.LETTER_HEIGHT;
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
        setHeight((int) (GuiPresets.LETTER_HEIGHT * GuiUtils.getFontScale(this.textStyle.getFontSize()) * lines));
    }
}
