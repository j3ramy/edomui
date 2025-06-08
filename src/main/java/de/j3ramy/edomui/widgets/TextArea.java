package de.j3ramy.edomui.widgets;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.GuiPresets;
import de.j3ramy.edomui.utils.GuiUtils;
import net.minecraft.client.gui.screens.Screen;

public class TextArea extends List {
    private boolean isSelectedAll;
    private int maxLength = GuiPresets.TEXTAREA_DEFAULT_MAX_LENGTH;

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public TextArea(int x, int y, int width, int height){
        super(x, y, width, height, GuiPresets.TEXTAREA_LINE_HEIGHT);

        this.borderTopPadding = GuiPresets.TEXT_FIELD_TEXT_PADDING;
        this.setDisabledBackgroundColor(this.getBackgroundColor());

        this.addBlankLine();
        this.unfocusAll();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for(Button button : this.content){
            button.setEnabled(true);
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if(mouseButton == 0){
            if(this.isMouseOver() && this.isEnabled()){
                this.unselectAll();
                super.onClick(mouseButton);

                if(!this.isAnyLineHovered() && !this.content.isEmpty()){
                    this.focusLastLine();
                }
            }
            else if(this.isAnyLineFocused()){
                this.unfocusAll();
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if(this.isAnyLineFocused()){

            if(keyCode != 259){
                super.keyPressed(keyCode);
            }

            //select complete text
            if(Screen.isSelectAll(keyCode)){
                this.selectAll();
            }

            switch(keyCode){
                case 259 -> this.onPressBackspace(); //Backspace
                case 261 -> { // Delete
                    if(this.isSelectedAll){
                        this.clear();
                        this.initTextField();
                    }
                }
                case 257 -> this.onPressEnter(' '); //Enter
                case 264 -> this.moveCaretDown(); //Arrow Down
                case 265 -> this.moveCaretUp(); //Arrow Up
            }
        }
    }

    @Override
    public void setHidden(boolean isHidden) {
        super.setHidden(isHidden);
    }

    public void hideScrollbar(){
        this.scrollbar.setHidden(true);
    }

    @Override
    public void charTyped(char codePoint) {
        this.setHidden(false);

        if(this.isSelectedAll){
            this.clear();
            this.initTextField();
        }

        if(this.getCharacterCount() < this.maxLength && this.isEnabled()){
            TextField focusedLine = this.getFocusedLine();
            if (focusedLine != null) {
                if (focusedLine.getRemainingSpace() > GuiUtils.getTextWidth(String.valueOf(codePoint), focusedLine.visibleText.getFontSizeScale())) {
                    super.charTyped(codePoint);
                } else {
                    this.onPressEnter(codePoint);
                    super.charTyped(codePoint);
                }
            }
        }

        if(this.getFocusedLine() != null){
            this.getFocusedLine().visibleText.setText(this.getFocusedLine().visibleText.getText().toString().replaceAll(GuiPresets.TEXTAREA_LINE_DELIMITER, ""));
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.isSelectedAll = false;
    }

    @Override
    public boolean isEmpty() {
        return this.getContent().equals(GuiPresets.TEXTAREA_LINE_DELIMITER);
    }

    public void scrollToTop(){
        while(this.selectedIndex > 0){
            this.moveCaretUp();
        }

        this.initList(0);
        this.unfocusAll();
    }

    public String getContent() {
        StringBuilder s = new StringBuilder();

        for (Button element : this.content) {
            if (element instanceof TextField) {
                String line = ((TextField) element).getText();
                if (!line.isBlank()) {
                    s.append(line).append(GuiPresets.TEXTAREA_LINE_DELIMITER);
                } else {
                    s.append(GuiPresets.TEXTAREA_LINE_DELIMITER);
                }
            }
        }

        int delimiterLength = GuiPresets.TEXTAREA_LINE_DELIMITER.length();
        while (s.length() >= delimiterLength && s.substring(s.length() - delimiterLength).equals(GuiPresets.TEXTAREA_LINE_DELIMITER)) {
            s.delete(s.length() - delimiterLength, s.length());
        }

        return s.toString();
    }


    public void setContent(String text) {
        this.clear();

        if (text.isEmpty() || text.isBlank()) {
            this.addBlankLine();
            return;
        }

        boolean enabledState = this.isEnabled();
        if (!enabledState) {
            this.setEnabled(true);
        }

        String[] lines = text.split(GuiPresets.TEXTAREA_LINE_DELIMITER, -1);

        for (String line : lines) {
            this.addBlankLine();
            this.focusLastLine();

            if (!line.isBlank()) {
                for (char c : line.toCharArray()) {
                    this.charTyped(c);
                }
            }
        }

        if (!enabledState) {
            this.setEnabled(false);
            this.unfocusAll();
        }

        this.scrollToTop();
    }

    public int getCharacterCount(){
        int length = 0;
        for(Button element : this.content){
            if(element instanceof TextField){
                String text = ((TextField) element).getText().replaceAll(GuiPresets.TEXTAREA_LINE_DELIMITER, "");
                length += text.length();
            }
        }

        return length;
    }

    public int getWordCount(){
        int length = 0;
        for(Button element : this.content){
            if(element instanceof TextField){
                String[] words = ((TextField) element).getText().replaceAll(GuiPresets.TEXTAREA_LINE_DELIMITER, "").split(" ");

                if(words.length == 1 && words[0].isEmpty()){
                    continue;
                }

                length += ((TextField) element).getText().split(" ").length;
            }
        }

        return length;
    }

    public int getCurrentLine(){
        if(this.isAnyLineFocused()){
            return this.content.indexOf(this.getFocusedLine());
        }

        return 0;
    }

    public int getLinesPerPage(){
        return this.getMaxVisibleListElements() - 1;
    }

    private void initTextField(){
        this.addBlankLine();
        this.focusLastLine();
    }

    private void onPressEnter(char c) {
        if (this.isSelectedAll) {
            this.clear();
            this.initTextField();
        } else if (this.getFocusedLine() != null) {
            int currentScrollIndex = this.currentScrollIndex;
            int currentCaretPos = this.getFocusedLine().getCaretCharPosition();
            int currentSelectedIndex = this.selectedIndex;

            if (c == ' ') {
                this.unfocusAll();
                this.insertLine("", this.selectedIndex + 1);
            } else {
                String lastWord = this.getFocusedLine().getLastWord();
                boolean shouldInsertChar = false;

                if (this.getFocusedLine().getLastChar() == ' ') {
                    lastWord = "";
                    shouldInsertChar = true;
                }

                TextField focusedLine = this.getFocusedLine();
                this.unfocusAll();
                this.insertLine(focusedLine.getWordCount() > 1 ? lastWord.trim() : "", this.selectedIndex + 1);

                if (!shouldInsertChar && focusedLine.getWordCount() > 1) {
                    this.getPreviousLine().setText(this.deleteLastOccurrence(this.getPreviousLine().getText() + " ", lastWord));
                }
            }

            if (this.getFocusedLine() != null) {
                this.getFocusedLine().visibleText.setText(
                        this.getFocusedLine().visibleText.getText().toString().replaceAll(GuiPresets.TEXTAREA_LINE_DELIMITER, "")
                );
            }

            if (this.getPreviousLine() != null) {
                this.getPreviousLine().visibleText.setText(
                        this.getPreviousLine().visibleText.getText().toString().replaceAll(GuiPresets.TEXTAREA_LINE_DELIMITER, "")
                );
            }

            this.currentScrollIndex = currentScrollIndex;
            this.selectedIndex = currentSelectedIndex + 1;
            this.initList(this.currentScrollIndex);
            this.focusLine(this.selectedIndex);
            this.getFocusedLine().setCaretPosition(currentCaretPos);

            if (this.isLastVisibleElementSelected()) {
                this.onScroll(-1);
            }
        }
    }

    private void onPressBackspace() {
        if (this.isSelectedAll) {
            this.clear();
            this.initTextField();
            return;
        }

        final TextField focusedLine = this.getFocusedLine();
        final TextField nextLine = this.getNextLine();

        if(focusedLine == null){
            return;
        }

        if (focusedLine.getText().trim().equals(GuiPresets.TEXTAREA_LINE_DELIMITER) || focusedLine.getText().trim().isEmpty()) {
            int tmpIndex = this.selectedIndex;

            if (nextLine != null && !nextLine.getText().startsWith(GuiPresets.TEXTAREA_LINE_DELIMITER)) {
                nextLine.setText(GuiPresets.TEXTAREA_LINE_DELIMITER + nextLine.getText());
            }

            if(this.selectedIndex > 0){
                this.removeSelectedElement();
            }

            this.selectedIndex = Math.max(tmpIndex - 1, 0); // Ensure index does not go below 0
            this.focusLine(this.selectedIndex);
        } else if (focusedLine.getTextBeforeCaret().trim().equals(GuiPresets.TEXTAREA_LINE_DELIMITER) || focusedLine.isCaretAtBeginning()) {
            if (nextLine != null && !nextLine.getText().startsWith(GuiPresets.TEXTAREA_LINE_DELIMITER)) {
                nextLine.setText(GuiPresets.TEXTAREA_LINE_DELIMITER + nextLine.getText());
            }

            this.selectedIndex = Math.max(this.selectedIndex - 1, 0); // Ensure index does not go below 0
            this.focusLine(this.selectedIndex);
        } else if (!focusedLine.isCaretAtBeginning()) {
            focusedLine.removeLetter();
        }

        // Clean up visible text
        focusedLine.visibleText.setText(focusedLine.visibleText.getText().toString().replace(GuiPresets.TEXTAREA_LINE_DELIMITER, "").trim());

        if (nextLine != null) {
            nextLine.visibleText.setText(nextLine.visibleText.getText().toString().replace(GuiPresets.TEXTAREA_LINE_DELIMITER, "").trim());
        }
    }

    private void focusLastLine(){
        this.focusLine(this.content.size() - 1);
    }

    private void addBlankLine() {
        this.insertLine("", this.content.size());
    }

    private void insertLine(String text, int index){
        this.insertElement(this.createLineElement(text), index);
    }

    private void moveCaretUp(){
        if(this.isSelectedAll){
            this.unselectAll();
        }

        if(this.selectedIndex > 0){
            this.selectedIndex--;
            this.focusLine(this.selectedIndex);

            if(this.isFirstVisibleElementSelected()){
                this.onScroll(1);
            }
        }
    }

    private void moveCaretDown(){
        if(this.isSelectedAll){
            this.unselectAll();
        }

        if(!this.isLastElementSelected()){
            this.selectedIndex++;
            this.focusLine(this.selectedIndex);

            if(this.isLastVisibleElementSelected()){
                this.onScroll(-1);
            }
        }
    }

    public void unfocusAll(){
        for(Button element : this.content){
            if(element instanceof TextField){
                ((TextField) element).setFocused(false);
            }
        }
    }

    private void focusLine(int index){
        if(index < 0){
            index = 0;
        }

        if(this.content.isEmpty()){
            return;
        }

        TextField line = (TextField) this.content.get(index);
        if(line != null){
            this.unfocusAll();
            line.setFocused(true);
            line.moveCaretToEnd();
            this.selectedIndex = index;
        }
    }

    private void selectAll(){
        for(Button element : this.content){
            if(element instanceof TextField){
                ((TextField) element).selectAll();
            }
        }

        this.isSelectedAll = true;
    }

    private void unselectAll(){
        for(Button element : this.content){
            if(element instanceof TextField){
                ((TextField) element).unselectAll();
            }
        }

        this.isSelectedAll = false;
    }

    private TextField getFocusedLine(){
        for(Button element : this.content){
            if(element instanceof TextField && ((TextField) element).isFocused()){
                return (TextField) element;
            }
        }

        return null;
    }

    private TextField getPreviousLine(){
        return (TextField) this.content.get(de.j3ramy.edomui.utils.Math.clamp(this.selectedIndex - 1, 0, this.content.size() - 1));
    }

    private TextField getNextLine(){
        return (TextField) this.content.get(de.j3ramy.edomui.utils.Math.clamp(this.selectedIndex + 1, 0, this.content.size() - 1));
    }

    private boolean isAnyLineFocused(){
        return this.getFocusedLine() != null;
    }

    private boolean isAnyLineHovered(){
        boolean b = false;
        for(Button element : this.content){
            if(element.isMouseOver()){
                b = true;
            }
        }

        return b;
    }

    private TextField createLineElement(String text){
        TextField line = new TextField(this.getLeftPos() + GuiPresets.TEXTAREA_BORDER_PADDING, this.getTopPos() + (this.selectedIndex + 1) *
                GuiPresets.TEXTAREA_LINE_HEIGHT + GuiPresets.TEXTAREA_BORDER_PADDING,
                this.getWidth() - 2 * GuiPresets.TEXTAREA_BORDER_PADDING, GuiPresets.TEXTAREA_LINE_HEIGHT, "",
                FontSize.XS, null, null);
        line.setShowBackground(false);
        line.setHoverable(false);
        line.setVisibleTextColor(Color.DARK_GRAY);
        line.setText(text);
        line.setFocused(true);

        return line;
    }

    private String deleteLastOccurrence(String s, String toRemove){
        int lastOccurrenceIndex = s.lastIndexOf(toRemove);
        if(lastOccurrenceIndex != -1){
            return s.substring(0, lastOccurrenceIndex);
        }

        return "";
    }
}
