package de.j3ramy.edomui.component.popup;

import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.component.basic.ProgressBar;

public final class ProgressPopUp extends PopUp {
    private final IAction finishAction;
    private final ProgressBar progressBar;

    public ProgressPopUp(View hostView, int xPos, int yPos, String title, String content, PopUpType type, int duration, IAction finishAction) {
        super(0, 0, title, "", type);

        this.finishAction = finishAction;

        int centeredX = xPos - this.getWidth() / 2;
        int centeredY = yPos - this.getHeight() / 2;
        this.setLeftPos(centeredX);
        this.setTopPos(centeredY);

        int barX = this.getLeftPos() + (this.getWidth() - this.popUpStyle.getProgressBarWidth()) / 2;
        int barY = this.getTopPos() + this.getHeight() - this.popUpStyle.getWidgetHeight() - this.popUpStyle.getMargin();

        this.progressBar = new ProgressBar(barX, barY, this.popUpStyle.getProgressBarWidth(), this.popUpStyle.getWidgetHeight(), duration,
                () -> {
                    this.finishAction.execute();
                    hostView.getWidgets().remove(this);
                }
        );

        this.view.addWidget(this.progressBar);
    }

    public ProgressPopUp(View hostView, int xPos, int yPos, String title, String content, int duration, IAction finishAction) {
        this(hostView, xPos, yPos, title, content, PopUpType.NOTICE, duration, finishAction);
    }

    public ProgressPopUp(View hostView, int xPos, int yPos, String title, int duration, IAction finishAction) {
        this(hostView, xPos, yPos, title, "", duration, finishAction);
    }

    public ProgressPopUp(View hostView, int xPos, int yPos, int duration, IAction finishAction) {
        this(hostView, xPos, yPos, "", "", duration, finishAction);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

}