package de.j3ramy.edomui.components.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public final class HorizontalLine extends Widget {
    private final int color;

    public HorizontalLine(int x, int y, int width, int thickness, int color) {
        super(x, y, width, thickness);
        this.color = color;
    }

    @Override
    public void render(PoseStack poseStack) {
        if (this.isHidden()) return;

        AbstractContainerScreen.fill(
                poseStack,
                getLeftPos(),
                getTopPos(),
                getLeftPos() + getWidth(),
                getTopPos() + getHeight(),
                color
        );
    }
}
