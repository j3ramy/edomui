package de.j3ramy.edomui.components.popup;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.components.input.TextArea;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.util.style.GuiPresets;
import de.j3ramy.edomui.components.text.HorizontalCenteredText;
import de.j3ramy.edomui.components.Widget;

import java.awt.Rectangle;

public abstract class PopUp extends Widget {
    protected final View view = new View();

    public PopUp(int xPos, int yPos, String title, String content, PopUpType type) {
        super(xPos, yPos, GuiPresets.POPUP_WIDTH, GuiPresets.POPUP_HEIGHT);

        this.getStyle().setBorderWidth(GuiPresets.POPUP_BORDER_WIDTH);
        this.getStyle().setBackgroundColor(GuiPresets.POPUP_BACKGROUND);
        this.getStyle().setBorderColor(switch (type) {
            case SUCCESS -> GuiPresets.POPUP_COLOR_SUCCESS;
            case DEFAULT -> GuiPresets.POPUP_COLOR_DEFAULT;
            case NOTICE  -> GuiPresets.POPUP_COLOR_NOTICE;
            case ERROR   -> GuiPresets.POPUP_COLOR_ERROR;
        });

        Rectangle rect = this.toRect();
        rect.x += 5;
        rect.width -= 10;
        this.view.addWidget(new HorizontalCenteredText(
                rect,
                this.getTopPos() + GuiPresets.POPUP_TITLE_MARGIN_TOP,
                title,
                GuiPresets.POPUP_TITLE_FONT_SIZE,
                GuiPresets.POPUP_TEXT
        ));

        TextArea textArea = new TextArea(xPos, yPos + 18, this.getWidth(), this.getHeight() - GuiPresets.POPUP_BUTTON_HEIGHT -
                GuiPresets.POPUP_BUTTON_MARGIN_BOTTOM - GuiPresets.POPUP_TITLE_MARGIN_TOP
        );
        textArea.setText(content);
        textArea.setTextColor(Color.DARK_GRAY);
        //textArea.setEnabled(false);
        textArea.hideBackground();

        this.view.addWidget(textArea);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (this.isHidden()) return;
        super.render(poseStack);
        this.view.render(poseStack);
    }

    @Override
    public void update(int x, int y) {
        if (this.isHidden()) return;
        super.update(x, y);
        this.view.update(x, y);
    }

    @Override
    public void tick() {
        super.tick();
        this.view.tick();
    }

    @Override
    public void onClick(int mouseButton) {
        this.view.onClick(mouseButton);
    }

    @Override
    public void onScroll(double delta) {
        super.onScroll(delta);
        this.view.onScroll(delta);
    }

    @Override
    public void setTopPos(int topPos) {
        int delta = this.getTopPos() - topPos;
        for (Widget widget : this.view.getWidgets()) {
            widget.setTopPos(widget.getTopPos() - delta);
        }
        super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        int delta = this.getLeftPos() - leftPos;
        for (Widget widget : this.view.getWidgets()) {
            widget.setLeftPos(widget.getLeftPos() - delta);
        }
        super.setLeftPos(leftPos);
    }
}
