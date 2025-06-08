package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.GuiUtils;
import net.minecraft.client.Minecraft;

public class Text extends Widget {
    private static final int LETTER_HEIGHT = 7;

    private final FontSize fontSize;
    private final int maxTextWidth; //in pixels

    private int textColor;
    private int hoverTextColor;
    private int disabledTextColor;
    private StringBuilder text;
    private boolean truncateLabel = true;

    public void setText(String text) {
        this.text = new StringBuilder(text);
    }

    public StringBuilder getText() {
        return text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
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

    public Text(int xPos, int yPos, String text, FontSize fontSize, int maxTextWidth, int color, int hoverColor, int disabledColor){
        super(xPos, yPos, 0 ,0);

        if(text == null){
            text = "";
        }

        this.text = new StringBuilder(text);
        this.fontSize = fontSize;

        if(maxTextWidth == 0){
            this.maxTextWidth = 2048;
        }
        else{
            this.maxTextWidth = maxTextWidth;
        }

        this.autoWidth();
        this.autoHeight();

        this.textColor = color;
        this.hoverTextColor = hoverColor;
        this.disabledTextColor =  disabledColor;
        this.setBackgroundColor(Color.GRAY);
        this.setShowBackground(false);
    }

    public Text(int xPos, int yPos, String text, FontSize fontSize, int textColor){
        this(xPos, yPos, text, fontSize, 0, textColor, textColor, textColor);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(!this.isHidden()){
            super.render(poseStack);

            int textColor = this.textColor;
            if(!this.isEnabled()){
                textColor = this.disabledTextColor;
            }
            else if(this.isMouseOver() && this.isHoverable()){
                textColor = this.hoverTextColor;
            }

            poseStack.pushPose();
            poseStack.scale(this.getFontSizeScale(), this.getFontSizeScale(), this.getFontSizeScale());
            Minecraft.getInstance().font.draw(poseStack,
                    this.truncateLabel ? GuiUtils.getTruncatedLabel(this.text.toString(), this.getFontSizeScale(), this.maxTextWidth) : this.text.toString(),
                    this.getLeftPos() * this.getFontSizeRatio(), this.getTopPos() * this.getFontSizeRatio(), textColor);
            poseStack.popPose();
        }
    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);
    }

    public void disableTruncate(){
        this.truncateLabel = false;
    }

    public void clear(){
        this.setText("");
    }

    public int getTextWidth(){
        if(this.text.isEmpty()){
            return 0;
        }
        else{
            return (int) this.getSubstringTextWidth(0, this.text.length());
        }
    }

    public float getSubstringTextWidth(int from, int to){
        if(to > this.text.length()){
            to = this.text.length();
        }

        float length;
        if(this.truncateLabel){
            final String truncatedLabel = GuiUtils.getTruncatedLabel(this.text.substring(from, to), this.getFontSizeScale(), this.maxTextWidth);
            length = Minecraft.getInstance().font.width(truncatedLabel) * this.getFontSizeScale();
        }
        else{
            length = Minecraft.getInstance().font.width(this.text.substring(from, to)) * this.getFontSizeScale();
        }

        return Math.max(length, 0);
    }

    public float getFontSizeScale(){
        switch (this.fontSize){
            case XXS -> {
                return 0.4f;
            }
            case XS -> {
                return 0.5f;
            }
            case S -> {
                return 0.75f;
            }
            case L -> {
                return 1.5f;
            }
            default -> {
                return 1f;
            }
        }
    }

    public boolean isEmpty(){
        return this.text.isEmpty();
    }

    protected void centerHorizontally(java.awt.Rectangle rectangle){
        this.setLeftPos((rectangle.x + rectangle.width / 2 - this.getWidth() / 2));
    }

    protected void centerVertically(java.awt.Rectangle rectangle){
        this.setTopPos((rectangle.y + rectangle.height / 2 - this.getHeight() / 2));
    }

    private float getFontSizeRatio(){
        return 1 / this.getFontSizeScale();
    }

    private float getTextHeight(){
        return LETTER_HEIGHT * this.getFontSizeScale();
    }

    protected void autoWidth(){
        this.setWidth(this.getTextWidth());
    }

    private void autoHeight(){
        this.setHeight((int) (this.getTextHeight()));
    }
}
