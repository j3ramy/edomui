package de.j3ramy.edomui.component.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;

public final class VerticalLine extends Widget {
    public VerticalLine(int x, int y, int thickness, int height, Color color) {
        super(x, y, thickness, height);

        this.getStyle().setBorderWidth(thickness);
        this.getStyle().setBackgroundColor(color);
    }

    public void render(PoseStack poseStack) {
        AbstractContainerScreen.fill(poseStack, this.getLeftPos(), this.getTopPos(), this.getLeftPos() + this.getStyle().getBorderWidth(),
                this.getTopPos() + this.getHeight(), this.getStyle().getBackgroundColor().getRGB());
    }
}
