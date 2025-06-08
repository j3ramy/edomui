package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.interfaces.IWidget;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.GuiPresets;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;
import java.awt.Rectangle;

public abstract class Widget implements IWidget {
    private final Point mousePosition = new Point();

    private int width, height, leftPos, topPos, borderWidth = GuiPresets.DEFAULT_BORDER_WIDTH;
    private int backgroundColor = Color.WHITE,  borderColor = Color.DARK_GRAY, hoverBackgroundColor = Color.GRAY,
            hoverBorderColor = Color.DARK_GRAY, disabledBackgroundColor = Color.DARK_GRAY, disabledBorderColor = Color.BLACK;
    private boolean isEnabled = true, isHoverable, isHidden, showBackground = true;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLeftPos() {
        return leftPos;
    }

    public void setLeftPos(int leftPos) {
        this.leftPos = leftPos;
    }

    public int getTopPos() {
        return topPos;
    }

    public void setTopPos(int topPos) {
        this.topPos = topPos;
    }

    public void setShowBackground(boolean showBackground) {
        this.showBackground = showBackground;
    }

    public boolean isShowBackground() {
        return showBackground;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isHoverable() {
        return isHoverable;
    }

    public void setHoverable(boolean hoverable) {
        isHoverable = hoverable;
    }

    public void setHidden(boolean isHidden){
        this.isHidden = isHidden;
        this.mousePosition.setLocation(0, 0);
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void noBorder(){
        this.borderWidth = 0;
    }

    public Point getMousePosition() {
        return mousePosition;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }


    public int getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setDisabledBackgroundColor(int disabledBackgroundColor) {
        this.disabledBackgroundColor = disabledBackgroundColor;
    }

    public void setDisabledBorderColor(int disabledBorderColor) {
        this.disabledBorderColor = disabledBorderColor;
    }

    public void setHoverBackgroundColor(int hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public void setHoverBorderColor(int hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor;
    }

    public int getDisabledBorderColor() {
        return disabledBorderColor;
    }

    public int getDisabledBackgroundColor() {
        return disabledBackgroundColor;
    }

    public int getHoverBorderColor() {
        return hoverBorderColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public Widget(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.leftPos = x;
        this.topPos = y;
    }

    @Override
    public void update(int x, int y) {
        if(this.isHidden()){
            return;
        }

        this.mousePosition.x = x;
        this.mousePosition.y = y;
    }

    @Override
    public void tick() {}

    @Override
    public void render(PoseStack poseStack) {
        if(this.isHidden()){
            return;
        }

        if(this.showBackground){
            this.renderBorder(poseStack);
            this.renderBackground(poseStack);
        }
    }

    @Override
    public void onClick(int mouseButton) {}

    @Override
    public void onMouseDrag(int mouseButton, double newX, double newY, int mouseX, int mouseY) {}

    @Override
    public void keyPressed(int keyCode) {}

    @Override
    public void charTyped(char codePoint) {}

    @Override
    public void onScroll(double delta) {}

    public Rectangle toRect(){
        return new Rectangle(this.getLeftPos(), this.getTopPos(), this.getWidth(), this.getHeight());
    }

    public boolean isMouseOver(){
        return this.mousePosition.x >= this.leftPos && this.mousePosition.x < this.leftPos + this.width &&
                this.mousePosition.y >= this.topPos && this.mousePosition.y < this.topPos + this.height;
    }

    private void renderBorder(PoseStack poseStack){
        int borderColor = this.borderColor;
        if(!this.isEnabled){
            borderColor = this.getDisabledBorderColor();
        }
        else if(this.isMouseOver() && this.isHoverable){
            borderColor = this.getHoverBorderColor();
        }

        AbstractContainerScreen.fill(poseStack, this.getLeftPos() - this.borderWidth, this.getTopPos() - this.borderWidth,
                this.getLeftPos() + this.getWidth() + this.borderWidth, this.getTopPos() + this.getHeight() + this.borderWidth, borderColor);
    }

    private void renderBackground(PoseStack poseStack){
        int backgroundColor = this.backgroundColor;
        if(!this.isEnabled){
            backgroundColor = this.getDisabledBackgroundColor();
        }
        else if(this.isMouseOver() && this.isHoverable){
            backgroundColor = this.getHoverBackgroundColor();
        }

        AbstractContainerScreen.fill(poseStack, this.getLeftPos(), this.getTopPos(), this.getLeftPos() + this.getWidth(),
                this.getTopPos() + this.getHeight(), backgroundColor);
    }
}
