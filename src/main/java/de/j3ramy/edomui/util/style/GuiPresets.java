package de.j3ramy.edomui.util.style;

public class GuiPresets {
    public static final int LETTER_HEIGHT = 7;

    public static int CURSOR_BLINK_TICK_TIME = 20;
    public static int DOUBLE_CLICK_THRESHOLD_FAST = 250; // TextField
    public static int DOUBLE_CLICK_THRESHOLD_NORMAL = 500; // TextArea
    public static int TEXT_FIELD_CHAR_LIMIT = 256;
    public static int NUMBER_FIELD_CHAR_LIMIT = 10;
    public static int TEXT_AREA_CHAR_LIMIT = 131072;
    public static String TEXT_AREA_DELIMITER = "\n";
    public static float MAX_PROGRESS_BAR_VALUE = 1;
    public static int MAX_TEXT_LENGTH = 1024;

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
}
