package de.j3ramy.edomui.util.style;

import de.j3ramy.edomui.enums.CompareOperation;
import de.j3ramy.edomui.enums.DateFormat;
import de.j3ramy.edomui.enums.FontSize;
import de.j3ramy.edomui.enums.TimeFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TranslatableComponent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class GuiUtils {
    public static float getFontScale(FontSize fontSize) {
        return switch (fontSize) {
            case XXS -> 0.4f;
            case XS -> 0.5f;
            case S -> 0.75f;
            case L -> 1.5f;
            default -> 1f;
        };
    }

    public static int getTextWidth(String text, float fontSizeScale){
        return (int) (Minecraft.getInstance().font.width(text) * fontSizeScale);
    }

    public static String getTruncatedLabel(String s, float fontSizeScale, int maxTextWidth) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        Font font = Minecraft.getInstance().font;
        int scaledTextWidth = (int) (font.width(s) * fontSizeScale);
        if (scaledTextWidth <= maxTextWidth) {
            return s;
        }

        int ellipsisWidth = (int) (font.width("...") * fontSizeScale);
        int availableWidth = maxTextWidth - ellipsisWidth;
        if (availableWidth <= 0) {
            return "...";
        }

        int currentWidth = 0;
        StringBuilder truncatedText = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int charWidth = (int) (font.width(String.valueOf(c)) * fontSizeScale);
            if (currentWidth + charWidth > availableWidth) {
                break;
            }
            truncatedText.append(c);
            currentWidth += charWidth;
        }

        return truncatedText + "...";
    }

    public static String getFormattedTime(boolean useIngameTime, TimeFormat timeFormat) {
        int hours;
        int minutes;

        if (useIngameTime && Minecraft.getInstance().level != null) {
            long worldTime = Minecraft.getInstance().level.getDayTime() % 24000;
            hours = (int) ((worldTime / 1000 + 6) % 24);
            minutes = (int) (60 * (worldTime % 1000) / 1000);
        } else {
            Calendar calendar = Calendar.getInstance();
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minutes = calendar.get(Calendar.MINUTE);
        }

        if (timeFormat == TimeFormat.HOURS_12) {
            return String.format("%02d:%02d %s", (hours % 12 == 0) ? 12 : hours % 12, minutes,
                    (hours < 12) ? "AM" : "PM");
        } else {
            return String.format("%02d:%02d", hours, minutes);
        }
    }

    public static String getDatetimeNow(boolean useIngameTime, DateFormat dateFormat, TimeFormat timeFormat){
        return getDateAsString(useIngameTime, dateFormat) + " " + getFormattedTime(useIngameTime, timeFormat);
    }

    public static String getDateNow(boolean useIngameTime, DateFormat dateFormat){
        return getDateAsString(useIngameTime, dateFormat);
    }


    private static final Calendar calendar = Calendar.getInstance();
    public static String getDateAsString(boolean useIngameTime, DateFormat dateFormat){
        if(useIngameTime && Minecraft.getInstance().level != null){
            return "Day " + (Minecraft.getInstance().level.getDayTime() / 24000);
        }
        else{
            String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
            String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
            String year = String.valueOf(calendar.get(Calendar.YEAR));

            switch (dateFormat){
                case DE -> {
                    return day + "." + month + "." + year;
                }
                case UK -> {
                    return day + "/" + month + "/" + year;
                }
                default -> {
                    return month + "/" + day + "/" + year;
                }
            }
        }
    }

    public static String getDateFormat(DateFormat dateFormat){
        switch (dateFormat){
            case DE -> {
                return "dd.MM.yyyy";
            }
            case UK -> {
                return "dd/MM/yyyy";
            }
            default -> {
                return "MM/dd/yyyy";
            }
        }
    }

    public static Date convertStringToDate(String dateStr, DateFormat dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat(dateFormat));
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date convertStringToDatetime(String dateStr, DateFormat dateFormat, TimeFormat timeFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat(dateFormat) + " " + getTimeFormat(timeFormat));
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getTimeFormat(TimeFormat timeFormat){
        switch (timeFormat){
            case HOURS_12 -> {
                return TimeFormat.HOURS_12.getFormat();
            }
            case HOURS_24 -> {
                return TimeFormat.HOURS_24.getFormat();
            }
        }

        return "";
    }

    public static String getFormattedDate(Date date, DateFormat dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(getDateFormat(dateFormat));
        return formatter.format(date);
    }

    public static String getStartOfCurrentWeek(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getEndOfCurrentWeek(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getStartOfLastWeek(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getEndOfLastWeek(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getStartOfCurrentMonth(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getEndOfCurrentMonth(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getStartOfLastMonth(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getEndOfLastMonth(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getStartOfCurrentYear(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getEndOfCurrentYear(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getStartOfLastYear(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }

    public static String getEndOfLastYear(DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return getFormattedDate(calendar.getTime(), dateFormat);
    }


    public static String getTranslationText(String modid, String translationKey){
        return new TranslatableComponent("screen." + modid + "." + translationKey).getString();
    }

    public static String getTranslationText(String modId, String translationKey, String... args){
        return getTranslationTextComponent(modId, translationKey, args).getString();
    }

    public static TranslatableComponent getTranslationTextComponent(String modid, String translationKey){
        return new TranslatableComponent("screen." + modid + "." + translationKey);
    }

    public static TranslatableComponent getTranslationTextComponent(String modid, String translationKey, String... args){
        return new TranslatableComponent("screen." + modid + "." + translationKey, (Object[]) args);
    }

    public static boolean compareTimestamps(String timestamp1, String timestamp2, CompareOperation operation){
        if(timestamp1.isEmpty() || timestamp2.isEmpty()){
            return false;
        }

        try {
            Date date1 = parseTimestampOrDate(timestamp1);
            Date date2 = parseTimestampOrDate(timestamp2);

            switch (operation){
                case GREATER_THAN -> {
                    return date1.after(date2);
                }
                case GREATER_EQUALS -> {
                    return date1.after(date2) || date1.equals(date2);
                }
                case LESS_THAN -> {
                    return date1.before(date2);
                }
                case LESS_EQUALS -> {
                    return date1.before(date2) || date1.equals(date2);
                }
                default -> {
                    return date1.equals(date2);
                }
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static Date parseTimestampOrDate(String timestamp) {
        String[] formatsWithTime = {
                "dd.MM.yyyy HH:mm", "dd.MM.yyyy hh:mm a",
                "dd/MM/yyyy HH:mm", "dd/MM/yyyy hh:mm a",
                "MM/dd/yyyy HH:mm", "MM/dd/yyyy hh:mm a"
        };

        String[] formatsWithoutTime = {
                "dd.MM.yyyy", "dd/MM/yyyy", "MM/dd/yyyy"
        };

        for (String format : formatsWithTime) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                return sdf.parse(timestamp);
            } catch (ParseException ignored) {}
        }

        for (String format : formatsWithoutTime) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                Date dateWithoutTime = sdf.parse(timestamp);
                return removeTime(dateWithoutTime);
            } catch (ParseException ignored) {}
        }

        return new Date();
    }

    private static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static boolean isValidDate(String dateString, String dateFormatPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(dateString);
            String formattedDate = dateFormat.format(date);

            return formattedDate.equals(dateString);
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidTimeFormat(TimeFormat timeFormat, String timeString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat.getFormat());
        dateFormat.setLenient(false);

        try {
            Date parsedDate = dateFormat.parse(timeString);
            return timeString.equalsIgnoreCase(dateFormat.format(parsedDate));
        } catch (ParseException e) {
            return false;
        }
    }

    public static <T extends Enum<T>> boolean isValidEnumValue(String value, Class<T> enumClass) {
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static <E extends Enum<E>> ArrayList<String> enumToStringList(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name).collect(Collectors.toCollection(ArrayList::new));
    }

    public static String getFormatting(Formatting formatting){
        switch (formatting){
            case BOLD -> {return "§l";}
            case STRIKETHROUGH -> {return "§m";}
            case UNDERLINE -> {return "§n";}
            case ITALIC -> {return "§o";}

            case BLACK -> {return "§0";}
            case DARK_BLUE -> {return "§1";}
            case DARK_GREEN -> {return "§2";}
            case DARK_AQUA -> {return "§3";}
            case DARK_RED -> {return "§4";}
            case DARK_PURPLE -> {return "§5";}
            case GOLD -> {return "§6";}
            case GRAY -> {return "§7";}
            case DARK_GRAY -> {return "§8";}
            case BLUE -> {return "§9";}
            case GREEN -> {return "§a";}
            case AQUA -> {return "§b";}
            case RED -> {return "§c";}
            case LIGHT_PURPLE -> {return "§d";}
            case YELLOW -> {return "§e";}
            case WHITE -> {return "§f";}

            case PREFIX -> {return "§";}
            default -> {return "§r";}
        }
    }

    public enum Formatting{
        BOLD,
        STRIKETHROUGH,
        UNDERLINE,
        ITALIC,
        RESET,
        BLACK,
        DARK_BLUE,
        DARK_GREEN,
        DARK_AQUA,
        DARK_RED,
        DARK_PURPLE,
        GOLD,
        GRAY,
        DARK_GRAY,
        BLUE,
        GREEN,
        AQUA,
        RED,
        LIGHT_PURPLE,
        YELLOW,
        WHITE,

        PREFIX
    }
}
