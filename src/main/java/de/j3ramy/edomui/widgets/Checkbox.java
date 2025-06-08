package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.utils.GuiPresets;


public final class Checkbox extends Button {
    private Rectangle check;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Checkbox(int x, int y, int width, int height, String title, IAction action) {
        super(x, y, width, height, title, GuiPresets.CHECKBOX_FONT_SIZE, action, ButtonType.CHECKBOX);
        this.getTitle().setTextColor(GuiPresets.CHECKBOX_TEXT_COLOR);
        this.initCheck();
    }

    public Checkbox(int x, int y, int width, int height, String title) {
        super(x, y, width, height, title, GuiPresets.CHECKBOX_FONT_SIZE, null, ButtonType.CHECKBOX);
        this.getTitle().setTextColor(GuiPresets.CHECKBOX_TEXT_COLOR);
        this.initCheck();
    }

    public Checkbox(int x, int y, int width, int height, String title, boolean isChecked) {
        this(x, y, width, height, title, isChecked, null);
    }

    public Checkbox(int x, int y, int width, int height, String title, boolean isChecked, IAction action) {
        this(x, y, width, height, title, action);
        this.setChecked(isChecked);
    }

    @Override
    public void setTopPos(int topPos) {
        int deltaPos = this.getTopPos() - topPos;

        if(this.check != null){
            this.check.setTopPos(this.check.getTopPos() - deltaPos);
        }

        super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        int deltaPos = this.getLeftPos() - leftPos;

        if(this.check != null){
            this.check.setLeftPos(this.check.getLeftPos() - deltaPos);
        }

        super.setLeftPos(leftPos);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(!this.isHidden()){
            super.render(poseStack);

           if(this.isChecked){
               this.renderCheck(poseStack);
           }
        }
    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);
        if(!this.isHidden()){
            this.getTitle().setTextColor(this.getBackgroundColor());
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if(this.isMouseOver()) {
            this.isChecked = !this.isChecked;
            super.onClick(mouseButton);
        }
    }

    private void initCheck(){
        this.check = new Rectangle(this.getLeftPos() + 2, this.getTopPos() + 2, this.getWidth() - 4, this.getHeight() - 4,
                GuiPresets.CHECKBOX_CHECK_COLOR);
    }

    private void renderCheck(PoseStack poseStack){
        check.render(poseStack);
    }
}
