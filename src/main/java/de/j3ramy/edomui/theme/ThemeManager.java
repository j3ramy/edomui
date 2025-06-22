package de.j3ramy.edomui.theme;

import de.j3ramy.edomui.theme.chart.BarChartStyle;
import de.j3ramy.edomui.theme.chart.LineChartStyle;
import de.j3ramy.edomui.theme.input.CheckboxStyle;
import de.j3ramy.edomui.theme.input.DropdownStyle;
import de.j3ramy.edomui.theme.input.TextFieldStyle;
import de.j3ramy.edomui.util.style.Color;
import de.j3ramy.edomui.util.style.WidgetStyle;

public class ThemeManager {
    public static final WidgetStyle DEFAULT_STYLE = new WidgetStyle(
            Color.GRAY, Color.GREEN, Color.GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.BLACK, 1
    );

    private static WidgetStyle globalDefaultStyle = DEFAULT_STYLE;
    private static VerticalScrollbarStyle verticalScrollbarStyle = new VerticalScrollbarStyle(globalDefaultStyle);
    private static ButtonStyle defaultButtonStyle = new ButtonStyle(globalDefaultStyle);
    private static BarChartStyle defaultBarChartStyle = new BarChartStyle(globalDefaultStyle);
    private static LineChartStyle defaultLineChartStyle = new LineChartStyle(globalDefaultStyle);
    private static TextFieldStyle defaultTextFieldStyle = new TextFieldStyle(globalDefaultStyle);
    private static CheckboxStyle defaultCheckboxStyle = new CheckboxStyle(globalDefaultStyle);
    private static DropdownStyle defaultDropdownStyle = new DropdownStyle(globalDefaultStyle);
    private static TextFieldStyle defaultTextAreaStyle = new TextFieldStyle(globalDefaultStyle);
    private static ProgressBarStyle defaultProgressBarStyle = new ProgressBarStyle(globalDefaultStyle);

    public static void setGlobalDefaultStyle(WidgetStyle style) {
        globalDefaultStyle = new WidgetStyle(style);
    }

    public static WidgetStyle getGlobalDefaultStyle() {
        return new WidgetStyle(globalDefaultStyle);
    }

    public static void setVerticalScrollbarStyle(VerticalScrollbarStyle verticalScrollbarStyle) {
        ThemeManager.verticalScrollbarStyle = verticalScrollbarStyle;
    }

    public static ButtonStyle getDefaultButtonStyle() {
        return defaultButtonStyle;
    }

    public static void setDefaultButtonStyle(ButtonStyle defaultButtonStyle) {
        ThemeManager.defaultButtonStyle = defaultButtonStyle;
    }

    public static BarChartStyle getDefaultBarChartStyle() {
        return defaultBarChartStyle;
    }

    public static CheckboxStyle getDefaultCheckboxStyle() {
        return defaultCheckboxStyle;
    }

    public static TextFieldStyle getDefaultTextFieldStyle() {
        return defaultTextFieldStyle;
    }

    public static VerticalScrollbarStyle getVerticalScrollbarStyle() {
        return verticalScrollbarStyle;
    }

    public static DropdownStyle getDefaultDropdownStyle() {
        return defaultDropdownStyle;
    }

    public static TextFieldStyle getDefaultTextAreaStyle() {
        return defaultTextAreaStyle;
    }

    public static ProgressBarStyle getDefaultProgressBarStyle() {
        return defaultProgressBarStyle;
    }

    public static void setDefaultBarChartStyle(BarChartStyle defaultBarChartStyle) {
        ThemeManager.defaultBarChartStyle = defaultBarChartStyle;
    }

    public static LineChartStyle getDefaultLineChartStyle() {
        return defaultLineChartStyle;
    }

    public static void setDefaultLineChartStyle(LineChartStyle defaultLineChartStyle) {
        ThemeManager.defaultLineChartStyle = defaultLineChartStyle;
    }

    public static void setDefaultCheckboxStyle(CheckboxStyle defaultCheckboxStyle) {
        ThemeManager.defaultCheckboxStyle = defaultCheckboxStyle;
    }

    public static void setDefaultTextFieldStyle(TextFieldStyle defaultTextFieldStyle) {
        ThemeManager.defaultTextFieldStyle = defaultTextFieldStyle;
    }

    public static void setDefaultDropdownStyle(DropdownStyle defaultDropdownStyle) {
        ThemeManager.defaultDropdownStyle = defaultDropdownStyle;
    }

    public static void setDefaultTextAreaStyle(TextFieldStyle defaultTextAreaStyle) {
        ThemeManager.defaultTextAreaStyle = defaultTextAreaStyle;
    }

    public static void setDefaultProgressBarStyle(ProgressBarStyle defaultProgressBarStyle) {
        ThemeManager.defaultProgressBarStyle = defaultProgressBarStyle;
    }
}
