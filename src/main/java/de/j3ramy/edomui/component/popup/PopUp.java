package de.j3ramy.edomui.component.popup;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.component.input.TextArea;
import de.j3ramy.edomui.component.text.CenteredText;
import de.j3ramy.edomui.theme.ThemeManager;
import de.j3ramy.edomui.theme.PopUpStyle;
import de.j3ramy.edomui.view.View;
import de.j3ramy.edomui.enums.PopUpType;
import de.j3ramy.edomui.component.Widget;

import java.awt.Rectangle;

public class PopUp extends Widget {
    private final CenteredText title;
    private final TextArea content;

    protected final PopUpStyle popUpStyle;
    protected final View view = new View();

    public PopUp(int xPos, int yPos, String titleText, String content, PopUpType type) {
        super(xPos, yPos, 0, 0);

        this.popUpStyle = new PopUpStyle(ThemeManager.getDefaultPopUpStyle());
        this.setStyle(popUpStyle);

        this.setWidth(this.popUpStyle.getWidth());
        this.setHeight(this.popUpStyle.getHeight());
        this.setLeftPos(this.getLeftPos() - this.popUpStyle.getWidth() / 2);
        this.setLeftPos(this.getTopPos() - this.popUpStyle.getHeight() / 2);

        this.getStyle().setBorderWidth(this.popUpStyle.getBorderWidth());
        this.getStyle().setBackgroundColor(this.popUpStyle.getBackgroundColor());
        this.getStyle().setBorderColor(switch (type) {
            case SUCCESS -> this.popUpStyle.getSuccessColor();
            case DEFAULT -> this.popUpStyle.getDefaultColor();
            case NOTICE -> this.popUpStyle.getNoticeColor();
            case ERROR -> this.popUpStyle.getErrorColor();
        });

        Rectangle titleRect = new Rectangle(this.getLeftPos(), this.getTopPos(), this.getWidth(), this.popUpStyle.getTitleHeight());
        this.title = new CenteredText(titleRect, titleText,
                this.popUpStyle.getTitleFontSize(),
                this.popUpStyle.getTextColor());
        this.view.addWidget(this.title);

        int contentY = this.getTopPos() + this.popUpStyle.getTitleHeight();
        int contentHeight = this.getHeight() - this.popUpStyle.getTitleHeight() - this.popUpStyle.getWidgetHeight()
                - this.popUpStyle.getMargin();

        this.content = new TextArea(
                this.getLeftPos() + this.popUpStyle.getMargin(),
                contentY,
                this.getWidth() - 2 * this.popUpStyle.getMargin(),
                contentHeight
        );

        this.content.getStyle().setPadding(0);
        this.content.setText(content);
        this.content.getStyle().setTextColor(this.popUpStyle.getTextColor());
        this.content.getStyle().setFontSize(this.popUpStyle.getContentFontSize());
        this.content.setEnabled(false);
        this.content.hideBackground();
        this.content.noBorder();

        this.view.addWidget(this.content);
    }

    @Override
    public PopUpStyle getStyle() {
        return this.popUpStyle;
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

        if (content != null) {
            content.update(x, y);
        }
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

    public CenteredText getTitle() {
        return title;
    }

    public TextArea getContent() {
        return content;
    }
}