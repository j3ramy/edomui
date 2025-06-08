
package de.j3ramy.edomui.widgets;


import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.GuiPresets;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public final class Dropdown extends Button {
    public static final int MAX_VISIBLE_ELEMENTS = 4;

    private final IAction onChangeAction;
    private boolean isUnfolded;
    private List menu;

    public Dropdown(ArrayList<String> options, int x, int y, int width, int height, String placeholder, int selectedColor, IAction onChangeAction){
        super(x, y, width, height, placeholder, GuiPresets.DROPDOWN_FONT_SIZE, null, ButtonType.DROPDOWN);

        this.onChangeAction = onChangeAction;

        this.getTitle().setTextColor(Color.GRAY);
        this.setHoverBackgroundColor(this.getBackgroundColor());
        this.setHoverBorderColor(Color.GRAY);

        if(options != null){
            this.setOptions(options, selectedColor);
        }
    }

    public Dropdown(ArrayList<String> options, int x, int y, int width, int height, String placeholder, int selectedColor){
        this(options, x, y, width, height, placeholder, selectedColor, null);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(!this.isHidden()){
            super.render(poseStack);

            this.renderArrow(poseStack, this.getLeftPos() + this.getWidth() - 15, this.getTopPos() + (this.getHeight() / 2) - 2, this.isUnfolded);
            if(this.isUnfolded() && this.menu != null){
                this.menu.render(poseStack);
            }
        }
    }

    @Override
    public void update(int x, int y) {
        if(this.isHidden()){
            return;
        }

        super.update(x, y);

        if (this.getSelectedText().isEmpty() || !this.hasSelection()) {
            this.getTitle().setTextColor(Color.GRAY);
        }
        else if(this.hasSelection()){
            this.getTitle().setTextColor(Color.WHITE);
        }

        if(this.isUnfolded() && this.menu != null){
            this.menu.update(x, y);
        }
    }

    @Override
    public void onScroll(double delta) {
        super.onScroll(delta);

        if(this.isUnfolded() && this.menu != null){
            this.menu.onScroll(delta);
        }
    }

    private String lastSelectedElement = "";
    @Override
    public void onClick(int mouseButton){
        if(mouseButton == 0){
            super.onClick(mouseButton);

            if(this.isUnfolded && this.menu != null){
                this.menu.onClick(mouseButton);

                if(this.onChangeAction != null && (!this.lastSelectedElement.equals(this.menu.getSelectedTitle()) || this.lastSelectedElement.isEmpty())){
                    this.lastSelectedElement = this.menu.getSelectedTitle();
                    this.onChangeAction.execute();
                }
            }

            if(this.isMouseOver() && this.isEnabled()){
                this.isUnfolded = !this.isUnfolded;
            }
            else{
                this.isUnfolded = false;
            }
        }
    }

    @Override
    public void setTopPos(int topPos) {
        int deltaPos = this.getTopPos() - topPos;

        if(this.menu != null){
            this.menu.setTopPos(this.menu.getTopPos() - deltaPos);
        }

        super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        int deltaPos = this.getLeftPos() - leftPos;

        if(this.menu != null){
            this.menu.setLeftPos(this.menu.getLeftPos() - deltaPos);
        }

        super.setLeftPos(leftPos);
    }

    public void setOptions(@NotNull ArrayList<String> options, int selectedColor){
        final int elementHeight = GuiPresets.DROPDOWN_OPTION_HEIGHT;
        this.menu = new List(this.getLeftPos(), this.getTopPos() + this.getHeight() + 3, this.getWidth(),
                options.size() > MAX_VISIBLE_ELEMENTS ? elementHeight * MAX_VISIBLE_ELEMENTS : elementHeight * options.size(), elementHeight, selectedColor);

        this.menu.clear();

        for(String option : options){
            this.menu.addElement(option, () -> this.setTitle(option));
        }
    }

    public boolean hasSelection(){
        for(String option : this.getOptions()){
            if(this.getSelectedText().equals(option)){
                return true;
            }
        }

        return false;
    }

    public String getSelectedText(){
        return this.getTitle().getText().toString();
    }

    public boolean containsOption(String option){
        for(String s : this.getOptions()){
            if(s.equalsIgnoreCase(option)){
                return true;
            }
        }

        return false;
    }

    public void clear(){
        this.setOptions(new ArrayList<>(), Color.GRAY);
        this.setOption("");
        this.menu.clear();
    }

    public boolean hasOptions(){
        return this.getOptions().length != 0;
    }

    public boolean setOption(String name){
        if(this.menu == null){
            return false;
        }

        if(this.menu.selectEntry(name)){
            this.setTitle(name);
            return true;
        }
        else{
            this.unselect();
            return false;
        }
    }

    public void unselect(){
        this.setTitle("");
        if (this.menu != null && this.menu.getSelectedIndex() != -1) {
            Button selectedButton = this.menu.content.get(this.menu.getSelectedIndex());
            selectedButton.setBackgroundColor(Color.WHITE);
            this.menu.unselect();
        }

        this.lastSelectedElement = "";
    }

    public String[] getOptions(){
        if(this.menu == null || this.menu.content.isEmpty()){
            return new String[]{};
        }

        String[] options = new String[this.menu.content.size()];
        for(int i = 0; i < this.menu.content.size(); i++){
            options[i] = this.menu.content.get(i).getTitle().getText().toString();
        }

        return options;
    }

    public boolean isUnfolded() {
        return this.isUnfolded;
    }

    private void renderArrow(PoseStack poseStack, int x, int y, boolean inverted){
        int arrowColor = Color.WHITE;
        if(inverted){
            AbstractContainerScreen.fill(poseStack, x + 3, y, x + 5, y + 1, !this.isEnabled() ? Color.GRAY : arrowColor);
            AbstractContainerScreen.fill(poseStack, x + 2, y + 1, x + 6, y + 2, !this.isEnabled() ? Color.GRAY : arrowColor);
            AbstractContainerScreen.fill(poseStack, x + 1, y + 2, x + 7, y + 3, !this.isEnabled() ? Color.GRAY : arrowColor);
            AbstractContainerScreen.fill(poseStack, x, y + 3, x + 8, y + 4, !this.isEnabled() ? Color.GRAY : arrowColor);
        }
        else{
            AbstractContainerScreen.fill(poseStack, x, y, x + 8, y + 1, !this.isEnabled() ? Color.GRAY : arrowColor);
            AbstractContainerScreen.fill(poseStack, x + 1, y + 1, x + 7, y + 2, !this.isEnabled() ? Color.GRAY : arrowColor);
            AbstractContainerScreen.fill(poseStack, x + 2, y + 2, x + 6, y + 3, !this.isEnabled() ? Color.GRAY : arrowColor);
            AbstractContainerScreen.fill(poseStack, x + 3, y + 3, x + 5, y + 4, !this.isEnabled() ? Color.GRAY : arrowColor);
        }
    }
}
