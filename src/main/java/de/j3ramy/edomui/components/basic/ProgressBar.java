package de.j3ramy.edomui.components.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.theme.ProgressBarStyle;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.components.Widget;
import de.j3ramy.edomui.util.style.GuiPresets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.util.Random;

public final class ProgressBar extends Widget {
    private final IAction finishAction;
    private final int duration;
    private final float drawStopStart = new Random().nextFloat();
    private final ProgressBarStyle progressBarStyle;

    private float drawStopEnd;
    private boolean isBarLagging = false;
    private boolean isRunning = true;
    private float progressDecimal = 0f;
    private float progressPixels = 0f;
    private float progressPixelStop = 0f;

    public ProgressBar(int x, int y, int width, int height, int duration, IAction finishAction) {
        super(x, y, width, height);
        this.duration = duration;
        this.finishAction = finishAction;

        this.progressBarStyle = new ProgressBarStyle(ThemeManager.getDefaultProgressBarStyle());
        this.setStyle(this.progressBarStyle);

        calculateDrawStopEnd();
    }

    public ProgressBar(int x, int y, int width, int height) {
        this(x, y, width, height, 0, null);
        this.isRunning = false;
    }

    @Override
    public ProgressBarStyle getStyle() {
        return this.progressBarStyle;
    }

    @Override
    public void render(PoseStack poseStack) {
        if (isHidden()) return;

        super.render(poseStack);

        int barEndX = getLeftPos() + (int) (isBarLagging ? progressPixelStop : progressPixels);
        AbstractContainerScreen.fill(poseStack, getLeftPos(), getTopPos(), barEndX, getTopPos() + getHeight(),
                this.progressBarStyle.getBarColor().getRGB());
    }

    @Override
    public void update(int x, int y) {
        if (isHidden() || !isRunning) return;

        int fps = Minecraft.getInstance().options.framerateLimit;
        if (fps <= 0) fps = 60;

        progressDecimal += GuiPresets.MAX_PROGRESS_BAR_VALUE / duration / fps;
        progressPixels = progressDecimal * getWidth();

        isBarLagging = progressDecimal > drawStopStart && progressDecimal < drawStopEnd;
        if (!isBarLagging) {
            progressPixelStop = progressPixels;
        }

        if (progressDecimal >= GuiPresets.MAX_PROGRESS_BAR_VALUE) {
            isRunning = false;
            if (finishAction != null) finishAction.execute();
        }
    }

    public void setStatic() {
        isRunning = false;
    }

    public void setProgress(float progress) {
        this.progressDecimal = Math.max(0f, Math.min(progress, GuiPresets.MAX_PROGRESS_BAR_VALUE));
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
        return progressDecimal >= GuiPresets.MAX_PROGRESS_BAR_VALUE;
    }

    private void calculateDrawStopEnd() {
        Random random = new Random();
        do {
            drawStopEnd = random.nextFloat();
        } while (drawStopEnd < drawStopStart);
    }
}