package de.j3ramy.edomui.components.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;

public final class Circle extends Widget {

    public Circle(int xPos, int yPos, int radius, int color) {
        super(xPos - radius ,yPos - radius, radius * 2, radius * 2);

        this.getStyle().setBackgroundColor(color);
    }

    public Circle(Point pos, int radius, int color) {
        this(pos.x , pos.y, radius, color);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(this.isHidden()){
            return;
        }

        AbstractContainerScreen.fill(poseStack, this.getLeftPos(),
                this.getTopPos(),
                this.getLeftPos() + this.getWidth(),
                this.getTopPos() + this.getHeight(),
                this.getStyle().getBackgroundColor());
    }
}
