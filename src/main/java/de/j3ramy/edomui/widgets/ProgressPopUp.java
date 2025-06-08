package de.j3ramy.edomui.widgets;

import de.j3ramy.edomui.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.utils.GuiPresets;

public final class ProgressPopUp extends PopUp {
    private final IAction finishAction;

    public ProgressPopUp(View view, int xPos, int yPos, String title, String content, PopUpType type, int duration, IAction finishAction, int barColor){
        super(xPos, yPos, title, content, type);

        this.finishAction = finishAction;
        this.view.addWidget(new ProgressBar(this.getLeftPos() + this.getWidth() / 2 - GuiPresets.POPUP_PROGRESS_BAR_WIDTH / 2,
                this.getTopPos() + this.getHeight() - GuiPresets.POPUP_PROGRESS_BAR_HEIGHT - GuiPresets.POPUP_PROGRESS_BAR_MARGIN_BOTTOM,
                GuiPresets.POPUP_PROGRESS_BAR_WIDTH, GuiPresets.POPUP_PROGRESS_BAR_HEIGHT, duration, () -> {
            this.finishAction.execute();
            view.getWidgets().remove(this);
        }, barColor));
    }

    public ProgressPopUp(View view, int xPos, int yPos, String title, String content, PopUpType type, int duration, IAction finishAction){
        this(view, xPos, yPos, title, content, type, duration, finishAction, GuiPresets.PROGRESS_BAR_BAR);
    }
}

