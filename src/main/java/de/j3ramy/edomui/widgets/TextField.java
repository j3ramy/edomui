package de.j3ramy.edomui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.j3ramy.edomui.enums.ButtonType;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.interfaces.IAction;
import de.j3ramy.edomui.utils.Color;
import de.j3ramy.edomui.utils.GuiPresets;
import de.j3ramy.edomui.utils.GuiUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TextField extends Button {
    private final static int OVERFLOW_PADDING = 6;

    private final StringBuilder text = new StringBuilder();
    private final ArrayList<Character> forbiddenCharacters = new ArrayList<>();
    private final IAction onTextChangeAction, onPressEnterAction;
    private final int maxVisibleTextLength;
    protected final VerticalCenteredText visibleText;
    private final String placeholder;

    private IAction onReachEndAction;
    private boolean isFocused, isSelectedAll, isCaretVisible = true, isOverflowEnabled = true, canLooseFocus = true;
    private int caretXPosition, caretCharPosition, allSelectedBackgroundColor = GuiPresets.TEXT_FIELD_ALL_SELECTED_BACKGROUND,
            allSelectedTextColor = GuiPresets.TEXT_FIELD_ALL_SELECTED_TEXT, originalVisibleTextColor, maxLength = GuiPresets.TEXT_FIELD_MAX_LENGTH,
            caretTicks, maxCaretPosition, textPadding = GuiPresets.TEXT_FIELD_TEXT_PADDING;

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
        this.isCaretVisible = true;
        this.caretTicks = 0;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setAllSelectedBackgroundColor(int allSelectedBackgroundColor) {
        this.allSelectedBackgroundColor = allSelectedBackgroundColor;
    }

    public void setAllSelectedTextColor(int allSelectedTextColor) {
        this.allSelectedTextColor = allSelectedTextColor;
    }

    public void setVisibleTextColor(int color){
        this.visibleText.setTextColor(color);
        this.originalVisibleTextColor = color;
    }

    public String getText() {
        return text.toString();
    }

    public void setCanLooseFocus(boolean canLooseFocus) {
        this.canLooseFocus = canLooseFocus;
    }

    public void setText(String text){
        if(!text.isEmpty()){
            this.clear();
            this.getTitle().clear();
            this.appendText(text);
        }
    }

    public void appendText(String text){
        if(text != null){
            this.isFocused = true;
            boolean tempIsEnabled = this.isEnabled();
            if(!tempIsEnabled){
                this.setEnabled(true);
            }

            for(int i = 0; i < (text.length() <= this.maxLength ? text.length() : this.maxLength - 1); i++){
                this.charTyped(text.charAt(i));
            }

            this.isFocused = false;
            if(!tempIsEnabled){
                this.setEnabled(false);
            }
        }
    }

    public void disableOverflow(){
        this.isOverflowEnabled = false;
        this.setMaxLength(999);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.visibleText.setEnabled(enabled);
    }

    public void setOnReachEndAction(IAction onReachEndAction) {
        this.onReachEndAction = onReachEndAction;
    }

    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
    }

    public int getCaretCharPosition() {
        return caretCharPosition;
    }

    public TextField(int x, int y, int width, int height, String placeholderText, FontSize fontSize, @Nullable IAction onTextChangeAction,
                     @Nullable IAction onPressEnterAction){
        super(x, y, width, height, placeholderText, fontSize, null, ButtonType.TEXT_FIELD);

        this.onTextChangeAction = onTextChangeAction;
        this.onPressEnterAction = onPressEnterAction;

        this.visibleText = new VerticalCenteredText(this.toRect(), this.getLeftPos() + this.textPadding, this.text.toString(),
                fontSize, GuiPresets.TEXT_FIELD_TEXT);
        this.visibleText.disableTruncate();
        this.originalVisibleTextColor = this.visibleText.getTextColor();
        this.maxVisibleTextLength = this.getWidth() - 2 * this.textPadding - OVERFLOW_PADDING;

        this.setHoverBackgroundColor(this.getBackgroundColor());
        this.setHoverBorderColor(Color.GRAY);
        this.getTitle().setTextColor(GuiPresets.TEXT_FIELD_PLACEHOLDER_TEXT);
        this.getTitle().setLeftPos(this.getLeftPos() + this.textPadding);
        this.placeholder = placeholderText;
    }

    public TextField(int x, int y, int width, int height, String placeholderText, @Nullable IAction onTextChangeAction,
                     @Nullable IAction onPressEnterAction){
        this(x, y, width, height, placeholderText, GuiPresets.TEXT_FIELD_FONT_SIZE, onTextChangeAction, onPressEnterAction);
    }

    public TextField(int x, int y, int width, int height, String placeholderText){
        this(x, y, width, height, placeholderText, null, null);
    }

    @Override
    public void setTopPos(int topPos) {
        int deltaPos = this.getTopPos() - topPos;
        this.visibleText.setTopPos(this.visibleText.getTopPos() - deltaPos);

        super.setTopPos(topPos);
    }

    @Override
    public void setLeftPos(int leftPos) {
        int deltaPos = this.getLeftPos() - leftPos;
        this.visibleText.setLeftPos(this.visibleText.getLeftPos() - deltaPos);

        super.setLeftPos(leftPos);
    }

    @Override
    public void render(PoseStack poseStack) {
        if(this.isHidden()){
            return;
        }

        super.render(poseStack);

        if(this.isSelectedAll){
            final int selectPadding = this.textPadding - 1;
            AbstractContainerScreen.fill(poseStack, this.getLeftPos() + selectPadding, this.getTopPos() + selectPadding,
                    this.getLeftPos() + this.visibleText.getTextWidth() + selectPadding + this.textPadding,
                    this.getTopPos() + this.getHeight() - selectPadding, this.allSelectedBackgroundColor);
        }

        this.visibleText.render(poseStack);

        if(this.isFocused && this.isCaretVisible){
            final int caretWidth = 1;
            AbstractContainerScreen.fill(poseStack, this.getLeftPos() + this.textPadding + this.caretXPosition, this.getTopPos() + this.textPadding,
                    this.getLeftPos() + this.textPadding + this.caretXPosition + caretWidth, this.getTopPos() + this.getHeight() - this.textPadding,
                    this.visibleText.getTextColor());
        }
    }

    @Override
    public void update(int x, int y) {
        if(this.isHidden()){
            return;
        }

        if(this.isEnabled()){
            super.update(x, y);

            final int caretXOffset = 0;
            if(!this.isOverflowEnabled){
                this.caretXPosition = (int) (this.visibleText.getSubstringTextWidth(0, this.caretCharPosition)) + caretXOffset;
            }
            else{
                this.caretXPosition = this.visibleText.getTextWidth() + caretXOffset;
            }

            if (this.getText().isEmpty()) {
                this.getTitle().setTextColor(Color.GRAY);
            }
        }
    }

    @Override
    public void tick() {
        if(this.isHidden()){
            return;
        }

        if(!this.isSelectedAll){
            if(this.caretTicks == 80){
                this.isCaretVisible = !this.isCaretVisible;
                this.caretTicks = 0;
            }

            this.caretTicks++;
        }
    }

    @Override
    public void onClick(int mouseButton) {
        if(mouseButton == 0){
            if(this.isMouseOver()){
                super.onClick(mouseButton);
                this.isFocused = true;

                this.isCaretVisible = true;
                this.caretTicks = 0;
            }
            else if(this.canLooseFocus){
                this.isFocused = false;
            }

            if(this.isSelectedAll){
                this.unselectAll();
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if(this.isEnabled() && this.isFocused){
            //select complete text
            if(Screen.isSelectAll(keyCode) && !this.getText().isEmpty()){
                this.selectAll();
            }

            //if text all selected and backspace (259) or delete (261) pressed
            if(this.isSelectedAll && keyCode == 259 || this.isSelectedAll && keyCode == 261){
                this.clear();
            }

            //call onPressEnter-event when enter pressed
            if(keyCode == 257 && this.onPressEnterAction != null){
                this.onPressEnterAction.execute();
            }

            //remove letter when backspace
            if(keyCode == 259 && !this.getText().isEmpty() && this.caretCharPosition > 0){
                this.removeLetter();

                if(this.onTextChangeAction != null){
                    this.onTextChangeAction.execute();
                }
            }
        }
    }

    @Override
    public void charTyped(char codePoint) {
        if(this.isEnabled() && this.isFocused && this.isCharAllowed(codePoint)){
            if(this.isSelectedAll){
                this.clear();
            }

            if(this.hasEndReached()){
                if(this.onReachEndAction != null){
                    this.onReachEndAction.execute();
                }
            }
            else {
                this.addLetter(codePoint);

                if(this.onTextChangeAction != null){
                    this.onTextChangeAction.execute();
                }
            }
        }
    }

    public boolean isEmpty(){
        return this.getText().isEmpty();
    }

    public void addForbiddenCharacter(char c){
        this.forbiddenCharacters.add(c);
    }

    public void addForbiddenCharacter(char[] chars){
        for (char c : chars) {
            this.forbiddenCharacters.add(c);
        }

    }

    public void moveCaretToEnd(){
        this.caretCharPosition = this.getText().contains(GuiPresets.TEXTAREA_LINE_DELIMITER) ?
                this.visibleText.getText().length() + GuiPresets.TEXTAREA_LINE_DELIMITER.length()
                : this.visibleText.getText().length();
    }

    public void moveCaretToPos(int caretCharPosition){
        this.caretCharPosition = caretCharPosition;
    }

    public String getLastWord(){
        if(this.isEmpty()){
            return "";
        }

        String[] words = this.visibleText.getText().toString().split(" ");
        return words[words.length - 1];
    }

    public Character getLastChar(){
        if(this.isEmpty()){
            return null;
        }

        return this.visibleText.getText().toString().charAt(this.visibleText.getText().toString().length() - 1);
    }

    public void clear(){
        this.getTitle().setHidden(false);

        this.visibleText.clear();
        this.getTitle().setText(this.placeholder);
        this.text.delete(0, this.text.length());

        this.caretCharPosition = 0;
        this.caretXPosition = 0;

        this.unselectAll();

        if(this.onTextChangeAction != null){
            this.onTextChangeAction.execute();
        }
    }

    public void selectAll(){
        this.isSelectedAll = true;
        this.isCaretVisible = false;
        this.visibleText.setTextColor(this.allSelectedTextColor);
    }

    public void unselectAll(){
        this.isSelectedAll = false;
        this.isCaretVisible = true;
        this.visibleText.setTextColor(this.originalVisibleTextColor);
    }

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public boolean isEmail(){
        return this.getText().matches(EMAIL_REGEX);
    }

    protected boolean doesTextFit(){
        float textLengthInPx = this.visibleText.getTextWidth() + (this.isOverflowEnabled ? OVERFLOW_PADDING * this.visibleText.getFontSizeScale() : 0);
        return textLengthInPx < this.maxVisibleTextLength;
    }

    protected boolean isCaretAtEnd(){
        return this.text.isEmpty() || this.caretCharPosition == this.text.length();
    }

    protected boolean isCaretAtBeginning(){
        return this.caretCharPosition == 0;
    }

    public int getRemainingSpace(){
        return this.maxVisibleTextLength - this.visibleText.getTextWidth();
    }

    private boolean hasEndReached(){
        return (!this.doesTextFit() && !this.isOverflowEnabled) || this.text.length() >= this.maxLength;
    }

    private void addLetter(char c) {
        if (this.isCaretAtEnd()) {
            this.text.append(c);
        } else {
            int safeCaretPosition = Math.min(this.caretCharPosition, this.text.length());
            try {
                this.text.insert(safeCaretPosition, c);
            } catch (Exception ignored) {
                return;
            }
        }

        this.caretCharPosition = Math.min(this.caretCharPosition + 1, this.text.length());
        this.updateVisibleText();
    }


    public void removeLetter(){
        this.text.deleteCharAt(this.caretCharPosition - 1);
        this.caretCharPosition--;

        if(this.text.isEmpty()){
            this.clear();
        }
        else{
            this.updateVisibleText();
        }
    }

    private boolean isCharAllowed(char c){
        if(this.forbiddenCharacters.isEmpty())
            return true;

        for(Character forbiddenChar : this.forbiddenCharacters){
            if(forbiddenChar == c)
                return false;
        }

        return true;
    }

    private void updateVisibleText(){
        this.getTitle().setHidden(!this.text.isEmpty());

        for(GuiUtils.Formatting formatting : GuiUtils.Formatting.values()){
            String f = GuiUtils.getFormatting(formatting);
            if(this.visibleText.toString().contains(f)){
                this.visibleText.setText(this.visibleText.toString().replaceAll(f, ""));
            }
        }

        if(this.doesTextFit()){
            this.visibleText.setText(this.text.toString());
            this.maxCaretPosition = this.caretCharPosition - 1;
        }
        else {
            this.visibleText.setText(this.text.substring(Math.max(this.text.length() - this.maxCaretPosition, 0)));
        }
    }

    public String getTextBeforeCaret() {
        String text = this.getText();
        int caretPos = this.getCaretCharPosition();
        if (caretPos >= 0 && caretPos < text.length()) {
            return text.substring(0, caretPos);
        } else {
            return text;
        }
    }

    public int getWordCount() {
        return this.text.toString().split(" ").length;
    }

    public void setCaretPosition(int currentCaretPos) {
        this.caretCharPosition = currentCaretPos;
    }
}

