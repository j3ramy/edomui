package de.j3ramy.edomui.components.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.j3ramy.edomui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;

public final class Line extends Widget {
    private final Point startPoint;
    private final int color, lineWidth;
    private final float lineLength, rotationAngleInDeg;

    public Line(Point start, Point end, int lineWidth, int color) {
        super(0, 0, 0, 0);

        this.startPoint = start;
        this.lineWidth = Math.abs(lineWidth);
        this.color = color;

        int deltaX = end.x - start.x;
        int deltaY = start.y - end.y;

        this.lineLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        this.rotationAngleInDeg = - (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

    public Line(int x1, int y1, int x2, int y2, int lineWidth, int color) {
        this(new Point(x1, y1), new Point(x2, y2), lineWidth, color);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (this.isHidden()) return;

        poseStack.pushPose();
        poseStack.translate(startPoint.x, startPoint.y, 0);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotationAngleInDeg));

        int yOffset = -(lineWidth / 2);
        AbstractContainerScreen.fill(poseStack, 0, yOffset, (int) lineLength, yOffset + lineWidth, color);

        poseStack.popPose();
    }
}