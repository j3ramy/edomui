package de.j3ramy.edomui.util.style;

import de.j3ramy.edomui.enums.FontSize;

public class GuiPresets {
    public static final int DEFAULT_BORDER_WIDTH = 1;

    public static final WidgetStyle DEFAULT_STYLE = new WidgetStyle(
            Color.GRAY, Color.BLACK, Color.GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.BLACK, DEFAULT_BORDER_WIDTH
    );

    public static final int TEXT_DEFAULT = Color.WHITE;


    public static final int CHECKBOX_LABEL_LEFT_MARGIN = 5;
    public static final FontSize CHECKBOX_FONT_SIZE = FontSize.S;
    public static final int CHECKBOX_CHECK_COLOR = Color.WHITE;

    public static final int INPUT_LABEL_LEFT_MARGIN = 5;

    public static final FontSize DROPDOWN_FONT_SIZE = FontSize.S;
    public static final int DROPDOWN_OPTION_HEIGHT = 13;

    public static final int TEXT_FIELD_MAX_LENGTH = 128;
    public static final int TEXT_FIELD_TEXT_PADDING = 2;
    public static final FontSize TEXT_FIELD_FONT_SIZE = FontSize.S;

    public static final int POPUP_BORDER_WIDTH = 3;
    public static final int POPUP_WIDTH = 150;
    public static final int POPUP_HEIGHT = 84;
    public static final int POPUP_BUTTON_MARGIN_BOTTOM = 10;
    public static final int POPUP_PROGRESS_BAR_MARGIN_BOTTOM = 20;
    public static final int POPUP_BUTTON_MARGIN_X = 10;
    public static final int POPUP_BUTTON_WIDTH = 60;
    public static final int POPUP_BUTTON_HEIGHT= 13;
    public static final int POPUP_TITLE_MARGIN_TOP = 10;
    public static final int POPUP_PROGRESS_BAR_WIDTH = 100;
    public static final int POPUP_PROGRESS_BAR_HEIGHT = 14;
    public static final FontSize POPUP_TITLE_FONT_SIZE = FontSize.BASE;
    public static final FontSize POPUP_CONTENT_FONT_SIZE = FontSize.XS;

    public static final FontSize SCROLLABLE_LIST_FONT_SIZE = FontSize.S;

    public static final FontSize TOOLTIP_FONT_SIZE = FontSize.S;

    public static final FontSize TABLE_COLUMN_FONT_SIZE = FontSize.S;

    public static final int SCROLLBAR_TRACK_WIDTH = 3;

    public static final int TEXTAREA_DEFAULT_MAX_LENGTH = 4096;
    public static final int TEXTAREA_BORDER_PADDING = 2;
    public static final int TEXTAREA_LINE_HEIGHT = 8;
    public static final String TEXTAREA_LINE_DELIMITER = "</br>";

    public static final int BUTTON_BACKGROUND = Color.DARK_GRAY;
    public static final int BUTTON_BORDER = Color.BLACK;
    public static final int BUTTON_TEXT = Color.WHITE;
    public static final int BUTTON_BACKGROUND_HOVER = Color.GRAY;
    public static final int BUTTON_BORDER_HOVER = Color.DARK_GRAY;
    public static final int BUTTON_TEXT_HOVER = BUTTON_TEXT;
    public static final int BUTTON_BACKGROUND_DISABLED = BUTTON_BACKGROUND;
    public static final int BUTTON_BORDER_DISABLED = BUTTON_BORDER;
    public static final int BUTTON_TEXT_DISABLED = Color.GRAY;

    public static final int TEXT_DEFAULT_HOVER = Color.WHITE;
    public static final int TEXT_DEFAULT_DISABLED = Color.GRAY;

    public static final int POPUP_COLOR_SUCCESS = Color.DARK_GREEN;
    public static final int POPUP_COLOR_DEFAULT = Color.DARK_GRAY;
    public static final int POPUP_COLOR_NOTICE = Color.ORANGE;
    public static final int POPUP_COLOR_ERROR = Color.RED;
    public static final int POPUP_BACKGROUND = Color.WHITE;
    public static final int POPUP_TEXT = Color.DARK_GRAY;

    public static final int PROGRESS_BAR_BACKGROUND = Color.DARK_GRAY;
    public static final int PROGRESS_BAR_BORDER = Color.BLACK;
    public static final int PROGRESS_BAR_BAR_COLOR = Color.BLUE;

    public static final int TOOLTIP_BACKGROUND = Color.GRAY;
    public static final int TOOLTIP_TEXT = Color.WHITE;

    public static final int TEXT_FIELD_TEXT = Color.WHITE;
    public static final int TEXT_FIELD_ALL_SELECTED_BACKGROUND = Color.YELLOW;
    public static final int TEXT_FIELD_ALL_SELECTED_TEXT = Color.DARK_GRAY;
    public static final int TEXT_FIELD_PLACEHOLDER_TEXT = Color.DARK_GRAY;

    public static final int SCROLLBAR_THUMB_COLOR = Color.GRAY;
}
