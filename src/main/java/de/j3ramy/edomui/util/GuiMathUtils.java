package de.j3ramy.edomui.util;

import java.util.Locale;

public class GuiMathUtils  {
    public static int clamp(int value, int min, int max){
        return java.lang.Math.max(min, java.lang.Math.min(max, value));
    }

    public static boolean isNumeric(String str, boolean isFloat, boolean allowNegative) {
        if (str == null || str.isEmpty()) return false;

        String pattern = allowNegative ? "-?" : "";
        pattern += isFloat ? "\\d+(\\.\\d+)?" : "\\d+";

        return str.matches(pattern);
    }

    public static boolean isNumeric(String str, boolean isFloat) {
        return isNumeric(str, isFloat, true);
    }

    public static boolean isPositiveInteger(String str) {
        return str != null && str.matches("\\d+");
    }

    public static long convertHourToTicks(int hour) {
        long ticksPerHour = 1000L;
        long tickOffset = 6000L; // Ticks are 0 at 6 AM
        long tickValue = (hour % 24) * ticksPerHour;

        return (24000 - tickOffset + tickValue) % 24000;
    }

    public static String formatFloatByLocale(float f, Locale locale, String currency) {
        return formatFloatByLocale(f, locale) + " " + currency;
    }

    public static String formatFloatByLocale(float f, Locale locale) {
        return String.format(locale, "%.2f", f);
    }
}
