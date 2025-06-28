package de.j3ramy.edomui.component.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;

public final class HorizontalLine extends Widget {
    public HorizontalLine(int x, int y, int width, int thickness, Color color) {
        super(x, y, width, thickness);

        this.getStyle().setBackgroundColor(color);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (this.isHidden()) return;

        AbstractContainerScreen.fill(poseStack, getLeftPos(), getTopPos(), getLeftPos() + getWidth(), getTopPos() + getHeight(),
                this.getStyle().getBackgroundColor().getRGB()
        );
    }
}
