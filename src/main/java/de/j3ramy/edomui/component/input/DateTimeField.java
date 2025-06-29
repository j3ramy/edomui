package de.j3ramy.edomui.component.input;

import de.j3ramy.edomui.enums.DateFormat;
import de.j3ramy.edomui.enums.TimeFormat;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.util.style.GuiUtils;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public final class DateTimeField extends TextField {
    private static final String DATETIME_SEPARATOR = " ";

    private final DateFormat dateFormat;
    private final TimeFormat timeFormat;
    private final String formatMask;
    private final char dateSeparator;
    private final Pattern allowedPattern;
    private final int datePartLength;
    private final int totalLength;

    public DateTimeField(int x, int y, int width, int height, DateFormat dateFormat, TimeFormat timeFormat,
                         @Nullable IValueAction onTextChangeAction, @Nullable IValueAction onPressEnterAction) {

        super(x, y, width, height, createPlaceholder(dateFormat, timeFormat), onTextChangeAction, onPressEnterAction);

        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
        this.formatMask = createFormatMask(dateFormat, timeFormat);

        String dateFormatStr = GuiUtils.getDateFormat(dateFormat);
        char sep = '.';
        for (char c : dateFormatStr.toCharArray()) {
            if (!Character.isLetter(c)) {
                sep = c;
                break;
            }
        }
        this.dateSeparator = sep;

        this.datePartLength = dateFormatStr.length();
        int timePartLength = timeFormat.getFormat().length();
        this.totalLength = datePartLength + DATETIME_SEPARATOR.length() + timePartLength;

        this.allowedPattern = timeFormat == TimeFormat.HOURS_12
                ? Pattern.compile("[0-9APMapm\\s" + Pattern.quote(String.valueOf(dateSeparator)) + ":]")
                : Pattern.compile("[0-9\\s" + Pattern.quote(String.valueOf(dateSeparator)) + ":]");

        this.setMaxLength(totalLength);
    }

    public DateTimeField(int x, int y, int width, int height, DateFormat dateFormat, TimeFormat timeFormat,
                         @Nullable IValueAction onTextChangeAction) {
        this(x, y, width, height, dateFormat, timeFormat, onTextChangeAction, null);
    }

    public DateTimeField(int x, int y, int width, int height, DateFormat dateFormat, TimeFormat timeFormat) {
        this(x, y, width, height, dateFormat, timeFormat, null, null);
    }

    public DateTimeField(int x, int y, int width, int height) {
        this(x, y, width, height, DateFormat.DE, TimeFormat.HOURS_24, null, null);
    }

    @Override
    public void charTyped(char codePoint) {
        if (!isFocused()) return;
        if (!allowedPattern.matcher(String.valueOf(codePoint)).matches()) return;

        deleteSelectionIfNeeded();

        String currentText = getText();
        String rawInput = extractRawInput(currentText);

        int maxRawLength = getMaxRawLength();
        if (rawInput.length() >= maxRawLength) return;

        if (Character.isDigit(codePoint)) {
            rawInput += codePoint;
        } else if ((codePoint == 'A' || codePoint == 'P' || codePoint == 'M') && timeFormat == TimeFormat.HOURS_12) {
            rawInput += Character.toUpperCase(codePoint);
        }

        setText(formatInput(rawInput));
        triggerTextChanged();
    }

    @Override
    public void backspace() {
        if (caretCharPos != selectionStart && selectionStart != -1) {
            deleteSelectionIfNeeded();
            triggerTextChanged();
            return;
        }

        String rawInput = extractRawInput(getText());
        if (rawInput.isEmpty()) return;

        rawInput = rawInput.substring(0, rawInput.length() - 1);
        setText(formatInput(rawInput));
        triggerTextChanged();
    }

    @Override
    public void paste() {
        String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
        if (clipboard.isEmpty()) return;

        deleteSelectionIfNeeded();

        String rawInput = extractRawInput(getText());
        StringBuilder filtered = new StringBuilder(rawInput);

        for (char c : clipboard.toCharArray()) {
            if (allowedPattern.matcher(String.valueOf(c)).matches() && filtered.length() < getMaxRawLength()) {
                if (Character.isDigit(c)) {
                    filtered.append(c);
                } else if ((c == 'A' || c == 'P' || c == 'M') && timeFormat == TimeFormat.HOURS_12) {
                    filtered.append(Character.toUpperCase(c));
                }
            }
        }

        setText(formatInput(filtered.toString()));
        triggerTextChanged();
    }

    private static String createPlaceholder(DateFormat dateFormat, TimeFormat timeFormat) {
        return GuiUtils.getDateFormat(dateFormat) + DATETIME_SEPARATOR + timeFormat.getFormat();
    }

    private static String createFormatMask(DateFormat dateFormat, TimeFormat timeFormat) {
        return GuiUtils.getDateFormat(dateFormat) + DATETIME_SEPARATOR + timeFormat.getFormat();
    }

    private String extractRawInput(String text) {
        if (timeFormat == TimeFormat.HOURS_12) {
            return text.replaceAll("[^0-9APM]", "");
        } else {
            return text.replaceAll("[^0-9]", "");
        }
    }

    private int getMaxRawLength() {
        // Date: 8 chars (ddMMyyyy), Time: 4 chars (HHmm), AM/PM: 2 chars
        return 8 + 4 + (timeFormat == TimeFormat.HOURS_12 ? 2 : 0);
    }

    private String formatInput(String rawInput) {
        StringBuilder formatted = new StringBuilder();
        int digitIndex = 0;
        int letterIndex = 0;

        for (int i = 0; i < formatMask.length() && (digitIndex < rawInput.length() || Character.isLetter(formatMask.charAt(i))); i++) {
            char maskChar = formatMask.charAt(i);

            if (maskChar == dateSeparator || maskChar == ':' || maskChar == ' ') {
                formatted.append(maskChar);
            } else if (Character.isLetter(maskChar)) {
                if (digitIndex < rawInput.length() && Character.isDigit(rawInput.charAt(digitIndex))) {
                    formatted.append(rawInput.charAt(digitIndex++));
                } else {
                    break;
                }
            } else {
                if (timeFormat == TimeFormat.HOURS_12 && formatted.length() >= datePartLength + DATETIME_SEPARATOR.length() + 5) {
                    while (letterIndex + digitIndex < rawInput.length()) {
                        char c = rawInput.charAt(letterIndex + digitIndex);
                        if (c == 'A' || c == 'P' || c == 'M') {
                            formatted.append(c);
                            letterIndex++;
                            break;
                        }
                        letterIndex++;
                    }
                    break;
                }
            }
        }

        return formatted.toString();
    }

    private static LocalDateTime parseDateTime(String text, DateFormat dateFormat, TimeFormat timeFormat) {
        try {
            DateTimeFormatter formatter = createFormatter(dateFormat, timeFormat);
            return LocalDateTime.parse(text, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static DateTimeFormatter createFormatter(DateFormat dateFormat, TimeFormat timeFormat) {
        String datePattern = switch (dateFormat) {
            case DE -> "dd.MM.yyyy";
            case UK -> "dd/MM/yyyy";
            case US -> "MM/dd/yyyy";
        };

        String timePattern = switch (timeFormat) {
            case HOURS_24 -> "HH:mm";
            case HOURS_12 -> "hh:mm a";
        };

        return DateTimeFormatter.ofPattern(datePattern + " " + timePattern);
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public TimeFormat getTimeFormat() {
        return timeFormat;
    }

    public String getFormatMask() {
        return formatMask;
    }

    public boolean isValid() {
        return isValid(String.valueOf(this.text), this.dateFormat, this.timeFormat);
    }

    public static boolean isValid(String text, DateFormat dateFormat, TimeFormat timeFormat) {
        try {
            if (text.isEmpty()) return false;

            LocalDateTime dateTime = parseDateTime(text, dateFormat, timeFormat);
            return dateTime != null;
        } catch (Exception e) {
            return false;
        }
    }

    public LocalDateTime getDateTime() {
        try {
            return parseDateTime(getText().trim(), dateFormat, timeFormat);
        } catch (Exception e) {
            return null;
        }
    }

    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            clear();
            return;
        }

        DateTimeFormatter formatter = createFormatter(dateFormat, timeFormat);
        setText(dateTime.format(formatter));
        triggerTextChanged();
    }

    public String getDatePart() {
        String text = getText().trim();
        if (text.length() >= datePartLength) {
            return text.substring(0, datePartLength);
        }
        return "";
    }

    public String getTimePart() {
        String text = getText().trim();
        int timeStartIndex = datePartLength + DATETIME_SEPARATOR.length();
        if (text.length() > timeStartIndex) {
            return text.substring(timeStartIndex);
        }
        return "";
    }

    public boolean hasDateOnly() {
        String text = getText().trim();
        return text.length() >= datePartLength && text.length() <= datePartLength + DATETIME_SEPARATOR.length();
    }

    public boolean hasDateTime() {
        return getText().trim().length() >= totalLength - 2;
    }

}