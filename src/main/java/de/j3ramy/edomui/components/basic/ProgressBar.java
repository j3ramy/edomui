package de.j3ramy.edomui.components.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.components.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.util.Random;

public final class ProgressBar extends Widget {
    private static final float MAX_PROGRESS = 1f;

    private final IAction finishAction;
    private final int duration;

    private final float drawStopStart = new Random().nextFloat();
    private float drawStopEnd;

    private boolean isBarLagging = false;
    private boolean isRunning = true;

    private float progressDecimal = 0f;
    private float progressPixels = 0f;
    private float progressPixelStop = 0f;

    private int barColor;

    public ProgressBar(int x, int y, int width, int height, int duration, IAction finishAction, int barColor) {
        super(x, y, width, height);
        this.duration = duration;
        this.finishAction = finishAction;
        this.barColor = barColor;

        calculateDrawStopEnd();
    }

    public ProgressBar(int x, int y, int width, int height, int duration, IAction finishAction) {
        this(x, y, width, height, duration, finishAction, GuiPresets.PROGRESS_BAR_BAR_COLOR);
    }

    public ProgressBar(int x, int y, int width, int height) {
        this(x, y, width, height, 0, null, GuiPresets.PROGRESS_BAR_BAR_COLOR);
        this.isRunning = false;
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);

        int barEndX = getLeftPos() + (int) (isBarLagging ? progressPixelStop : progressPixels);
        AbstractContainerScreen.fill(poseStack, getLeftPos(), getTopPos(), barEndX, getTopPos() + getHeight(), barColor);
    }

    @Override
    public void update(int x, int y) {
        if (isHidden() || !isRunning) return;

        int fps = Minecraft.getInstance().options.framerateLimit;
        if (fps <= 0) fps = 60;

        progressDecimal += MAX_PROGRESS / duration / fps;
        progressPixels = progressDecimal * getWidth();

        isBarLagging = progressDecimal > drawStopStart && progressDecimal < drawStopEnd;
        if (!isBarLagging) {
            progressPixelStop = progressPixels;
        }

        if (progressDecimal >= MAX_PROGRESS) {
            isRunning = false;
            if (finishAction != null) finishAction.execute();
        }
    }

    public void setStatic() {
        isRunning = false;
    }

    public void setProgress(float progress) {
        this.progressDecimal = Math.max(0f, Math.min(progress, MAX_PROGRESS));
        this.progressPixels = progressDecimal * getWidth();
    }

    public void reset() {
        setProgress(0);
        isRunning = false;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isFull() {
        return progressDecimal >= MAX_PROGRESS;
    }

    public void setBarColor(int color) {
        this.barColor = color;
    }

    private void calculateDrawStopEnd() {
        Random random = new Random();
        do {
            drawStopEnd = random.nextFloat();
        } while (drawStopEnd < drawStopStart);
    }
}