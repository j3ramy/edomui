package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.utils.GuiPresets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.util.Random;

public final class ProgressBar extends Widget {
    private final float maxProgress = 1f, drawStopStart = new Random().nextFloat();
    private final IAction finishAction;
    private final int duration;

    private boolean isBarLagging, isRunning;
    private float progressAsDecimal, progressAsPixels, progressStop, drawStopEnd;
    private int barColor;

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public ProgressBar(int xPos, int yPos, int width, int height, int duration, IAction finishAction, int barColor){
        super(xPos, yPos, width, height);

        this.barColor = barColor;
        this.duration = duration;
        this.finishAction = finishAction;
        this.isRunning = true;

        this.setBackgroundColor(GuiPresets.PROGRESS_BAR_BACKGROUND);
        this.setBorderColor(GuiPresets.PROGRESS_BAR_BORDER);

        this.calculateProgressStopInterval();
    }

    public ProgressBar(int xPos, int yPos, int width, int height, int duration, IAction finishAction){
        this(xPos, yPos, width, height, duration, finishAction, GuiPresets.PROGRESS_BAR_BAR);
    }

    public ProgressBar(int xPos, int yPos, int width, int height){
        this(xPos, yPos, width, height, 0, null, GuiPresets.PROGRESS_BAR_BAR);

        this.setStatic();
    }

    @Override
    public void render(PoseStack poseStack){
        if(this.isHidden()){
            return;
        }

        super.render(poseStack);

        AbstractContainerScreen.fill(poseStack, getLeftPos(), getTopPos(), (int) (getLeftPos() + (this.isBarLagging ? this.progressStop : this.progressAsPixels)),
                getTopPos() + getHeight(), this.barColor);
    }

    @Override
    public void update(int x, int y) {
        if(this.isHidden() || !this.isRunning){
            return;
        }

        int frameRate = Minecraft.getInstance().options.framerateLimit;
        this.progressAsDecimal += this.maxProgress / this.duration / frameRate;

        this.isBarLagging = this.progressAsDecimal > this.drawStopStart && this.progressAsDecimal < this.drawStopEnd;

        this.progressAsPixels = this.progressAsDecimal / this.maxProgress * getWidth();
        if(!this.isBarLagging){
            this.progressStop = this.progressAsPixels;
        }

        if(this.isFull() && this.isRunning){
            if(this.finishAction != null){
                this.finishAction.execute();
            }

            this.isRunning = false;
        }
    }

    public void setStatic() {
        this.isRunning = false;
    }

    public void setProgress(float progress) {
        this.progressAsDecimal = Math.max(0, Math.min(progress, maxProgress)); // Clamp to 0-1 range
        this.progressAsPixels = this.progressAsDecimal * getWidth();
    }

    public void reset(){
        this.setProgress(0);
        this.isRunning = false;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isFull(){
        return this.progressAsDecimal >= this.maxProgress;
    }

    private void calculateProgressStopInterval(){
        Random random = new Random();
        do {
            this.drawStopEnd = random.nextFloat();
        } while (this.drawStopEnd < this.drawStopStart);
    }
}
