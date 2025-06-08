package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public final class VerticalLine extends Widget {
    private final int thickness, color;
    public VerticalLine(int x, int y, int thickness, int height, int color) {
        super(x, y, thickness, height);

        this.thickness = this.getWidth();
        this.color = color;
    }

    public void render(PoseStack poseStack) {
        AbstractContainerScreen.fill(poseStack, this.getLeftPos(), this.getTopPos(), this.getLeftPos() + this.thickness,
                this.getTopPos() + this.getHeight(), this.color);
    }
}
