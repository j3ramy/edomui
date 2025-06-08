package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public final class HorizontalLine extends Widget {
    private final int thickness, color;


    public HorizontalLine(int x, int y, int width, int thickness, int color) {
        super(x, y, width, thickness);

        this.thickness = this.getHeight();
        this.color = color;
    }

    @Override
    public void render(PoseStack matrixStack) {
        if(this.isHidden()){
            return;
        }

        AbstractContainerScreen.fill(matrixStack, this.getLeftPos(), this.getTopPos(), this.getLeftPos() + this.getWidth(),
                this.getTopPos() + this.thickness, this.color);
    }
}
