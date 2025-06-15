package de.j3ramy.edomui.components.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public final class VerticalScrollbar extends Widget {
    private final int maxVisibleElements;
    private int currentScrollIndex, contentSize;

    public VerticalScrollbar(Widget widget, int contentSize, int maxVisibleElements) {
        super(widget.getLeftPos(), widget.getTopPos(), widget.getWidth(), widget.getHeight());
        this.contentSize = Math.max(0, contentSize);
        this.maxVisibleElements = Math.max(1, maxVisibleElements);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (this.isHidden()) return;
        if (contentSize <= 0 || getHeight() <= 0 || maxVisibleElements <= 0) return;

        int trackLeft = getLeftPos() + getWidth() - GuiPresets.SCROLLBAR_TRACK_WIDTH;
        int trackRight = getLeftPos() + getWidth();
        int trackTop = getTopPos();
        int trackBottom = getTopPos() + getHeight();

        AbstractContainerScreen.fill(poseStack, trackLeft, trackTop, trackRight, trackBottom, this.getStyle().getBorderColor());

        int invisibleElements = contentSize - maxVisibleElements;
        if (invisibleElements <= 0) return;

        int thumbStep = getHeight() / contentSize;
        int thumbHeight = getHeight() - (thumbStep * invisibleElements);
        thumbHeight = Math.max(4, thumbHeight);

        int thumbTop = getTopPos() + thumbStep * currentScrollIndex;
        int thumbBottom = thumbTop + thumbHeight;

        AbstractContainerScreen.fill(poseStack, trackLeft, thumbTop, trackRight, thumbBottom, GuiPresets.SCROLLBAR_THUMB_COLOR);
    }

    public void updateScrollIndex(int newScrollIndex) {
        this.currentScrollIndex = Math.min(Math.max(0, newScrollIndex), getMaxScrollIndex());
    }

    public void updateContentSize(int newContentSize) {
        this.contentSize = Math.max(0, newContentSize);
        this.currentScrollIndex = Math.min(currentScrollIndex, getMaxScrollIndex());
    }

    public int getCurrentScrollIndex() {
        return currentScrollIndex;
    }

    public int getMaxScrollIndex() {
        return Math.max(0, contentSize - maxVisibleElements);
    }
}
