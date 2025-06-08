package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;

public final class Line extends Widget {
    private final Point startPoint, endPoint;
    private final int color, lineWidth;
    private final float lineLength, rotationAngleInDeg;

    public Line(Point start, Point end, int lineWidth, int color) {
        super(0 ,0, 0, 0);

        this.startPoint = start;
        this.endPoint = end;
        this.lineWidth = Math.abs(lineWidth);
        this.color = color;

        final int deltaX = this.endPoint.x - this.startPoint.x;
        final int deltaY = this.startPoint.y - this.endPoint.y;

        this.lineLength = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        this.rotationAngleInDeg = (float) Math.toDegrees(Math.atan2(deltaY, deltaX)) * -1;
    }

    public Line(int x1, int y1, int x2, int y2, int lineWidth, int color) {
        this(new Point(x1, y1), new Point(x2, y2), lineWidth, color);
    }

    public Line(int x, int y, float lineLength, float rotationInDegrees, int lineWidth, int color) {
        super(0, 0, 0, 0);

        this.color = color;
        this.lineWidth = lineWidth;
        this.rotationAngleInDeg = rotationInDegrees * -1;
        this.lineLength = lineLength;

        final int deltaX = (int) (lineLength / 2 * Math.cos(Math.toRadians(this.rotationAngleInDeg)));
        final int deltaY = (int) (lineLength / 2 * Math.sin(Math.toRadians(this.rotationAngleInDeg)));

        this.startPoint = new Point(x - deltaX, y - deltaY);
        this.endPoint = new Point(x + deltaX, y + deltaY);
    }

    public Line(Point center, float lineLength, float rotationInDegrees, int lineWidth, int color) {
        this(center.x, center.y, lineLength, rotationInDegrees, lineWidth, color);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(this.isHidden()){
            return;
        }

        poseStack.pushPose();
        poseStack.translate(this.startPoint.x, this.startPoint.y, 0);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(this.rotationAngleInDeg));

        if(this.lineWidth % 2 == 0 || this.lineWidth == 1){
            AbstractContainerScreen.fill(poseStack, 0, 0,
                    (int) (this.lineLength), this.lineWidth, this.color);
        }
        else{
            AbstractContainerScreen.fill(poseStack, 0, -this.lineWidth / 2,
                    (int) (this.lineLength), this.lineWidth / 2, this.color);
        }

        poseStack.popPose();
    }
}
