package de.j3ramy.edomui.utils;

public class Color {
    public static final int RED = 0xffEC3636;
    public static final int ORANGE = 0xffFFAA00;
    public static final int YELLOW = 0xffFFD817;
    public static final int AQUA = 0xff55FFFF;
    public static final int BLUE = 0xff5555FF;
    public static final int GREEN = 0xff55FF55;
    public static final int PURPLE = 0xffFF55FF;

    public static final int DARK_RED = 0xffA22A2A;
    public static final int DARK_ORANGE = 0xffC98600;
    public static final int DARK_YELLOW = 0xffA79229;
    public static final int DARK_AQUA = 0xff019898;
    public static final int DARK_BLUE = 0xff3030DD;
    public static final int DARK_GREEN = 0xff0F890F;
    public static final int DARK_PURPLE = 0xffC11DC1;

    public static final int WHITE = 0xffFFFFFF;
    public static final int BLACK = 0xff0e0e0e;
    public static final int GRAY = 0xffAAAAAA;
    public static final int DARK_GRAY = 0xff555555;

    public static int getContrastColor(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        double luminance = (0.299 * r + 0.587 * g + 0.114 * b);

        return luminance > 128 ? DARK_GRAY : WHITE;
    }
}
