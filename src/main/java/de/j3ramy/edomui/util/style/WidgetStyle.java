package de.j3ramy.edomui.util.style;

import de.j3ramy.edomui.enums.WidgetState;

import java.awt.Color;

public class WidgetStyle {
    private Color backgroundColor;
    private Color borderColor;
    private Color hoverBackgroundColor;
    private Color hoverBorderColor;
    private Color disabledBackgroundColor;
    private Color disabledBorderColor;
    private int borderWidth;

    public WidgetStyle(Color backgroundColor, Color borderColor, Color hoverBackgroundColor, Color hoverBorderColor,
                       Color disabledBackgroundColor, Color disabledBorderColor, int borderWidth) {
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

    public Color getBorderColorForState(WidgetState state) {
        return switch (state) {
            case DISABLED -> disabledBorderColor;
            case HOVERED -> hoverBorderColor;
            default -> borderColor;
        };
    }

    public Color getBackgroundColorForState(WidgetState state) {
        return switch (state) {
            case DISABLED -> disabledBackgroundColor;
            case HOVERED -> hoverBackgroundColor;
            default -> backgroundColor;
        };
    }

    public Color getBackgroundColor() { return backgroundColor; }
    public Color getBorderColor() { return borderColor; }
    public Color getHoverBackgroundColor() { return hoverBackgroundColor; }
    public Color getHoverBorderColor() { return hoverBorderColor; }
    public Color getDisabledBackgroundColor() { return disabledBackgroundColor; }
    public Color getDisabledBorderColor() { return disabledBorderColor; }
    public int getBorderWidth() { return borderWidth; }

    public void setBackgroundColor(Color c) { backgroundColor = c; }
    public void setBorderColor(Color c) { borderColor = c; }
    public void setHoverBackgroundColor(Color c) { hoverBackgroundColor = c; }
    public void setHoverBorderColor(Color c) { hoverBorderColor = c; }
    public void setDisabledBackgroundColor(Color c) { disabledBackgroundColor = c; }
    public void setDisabledBorderColor(Color c) { disabledBorderColor = c; }
    public void setBorderWidth(int w) { borderWidth = w; }
}
