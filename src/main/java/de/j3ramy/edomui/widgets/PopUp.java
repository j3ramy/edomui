package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.utils.GuiPresets;

import java.awt.Rectangle;

public class PopUp extends Widget {
    protected final View view = new View();

     public PopUp(int xPos, int yPos, String title, String content, PopUpType type) {
        super(xPos , yPos, GuiPresets.POPUP_WIDTH, GuiPresets.POPUP_HEIGHT);

        switch (type) {
            case SUCCESS -> this.setBorderColor(GuiPresets.POPUP_COLOR_SUCCESS);
            case DEFAULT -> this.setBorderColor(GuiPresets.POPUP_COLOR_DEFAULT);
            case NOTICE -> this.setBorderColor(GuiPresets.POPUP_COLOR_NOTICE);
            case ERROR -> this.setBorderColor(GuiPresets.POPUP_COLOR_ERROR);
        }

        this.setBackgroundColor(GuiPresets.POPUP_BACKGROUND);
        this.setBorderWidth(GuiPresets.POPUP_BORDER_WIDTH);

        Rectangle rect = this.toRect();
        rect.x = rect.x + 5;
        rect.width = rect.width - 10;
        this.view.addWidget(new HorizontalCenteredText(rect, this.getTopPos() + GuiPresets.POPUP_TITLE_MARGIN_TOP, title, GuiPresets.POPUP_TITLE_FONT_SIZE,
                GuiPresets.POPUP_TEXT));

        TextArea textArea;
        this.view.addWidget(textArea = new TextArea(xPos, yPos + 22, this.getWidth(),
                this.getHeight() - 22 - 30));
        textArea.setEnabled(false);
        textArea.noBorder();
        textArea.setContent(content);
        textArea.unfocusAll();
    }

    public void render(PoseStack poseStack){
        if(this.isHidden()){
            return;
        }

        super.render(poseStack);
        this.view.render(poseStack);
    }

    @Override
    public void update(int x, int y) {
        if(this.isHidden()){
            return;
        }

        super.update(x, y);
        this.view.update(x,y);
    }

    @Override
    public void onClick(int mouseButton) {
        this.view.onClick(mouseButton);
    }

    @Override
    public void setTopPos(int topPos) {
        for(Widget widget : this.view.getWidgets()){
            int deltaPos = this.getTopPos() - topPos;
            widget.setTopPos(widget.getTopPos() - deltaPos);
        }

         super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        for(Widget widget : this.view.getWidgets()){
            int deltaPos = this.getLeftPos() - leftPos;
            widget.setLeftPos(widget.getLeftPos() - deltaPos);
        }

        super.setLeftPos(leftPos);
    }

    @Override
    public void onScroll(double delta) {
        super.onScroll(delta);
        this.view.onScroll(delta);
    }
}
