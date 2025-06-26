package de.j3ramy.edomui.theme;

import de.j3ramy.edomui.theme.chart.BarChartStyle;
import de.j3ramy.edomui.theme.chart.LineChartStyle;
import de.j3ramy.edomui.theme.input.CheckboxStyle;
import de.j3ramy.edomui.theme.input.DropdownStyle;
import de.j3ramy.edomui.theme.input.TextFieldStyle;
import de.j3ramy.edomui.theme.text.TextStyle;
import de.j3ramy.edomui.theme.text.TooltipStyle;
import de.j3ramy.edomui.util.style.WidgetStyle;

import java.awt.*;

public class ThemeManager {
    public static final WidgetStyle DEFAULT_STYLE = new WidgetStyle(
            Color.GRAY, Color.BLACK, Color.GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.BLACK, 1
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
    private static PopUpStyle defaultPopUpStyle = new PopUpStyle(globalDefaultStyle);
    private static TextStyle defaultTextStyle = new TextStyle(globalDefaultStyle);
    private static TooltipStyle defaultTooltipStyle = new TooltipStyle(globalDefaultStyle);
    private static ScrollableListStyle defaultScrollableListStyle = new ScrollableListStyle(globalDefaultStyle);
    private static ContextMenuStyle defaultContextMenuStyle = new ContextMenuStyle(globalDefaultStyle);
    private static GridStyle defaultGridStyle = new GridStyle(globalDefaultStyle);
    private static TableStyle defaultTableStyle = new TableStyle(globalDefaultStyle);

    public static void setGlobalDefaultStyle(WidgetStyle style) {
        globalDefaultStyle = new WidgetStyle(style);
    }

    public static WidgetStyle getGlobalDefaultStyle() {
        return new WidgetStyle(globalDefaultStyle);
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

    public static TextStyle getDefaultTextStyle() {
        return defaultTextStyle;
    }

    public static TooltipStyle getDefaultTooltipStyle() {
        return defaultTooltipStyle;
    }

    public static ScrollableListStyle getDefaultScrollableListStyle() {
        return defaultScrollableListStyle;
    }

    public static ContextMenuStyle getDefaultContextMenuStyle() {
        return defaultContextMenuStyle;
    }

    public static GridStyle getDefaultGridStyle() {
        return defaultGridStyle;
    }

    public static void setVerticalScrollbarStyle(VerticalScrollbarStyle verticalScrollbarStyle) {
        ThemeManager.verticalScrollbarStyle = verticalScrollbarStyle;
    }

    public static void setDefaultTextStyle(TextStyle defaultTextStyle) {
        ThemeManager.defaultTextStyle = defaultTextStyle;
    }

    public static TableStyle getDefaultTableStyle() {
        return defaultTableStyle;
    }

    public static void setDefaultBarChartStyle(BarChartStyle defaultBarChartStyle) {
        ThemeManager.defaultBarChartStyle = defaultBarChartStyle;
    }

    public static LineChartStyle getDefaultLineChartStyle() {
        return defaultLineChartStyle;
    }

    public static PopUpStyle getDefaultPopUpStyle() {
        return defaultPopUpStyle;
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

    public static void setDefaultPopUpStyle(PopUpStyle defaultPopUpStyle) {
        ThemeManager.defaultPopUpStyle = defaultPopUpStyle;
    }

    public static void setDefaultTooltipStyle(TooltipStyle defaultTooltipStyle) {
        ThemeManager.defaultTooltipStyle = defaultTooltipStyle;
    }

    public static void setDefaultScrollableListStyle(ScrollableListStyle defaultScrollableListStyle) {
        ThemeManager.defaultScrollableListStyle = defaultScrollableListStyle;
    }

    public static void setDefaultContextMenuStyle(ContextMenuStyle defaultContextMenuStyle) {
        ThemeManager.defaultContextMenuStyle = defaultContextMenuStyle;
    }

    public static void setDefaultGridStyle(GridStyle defaultGridStyle) {
        ThemeManager.defaultGridStyle = defaultGridStyle;
    }

    public static void setDefaultTableStyle(TableStyle defaultTableStyle) {
        ThemeManager.defaultTableStyle = defaultTableStyle;
    }
}
