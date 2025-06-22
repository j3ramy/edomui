package de.j3ramy.edomui.components.popup;

import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.components.basic.ProgressBar;

public final class ProgressPopUp extends PopUp {
    private final IAction finishAction;
    private final ProgressBar progressBar;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public ProgressPopUp(View hostView, int xPos, int yPos, String title, String content,
                         PopUpType type, int duration, IAction finishAction, int barColor) {
        super(xPos, yPos, title, content, type);

        this.finishAction = finishAction;

        int barX = this.getLeftPos() + this.getWidth() / 2 - GuiPresets.POPUP_PROGRESS_BAR_WIDTH / 2;
        int barY = this.getTopPos() + this.getHeight() - GuiPresets.POPUP_PROGRESS_BAR_HEIGHT - GuiPresets.POPUP_PROGRESS_BAR_MARGIN_BOTTOM;
        view.addWidget(this.progressBar = new ProgressBar(barX, barY, GuiPresets.POPUP_PROGRESS_BAR_WIDTH, GuiPresets.POPUP_PROGRESS_BAR_HEIGHT, duration,
                () -> {
                    this.finishAction.execute();
                    hostView.getWidgets().remove(this);
                }));
    }

    public ProgressPopUp(View hostView, int xPos, int yPos, String title, String content, PopUpType type, int duration, IAction finishAction) {
        this(hostView, xPos, yPos, title, content, type, duration, finishAction, GuiPresets.PROGRESS_BAR_BAR_COLOR);
    }

    public ProgressPopUp(View hostView, int xPos, int yPos, String title, String content, int duration, IAction finishAction) {
        this(hostView, xPos, yPos, title, content, PopUpType.NOTICE, duration, finishAction, GuiPresets.PROGRESS_BAR_BAR_COLOR);
    }

    public ProgressPopUp(View hostView, int xPos, int yPos, String title, int duration, IAction finishAction) {
        this(hostView, xPos, yPos, title, "", duration, finishAction);
    }

    public ProgressPopUp(View hostView, int xPos, int yPos, int duration, IAction finishAction) {
        this(hostView, xPos, yPos, "", "", duration, finishAction);
    }
}

