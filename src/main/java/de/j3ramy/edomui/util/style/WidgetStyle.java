package de.j3ramy.edomui.util.style;

import de.j3ramy.edomui.enums.WidgetState;

public class WidgetStyle {
    private int backgroundColor;
    private int borderColor;
    private int hoverBackgroundColor;
    private int hoverBorderColor;
    private int disabledBackgroundColor;
    private int disabledBorderColor;
    private int borderWidth;

    public WidgetStyle(int backgroundColor, int borderColor, int hoverBackgroundColor, int hoverBorderColor,
                       int disabledBackgroundColor, int disabledBorderColor, int borderWidth) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.hoverBackgroundColor = hoverBackgroundColor;
        this.hoverBorderColor = hoverBorderColor;
        this.disabledBackgroundColor = disabledBackgroundColor;
        this.disabledBorderColor = disabledBorderColor;
        this.borderWidth = borderWidth;
    }

    public WidgetStyle(WidgetStyle other) {
        this(other.backgroundColor, other.borderColor, other.hoverBackgroundColor, other.hoverBorderColor,
                other.disabledBackgroundColor, other.disabledBorderColor, other.borderWidth);
    }

    public int getBorderColorForState(WidgetState state) {
        return switch (state) {
            case DISABLED -> disabledBorderColor;
            case HOVERED -> hoverBorderColor;
            default -> borderColor;
        };
    }

    public int getBackgroundColorForState(WidgetState state) {
        return switch (state) {
            case DISABLED -> disabledBackgroundColor;
            case HOVERED -> hoverBackgroundColor;
            default -> backgroundColor;
        };
    }

    public int getBackgroundColor() { return backgroundColor; }
    public int getBorderColor() { return borderColor; }
    public int getHoverBackgroundColor() { return hoverBackgroundColor; }
    public int getHoverBorderColor() { return hoverBorderColor; }
    public int getDisabledBackgroundColor() { return disabledBackgroundColor; }
    public int getDisabledBorderColor() { return disabledBorderColor; }
    public int getBorderWidth() { return borderWidth; }

    public void setBackgroundColor(int c) { backgroundColor = c; }
    public void setBorderColor(int c) { borderColor = c; }
    public void setHoverBackgroundColor(int c) { hoverBackgroundColor = c; }
    public void setHoverBorderColor(int c) { hoverBorderColor = c; }
    public void setDisabledBackgroundColor(int c) { disabledBackgroundColor = c; }
    public void setDisabledBorderColor(int c) { disabledBorderColor = c; }
    public void setBorderWidth(int w) { borderWidth = w; }
}
