package de.j3ramy.edomui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.WidgetState;
import de.j3ramy.edomui.interfaces.IWidget;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.util.style.WidgetStyle;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;
import java.awt.Rectangle;

public abstract class Widget implements IWidget {
    private final Point mousePosition = new Point();

    private WidgetStyle style = ThemeManager.getGlobalDefaultStyle();
    private int width, height, leftPos, topPos;
    private boolean isEnabled = true;
    private boolean isHoverable;
    private boolean isHidden;
    private boolean showBackground = true;

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getLeftPos() { return leftPos; }
    public void setLeftPos(int leftPos) { this.leftPos = leftPos; }

    public int getTopPos() { return topPos; }
    public void setTopPos(int topPos) { this.topPos = topPos; }

    public void hideBackground() { this.showBackground = false; }
    public boolean isShowBackground() { return showBackground; }

    public void setEnabled(boolean enabled) { isEnabled = enabled; }
    public boolean isEnabled() { return isEnabled; }

    public boolean isHoverable() { return isHoverable; }
    public void setHoverable(boolean hoverable) { isHoverable = hoverable; }

    public boolean isHidden() { return isHidden; }

    public void setHidden(boolean hidden) {
        this.isHidden = hidden;
        this.mousePosition.setLocation(0, 0);
    }

    public Point getMousePosition() { return mousePosition; }

    public WidgetStyle getStyle() { return style; }

    public void setStyle(WidgetStyle style) {
        this.style = style;
    }

    public Widget(int x, int y, int width, int height) {
        this.leftPos = x;
        this.topPos = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden || !showBackground) return;
        renderBorder(poseStack);
        renderBackground(poseStack);
    }

    @Override public void update(int x, int y) {
        if (!isHidden) mousePosition.setLocation(x, y);
    }

    @Override public void tick() {}
    @Override public void onClick(int mouseButton) {}
    @Override public void onMouseDrag(int mouseButton, double newX, double newY, int mouseX, int mouseY) {}
    @Override public void keyPressed(int keyCode) {}
    @Override public void charTyped(char codePoint) {}
    @Override public void onScroll(double delta) {}

    public boolean isMouseOver() {
        return mousePosition.x >= leftPos && mousePosition.x < leftPos + width &&
                mousePosition.y >= topPos && mousePosition.y < topPos + height;
    }

    public Rectangle toRect() {
        return new Rectangle(leftPos, topPos, width, height);
    }

    public void noBorder() { style.setBorderWidth(0); }

    public WidgetState getCurrentState() {
        if (!isEnabled) return WidgetState.DISABLED;
        if (isHoverable && isMouseOver()) return WidgetState.HOVERED;
        return WidgetState.NORMAL;
    }

    protected void renderBorder(PoseStack poseStack) {
        WidgetState state = getCurrentState();
        int color = style.getBorderColorForState(state);
        int bw = style.getBorderWidth();
        this.fill(poseStack,
                leftPos - bw, topPos - bw,
                leftPos + width + bw, topPos + height + bw,
                color);
    }

    public void renderBackground(PoseStack poseStack) {
        WidgetState state = getCurrentState();
        int color = style.getBackgroundColorForState(state);
        this.fill(poseStack,
                leftPos, topPos,
                leftPos + width, topPos + height,
                color);
    }

    private void fill(PoseStack stack, int left, int top, int right, int bottom, int color) {
        AbstractContainerScreen.fill(stack, left, top, right, bottom, color);
    }
}
