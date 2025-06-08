package de.j3ramy.edomui.widgets;

import de.j3ramy.edomui.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.utils.GuiPresets;

public final class AlertPopUp extends PopUp {
    public AlertPopUp(View view, int xPos, int yPos, String title, String content, String buttonTitle, PopUpType type) {
        super(xPos - GuiPresets.POPUP_WIDTH / 2, yPos - GuiPresets.POPUP_HEIGHT / 2, title, content, type);

        this.view.addWidget(new Button((this.getLeftPos() + this.getWidth() / 2) - GuiPresets.POPUP_BUTTON_WIDTH / 2,
                this.getTopPos() + this.getHeight() - GuiPresets.POPUP_BUTTON_HEIGHT - GuiPresets.POPUP_BUTTON_MARGIN_BOTTOM,
                GuiPresets.POPUP_BUTTON_WIDTH, GuiPresets.POPUP_BUTTON_HEIGHT, buttonTitle, () -> view.getWidgets().remove(this)));
    }
}

