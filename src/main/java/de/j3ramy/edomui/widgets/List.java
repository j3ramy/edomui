package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.GuiPresets;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class List extends Widget {
    protected final int elementHeight, selectedColor, maxVisibleListElements;
    final VerticalScrollbar scrollbar;
    protected final CopyOnWriteArrayList<Button> content = new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<Button> visibleContent = new CopyOnWriteArrayList<>();
    protected int selectedIndex = -1, currentScrollIndex, borderTopPadding;

    public int getMaxVisibleListElements() {
        return maxVisibleListElements;
    }

    public List(ArrayList<String> content, int x, int y, int width, int height, int elementHeight, int selectedColor){
        super(x, y, width, height);

        this.selectedColor = selectedColor == -1 ? this.getBackgroundColor() : selectedColor;
        this.maxVisibleListElements = elementHeight != 0 ? height / elementHeight : 0;
        this.elementHeight = elementHeight;

        this.scrollbar = new VerticalScrollbar(this, content.size(), this.maxVisibleListElements);

        for(String s : content){
            this.addElement(s);
        }

        this.setBorderColor(Color.BLACK);
    }

    public List(int x, int y, int width, int height, int elementHeight, int selectedColor){
        this(new ArrayList<>(), x, y, width, height, elementHeight, selectedColor);
    }

    public List(int x, int y, int width, int height, int elementHeight){
        this(new ArrayList<>(), x, y, width, height, elementHeight, -1);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for(Button button : this.content){
            button.setEnabled(enabled);
        }
    }

    @Override
    public void render(PoseStack poseStack) {
        if(!this.isHidden()){
            super.render(poseStack);

            if(this.needsScrolling()){
                this.scrollbar.render(poseStack);
            }

            for(Button entry : this.visibleContent){
                entry.render(poseStack);
            }
        }
    }

    @Override
    public void update(int x, int y) {
        if(!this.isHidden()){
            super.update(x, y);

            for(Button entry : this.visibleContent){
                entry.update(x, y);
            }
        }
    }

    @Override
    public void tick() {
        if(!this.isHidden()){
            for(Button entry : this.visibleContent){
                if(entry != null){
                    entry.tick();
                }
            }
        }
    }

    @Override
    public void onClick(int mouseButton){
        if(!this.isHidden() && mouseButton == 0 && this.isMouseOver()){
            boolean elementClicked = false;
            for(Button entry: this.content){
                entry.setBackgroundColor(this.getBackgroundColor());
                entry.getTitle().setTextColor(Color.DARK_GRAY);
                if(entry.isMouseOver()){
                    this.selectEntry(entry);
                    elementClicked = true;
                }

                entry.onClick(mouseButton);
            }

            if(!elementClicked){
                this.selectedIndex = -1;
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if(!this.isHidden()){
            for(Button entry : this.visibleContent){
                entry.keyPressed(keyCode);
            }
        }
    }

    @Override
    public void onScroll(double delta) {
        if(!this.isHidden() && this.isMouseOver() && this.needsScrolling()){
            int maxScrollIndex = this.content.size() - this.maxVisibleListElements;

            if(this.currentScrollIndex < maxScrollIndex && delta < 0){
                this.currentScrollIndex++;
            }

            if(this.currentScrollIndex > 0 && delta > 0){
                this.currentScrollIndex--;
            }

            this.initList(this.currentScrollIndex);
            this.scrollbar.updateScrollIndex(this.currentScrollIndex);
        }
    }

    @Override
    public void charTyped(char codePoint) {
        if(!this.isHidden()){
            for(Button entry: this.content){
                entry.charTyped(codePoint);
            }
        }
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        super.setBackgroundColor(backgroundColor);

        for(Button entry: this.content){
            entry.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void setTopPos(int topPos) {
        this.scrollbar.setTopPos(topPos);

        for(int i = 0; i < this.content.size(); i++){
            this.content.get(i).setTopPos(topPos + this.elementHeight * i);
        }

        for(int i = 0; i < this.visibleContent.size(); i++){
            this.visibleContent.get(i).setTopPos(topPos + this.elementHeight * i);
        }

        super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        this.scrollbar.setLeftPos(leftPos);

        for(Button element : this.content){
            element.setLeftPos(leftPos);
        }

        for(Button element : this.visibleContent){
            element.setLeftPos(leftPos);
        }

        super.setLeftPos(leftPos);
    }

    public void setTextColor(int textColor){
        for(Button entry: this.content){
            entry.getTitle().setTextColor(textColor);
        }
    }

    public String getSelectedTitle(){
        if(this.selectedIndex == -1)
            return "";

        return this.content.get(this.selectedIndex).getTitle().getText().toString();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public int getCount(){
        return this.content.size();
    }

    public boolean selectEntry(String name){
        for(Button entry : this.content){
            if(entry.getTitle().getText().toString().equals(name)){
                this.selectEntry(entry);
                this.content.get(this.selectedIndex).handleMouseButtonAction(0);
                return true;
            }
        }

        return false;
    }

    public void selectFirstEntry(){
        if(!this.content.isEmpty()){
            this.selectEntry(content.get(0));
            this.content.get(0).handleMouseButtonAction(0);
        }
    }

    public void renameEntry(int index, String newName){
        if(index != -1){
            String currentName = this.getSelectedTitle();
            this.content.get(index).getTitle().setText(newName);

            for(Button visibleButton : this.visibleContent){
                if(visibleButton.getTitle().getText().toString().equals(currentName)){
                    visibleButton.getTitle().setText(newName);
                }
            }
        }
    }

    public void setElementEnabled(String content, boolean enabled){
        for(Button element : this.content){
            if(element.getTitle().getText().toString().equals(content)){
                element.setEnabled(enabled);
            }
        }
    }

    public void addElement(String title, @Nullable IAction clickAction){
        Button button = new Button(this.getLeftPos(), this.getTopPos() + this.content.size() * this.elementHeight,
                this.getWidth() - GuiPresets.SCROLLBAR_TRACK_WIDTH, this.elementHeight, title, GuiPresets.SCROLLABLE_LIST_FONT_SIZE, clickAction,
                ButtonType.TEXT_FIELD);
        button.noBorder();
        button.setBackgroundColor(this.getBackgroundColor());
        button.setHoverBackgroundColor(this.selectedColor);
        button.getTitle().setTextColor(Color.DARK_GRAY);
        button.enableTooltip();

        this.addElement(button);
    }

    public void addElement(Button button){
        this.content.add(button);
        this.initList(0);

        this.scrollbar.updateContentSize(this.content.size());
    }

    public void addElement(String title){
        this.addElement(title, null);
    }

    public void insertElement(Button button, int index){
        button.setWidth(this.getWidth() - GuiPresets.SCROLLBAR_TRACK_WIDTH);
        this.content.add(index, button);

        this.selectedIndex = index;
        if(this.isLastElementSelected()){
            this.onScroll(-1);
        }

        this.updateElementPosition(index, true);
        this.initList(this.content.size() - this.maxVisibleListElements);
        this.scrollbar.updateContentSize(this.content.size());
    }

    public void removeSelectedElement(){
        this.removeElement(this.selectedIndex);

        //this.initList(0);
    }

    public void removeElement(int index){
        if(index != -1 && index < this.content.size()){
            this.content.remove(index);
            this.onScroll(1);
            this.selectedIndex = -1;
            this.updateElementPosition(index, false);
            this.initList(this.content.size() - this.maxVisibleListElements);

            this.scrollbar.updateContentSize(this.content.size());
        }
    }

    public void moveSelectedElementUp(){
        if(this.selectedIndex != -1){
            if(this.selectedIndex > 0){
                Collections.swap(this.content, this.selectedIndex, this.selectedIndex - 1);
                this.updateElementPosition();
                this.selectedIndex--;

                this.initList(0);
            }
        }
    }

    public void moveSelectedElementDown(){
        if(this.selectedIndex != -1){
            if(this.selectedIndex < this.content.size() - 1){
                Collections.swap(this.content, this.selectedIndex, this.selectedIndex + 1);
                this.updateElementPosition();
                this.selectedIndex++;

                this.initList(0);
            }
        }
    }

    public void clear(){
        this.visibleContent.clear();
        this.content.clear();
        this.selectedIndex = -1;
    }

    public void unselect(){
        if(this.selectedIndex != -1){
            this.content.get(this.selectedIndex).setBackgroundColor(this.getBackgroundColor());
            this.selectedIndex = -1;
        }
    }

    public boolean isEmpty(){
        return this.content.size() <= 1;
    }

    public boolean hasEntry(String title){
        for(Button entry : this.content){
            if(entry.getTitle().getText().toString().equals(title))
                return true;
        }

        return false;
    }

    public boolean hasSelection(){
        return this.selectedIndex > -1;
    }

    public boolean isLastElementSelected(){
        return this.selectedIndex == this.content.size() - 1;
    }

    public boolean isFirstElementSelected(){
        return this.selectedIndex == 0;
    }

    protected boolean isLastVisibleElementSelected(){
        return this.selectedIndex == this.currentScrollIndex + this.maxVisibleListElements;
    }

    protected boolean isFirstVisibleElementSelected(){
        return this.selectedIndex == this.currentScrollIndex - 1;
    }

    protected void initList(int startIndex){
        this.visibleContent.clear();
        if(this.needsScrolling()){
            for(int i = startIndex; i < this.content.size(); i++){
                if(this.visibleContent.size() < this.maxVisibleListElements){
                    this.content.get(i).setTopPos(this.getTopPos() + this.borderTopPadding + this.visibleContent.size() * this.elementHeight);
                    this.visibleContent.add(this.content.get(i));
                }
                else{
                    return;
                }
            }
        }
        else{
            this.visibleContent.addAll(this.content);
        }
    }

    private void updateElementPosition(int index, boolean moveDown){
        if(moveDown){
            index++;
        }

        for(int i = index; i < this.content.size(); i++){
            Button element = this.content.get(i);
            element.setTopPos(element.getTopPos() + (moveDown ? this.elementHeight : - this.elementHeight));
        }
    }

    private void updateElementPosition(){
        for(int i = 0; i < this.content.size(); i++){
            this.content.get(i).setTopPos(this.getTopPos() + i * this.elementHeight);
        }
    }

    private void selectEntry(Button entry){
        this.selectedIndex = this.content.indexOf(entry);
        entry.setBackgroundColor(this.selectedColor);
        entry.getTitle().setTextColor(Color.WHITE);
    }

    private boolean needsScrolling(){
        return this.content.size() >= this.maxVisibleListElements;
    }

    public boolean containsOption(String text) {
        for(Button entry : this.content){
            if(entry.getTitle().getText().toString().equalsIgnoreCase(text)){
                return true;
            }
        }

        return false;
    }
}
