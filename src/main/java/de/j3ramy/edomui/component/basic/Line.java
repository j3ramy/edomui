package de.j3ramy.edomui.component.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.j3ramy.edomui.component.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;

public final class Line extends Widget {
    private final Point startPoint;
    private final float lineLength, rotationAngleInDeg;

    public Line(Point start, Point end, int lineWidth, Color color) {
        super(0, 0, 0, 0);

        this.startPoint = start;
        this.getStyle().setBorderWidth(lineWidth);
        this.getStyle().setBackgroundColor(color);

        int deltaX = end.x - start.x;
        int deltaY = start.y - end.y;

        this.lineLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        this.rotationAngleInDeg = - (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

    public Line(int x1, int y1, int x2, int y2, int lineWidth, Color color) {
        this(new Point(x1, y1), new Point(x2, y2), lineWidth, color);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (this.isHidden()) return;

        poseStack.pushPose();
        poseStack.translate(startPoint.x, startPoint.y, 0);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotationAngleInDeg));

        int yOffset = -(this.getStyle().getBorderWidth() / 2);
        AbstractContainerScreen.fill(poseStack, 0, yOffset, (int) lineLength, yOffset + this.getStyle().getBorderWidth(),
                this.getStyle().getBackgroundColor().getRGB());

        poseStack.popPose();
    }
}