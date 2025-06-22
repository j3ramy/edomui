package de.j3ramy.edomui.util.style;

import de.j3ramy.edomui.enums.FontSize;

public class GuiPresets {
    public static int CURSOR_BLINK_TICK_TIME = 20;
    public static int DOUBLE_CLICK_THRESHOLD_FAST = 250; // TextField
    public static int DOUBLE_CLICK_THRESHOLD_NORMAL = 500; // TextArea
    public static int TEXT_FIELD_CHAR_LIMIT = 256;
    public static int NUMBER_FIELD_CHAR_LIMIT = 10;
    public static int TEXT_AREA_CHAR_LIMIT = 131072;
    public static String TEXT_AREA_DELIMITER = "\n";
    public static float MAX_PROGRESS_BAR_VALUE = 1;
    public static int MAX_TEXT_LENGTH = 256;

    public static void setCursorBlinkTickTime(int cursorBlinkTickTime) {
        CURSOR_BLINK_TICK_TIME = cursorBlinkTickTime;
    }

    public static void setDoubleClickThresholdFast(int doubleClickThresholdFast) {
        DOUBLE_CLICK_THRESHOLD_FAST = doubleClickThresholdFast;
    }

    public static void setDoubleClickThresholdNormal(int doubleClickThresholdNormal) {
        DOUBLE_CLICK_THRESHOLD_NORMAL = doubleClickThresholdNormal;
    }

    public static void setTextFieldCharLimit(int textFieldCharLimit) {
        TEXT_FIELD_CHAR_LIMIT = textFieldCharLimit;
    }

    public static void setTextAreaCharLimit(int textAreaCharLimit) {
        TEXT_AREA_CHAR_LIMIT = textAreaCharLimit;
    }

    public static void setTextAreaDelimiter(String textAreaDelimiter) {
        TEXT_AREA_DELIMITER = textAreaDelimiter;
    }

    public static void setMaxProgressBarValue(float maxProgressBarValue) {
        MAX_PROGRESS_BAR_VALUE = maxProgressBarValue;
    }

    public static void setNumberFieldCharLimit(int numberFieldCharLimit) {
        NUMBER_FIELD_CHAR_LIMIT = numberFieldCharLimit;
    }

    public static void setMaxTextLength(int maxTextLength) {
        MAX_TEXT_LENGTH = maxTextLength;
    }

    public static final int TEXT_DEFAULT = Color.WHITE;

    public static final int INPUT_LABEL_LEFT_MARGIN = 5;

    public static final int POPUP_BORDER_WIDTH = 3;
    public static final int POPUP_WIDTH = 150;
    public static final int POPUP_HEIGHT = 84;
    public static final int POPUP_BUTTON_MARGIN_BOTTOM = 10;
    public static final int POPUP_PROGRESS_BAR_MARGIN_BOTTOM = 20;
    public static final int POPUP_BUTTON_MARGIN_X = 10;
    public static final int POPUP_BUTTON_WIDTH = 60;
    public static final int POPUP_BUTTON_HEIGHT= 13;
    public static final int POPUP_TITLE_MARGIN_TOP = 7;
    public static final int POPUP_PROGRESS_BAR_WIDTH = 100;
    public static final int POPUP_PROGRESS_BAR_HEIGHT = 14;
    public static final FontSize POPUP_TITLE_FONT_SIZE = FontSize.BASE;

    public static final FontSize TABLE_COLUMN_FONT_SIZE = FontSize.S;

    public static final int SCROLLBAR_TRACK_WIDTH = 3;

    public static final int TEXT_DEFAULT_HOVER = Color.WHITE;
    public static final int TEXT_DEFAULT_DISABLED = Color.GRAY;

    public static final int POPUP_COLOR_SUCCESS = Color.DARK_GREEN;
    public static final int POPUP_COLOR_DEFAULT = Color.DARK_GRAY;
    public static final int POPUP_COLOR_NOTICE = Color.ORANGE;
    public static final int POPUP_COLOR_ERROR = Color.RED;
    public static final int POPUP_BACKGROUND = Color.WHITE;
    public static final int POPUP_TEXT = Color.DARK_GRAY;

    public static final int PROGRESS_BAR_BAR_COLOR = Color.BLUE;

    public static final int TOOLTIP_BACKGROUND = Color.GRAY;
    public static final int TOOLTIP_TEXT = Color.WHITE;
}
