package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.utils.GuiPresets;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public final class VerticalScrollbar extends Widget{
    private final int maxVisibleElements;
    private int currentScrollIndex, contentSize;

    public VerticalScrollbar(Widget widget, int contentSize, int maxVisibleElements){
        super(widget.getLeftPos(), widget.getTopPos(), widget.getWidth(), widget.getHeight());

        this.contentSize = contentSize;
        this.maxVisibleElements = maxVisibleElements;
    }

    @Override
    public void render(PoseStack poseStack) {
        if(this.isHidden()){
            return;
        }

        if(this.contentSize > 0){
            //track
            AbstractContainerScreen.fill(poseStack,
                    this.getLeftPos() + this.getWidth() - GuiPresets.SCROLLBAR_TRACK_WIDTH,
                    this.getTopPos(),
                    this.getLeftPos() + this.getWidth(),
                    this.getTopPos() + this.getHeight(),
                    this.getBorderColor());

            //thumb
            int thumbStep = this.getHeight() / this.contentSize;

            int invisibleElements = this.contentSize - this.maxVisibleElements;
            int thumbHeight = this.getHeight() - (thumbStep * invisibleElements);

            AbstractContainerScreen.fill(poseStack,
                    this.getLeftPos() + this.getWidth() - GuiPresets.SCROLLBAR_TRACK_WIDTH,
                    this.getTopPos() + thumbStep * this.currentScrollIndex,
                    this.getLeftPos() + this.getWidth(),
                    this.getTopPos() + thumbHeight + thumbStep * this.currentScrollIndex,
                    GuiPresets.SCROLLBAR_THUMB_COLOR);
        }
    }

    public void updateScrollIndex(int newScrollIndex){
        this.currentScrollIndex = newScrollIndex;
    }

    public void updateContentSize(int newContentSize){
        this.contentSize = newContentSize;
    }
}
