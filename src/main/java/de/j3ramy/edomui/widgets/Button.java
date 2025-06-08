package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.GuiPresets;

public class Button extends Widget {
    private final IAction leftClickAction;
    private final IAction rightClickAction;
    private final Text title;

    private final Tooltip tooltip;
    private boolean isTooltipEnabled = false;

    public Text getTitle() {
        return title;
    }

    public void setTitle(String text){
        this.title.setText(text);
        this.getTitle().setTextColor(Color.WHITE);
    }

    public void enableTooltip(){
        this.isTooltipEnabled = true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.title.setEnabled(this.isEnabled());
    }

    @Override
    public void setHidden(boolean isHidden) {
        super.setHidden(isHidden);
    }

    public Button(int x, int y, int width, int height, String title, FontSize fontSize, IAction leftClickAction, IAction rightClickAction, ButtonType buttonType){
        super(x, y, width, height);

        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;
        this.setHoverable(true);

        switch (buttonType){
            case DROPDOWN -> this.title = new VerticalCenteredText(this.toRect(), this.getLeftPos() + GuiPresets.INPUT_LABEL_LEFT_MARGIN, title,
                    fontSize, this.getWidth() - 2 * GuiPresets.INPUT_LABEL_LEFT_MARGIN - 15, GuiPresets.BUTTON_TEXT, GuiPresets.BUTTON_TEXT_HOVER,
                    GuiPresets.BUTTON_TEXT_DISABLED);
            case CHECKBOX -> this.title = new VerticalCenteredText(this.toRect(), this.getLeftPos() + this.getWidth() + GuiPresets.CHECKBOX_LABEL_LEFT_MARGIN,
                    title, fontSize, 0, GuiPresets.BUTTON_TEXT, GuiPresets.BUTTON_TEXT_HOVER, GuiPresets.BUTTON_TEXT_DISABLED);
            case TEXT_FIELD -> this.title = new VerticalCenteredText(this.toRect(), this.getLeftPos() + GuiPresets.INPUT_LABEL_LEFT_MARGIN, title,
                    fontSize, this.getWidth() - 2 * GuiPresets.INPUT_LABEL_LEFT_MARGIN, GuiPresets.BUTTON_TEXT, GuiPresets.BUTTON_TEXT_HOVER,
                    GuiPresets.BUTTON_TEXT_DISABLED);
            default -> this.title = new CenteredText(this.toRect(), title, fontSize,
                    GuiPresets.BUTTON_TEXT, GuiPresets.BUTTON_TEXT_HOVER, GuiPresets.BUTTON_TEXT_DISABLED);
        }

        this.tooltip = new Tooltip(title, this.toRect());

        this.applyDefaultColor();
    }

    public Button(int x, int y, int width, int height, String title, FontSize fontSize, IAction leftClickAction){
        this(x, y, width, height, title, fontSize, leftClickAction, null, ButtonType.DEFAULT);
    }

    public Button(int x, int y, int width, int height, String title, FontSize fontSize, IAction leftClickAction, ButtonType buttonType){
        this(x, y, width, height, title, fontSize, leftClickAction, null, buttonType);
    }

    public Button(int x, int y, int width, int height, String title, FontSize fontSize, IAction leftClickAction, IAction rightClickAction){
        this(x, y, width, height, title, fontSize, leftClickAction, rightClickAction, ButtonType.DEFAULT);
    }

    public Button(int x, int y, int width, int height, String title, IAction leftClickAction){
        this(x, y, width, height, title, FontSize.BASE, leftClickAction, null, ButtonType.DEFAULT);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(this.isHidden()){
            return;
        }

        super.render(poseStack);
        this.title.render(poseStack);

        if(this.isTooltipEnabled && this.isMouseOver()){
            this.tooltip.render(poseStack);
        }
    }

    int originalTextColor;
    @Override
    public void update(int x, int y) {
        if(this.isHidden()){
            return;
        }

        super.update(x, y);
        if(this.isTooltipEnabled){
            this.tooltip.update(x, y);
        }

        if (this.isShowBackground()) {
            if (this.isMouseOver()) {
                if (this.originalTextColor == 0) {
                    this.originalTextColor = this.getTitle().getTextColor();
                }

                this.getTitle().setTextColor(Color.getContrastColor(this.getHoverBackgroundColor()));
            } else {
                if (this.originalTextColor != 0) {
                    this.getTitle().setTextColor(this.originalTextColor);
                }
            }
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if(this.isMouseOver() && this.isEnabled()){
            this.handleMouseButtonAction(mouseButton);
        }
    }

    @Override
    public void setLeftPos(int leftPos) {
        int deltaPos = this.getLeftPos() - leftPos;
        this.title.setLeftPos(this.title.getLeftPos() - deltaPos);

        super.setLeftPos(leftPos);
    }

    @Override
    public void setTopPos(int topPos) {
        int deltaPos = this.getTopPos() - topPos;
        this.title.setTopPos(this.title.getTopPos() - deltaPos);
        this.tooltip.setTopPos(this.tooltip.getTopPos() - deltaPos);

        super.setTopPos(topPos);
    }

    public void handleMouseButtonAction(int mouseButton) {
        if(this.leftClickAction != null && mouseButton == 0)
            this.leftClickAction.execute();

        if(this.rightClickAction != null && mouseButton == 1)
            this.rightClickAction.execute();
    }

    private void applyDefaultColor(){
        this.setBackgroundColor(GuiPresets.BUTTON_BACKGROUND);
        this.setBorderColor(GuiPresets.BUTTON_BORDER);
        this.setHoverBackgroundColor(GuiPresets.BUTTON_BACKGROUND_HOVER);
        this.setHoverBorderColor(GuiPresets.BUTTON_BORDER_HOVER);
        this.setDisabledBackgroundColor(GuiPresets.BUTTON_BACKGROUND_DISABLED);
        this.setDisabledBorderColor(GuiPresets.BUTTON_BORDER_DISABLED);
    }
}