package de.j3ramy.edomui.theme;

import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.util.style.GuiUtils;
import de.j3ramy.edomui.util.style.WidgetStyle;

import java.awt.*;

public class PopUpStyle extends WidgetStyle {
    private Color textColor = Color.BLACK;
    private FontSize titleFontSize = FontSize.BASE;
    private FontSize contentFontSize = FontSize.XS;
    private int width = 150;
    private int height = 84;
    private Color defaultColor = Color.DARK_GRAY;
    private Color successColor = Color.GREEN;
    private Color errorColor = Color.RED;
    private Color noticeColor = Color.ORANGE;
    private int margin = 10;
    private int titleHeight = 20;

    private int widgetHeight = 13;
    private int buttonWidth = 60;;
    private int inputWidth = 100;
    private int progressBarWidth = 100;

    public Color getTextColor() {
        return textColor;
    }

    public FontSize getTitleFontSize() {
        return titleFontSize;
    }

    public FontSize getContentFontSize() {
        return contentFontSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public Color getSuccessColor() {
        return successColor;
    }

    public Color getErrorColor() {
        return errorColor;
    }

    public Color getNoticeColor() {
        return noticeColor;
    }

    public int getTitleHeight() {
        return titleHeight;
    }

    public int getButtonWidth() {
        return buttonWidth;
    }

    public int getMargin() {
        return margin;
    }

    public int getInputWidth() {
        return inputWidth;
    }

    public int getProgressBarWidth() {
        return progressBarWidth;
    }

    public int getWidgetHeight() {
        return widgetHeight;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setContentFontSize(FontSize contentFontSize) {
        this.contentFontSize = contentFontSize;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void setErrorColor(Color errorColor) {
        this.errorColor = errorColor;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setNoticeColor(Color noticeColor) {
        this.noticeColor = noticeColor;
    }

    public void setSuccessColor(Color successColor) {
        this.successColor = successColor;
    }

    public void setTitleFontSize(FontSize titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setButtonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
    }

    public void setInputWidth(int inputWidth) {
        this.inputWidth = inputWidth;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }

    public void setProgressBarWidth(int progressBarWidth) {
        this.progressBarWidth = progressBarWidth;
    }

    public void setWidgetHeight(int widgetHeight) {
        this.widgetHeight = widgetHeight;
    }

    public PopUpStyle(WidgetStyle style) {
        super(style);

        this.setBorderWidth(3);
        this.setTextColor(GuiUtils.getContrastColor(this.getBackgroundColor()));
    }
}
