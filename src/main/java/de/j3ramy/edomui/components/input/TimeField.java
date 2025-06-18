package de.j3ramy.edomui.components.input;

import de.j3ramy.edomui.enums.TimeFormat;
import de.j3ramy.edomui.interfaces.IValueAction;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public final class TimeField extends TextField {
    private final boolean isAmPm;
    private final Pattern allowedPattern;

    public TimeField(int x, int y, int width, int height, TimeFormat format, @Nullable IValueAction onTextChange,
                     @Nullable IValueAction onPressEnterAction) {
        super(x, y, width, height, format.getFormat(), onTextChange, onPressEnterAction);
        this.isAmPm = format == TimeFormat.HOURS_12;
        this.allowedPattern = isAmPm
                ? Pattern.compile("[0-9APMapm]")
                : Pattern.compile("[0-9]");
    }

    public TimeField(int x, int y, int width, int height, TimeFormat format, @Nullable IValueAction onTextChange) {
        this(x, y, width, height, format, onTextChange, null);
    }

    public TimeField(int x, int y, int width, int height, TimeFormat format) {
        this(x, y, width, height, format, null, null);
    }

    @Override
    public void charTyped(char codePoint) {
        if (!isFocused()) return;
        if (!allowedPattern.matcher(String.valueOf(codePoint)).matches()) return;

        deleteSelectionIfNeeded();

        String raw = getRawInput();
        if (raw.length() >= getMaxLength()) return;

        raw += Character.toUpperCase(codePoint);
        setText(formatRawInput(raw));
    }

    @Override
    public void backspace() {
        if (caretCharPos != selectionStart && selectionStart != -1) {
            deleteSelectionIfNeeded();
            triggerTextChanged();
            return;
        }

        String raw = getRawInput();
        if (raw.isEmpty()) return;

        raw = raw.substring(0, raw.length() - 1);
        setText(formatRawInput(raw));
    }

    @Override
    public void paste() {
        String clip = Minecraft.getInstance().keyboardHandler.getClipboard();
        if (clip.isEmpty()) return;

        deleteSelectionIfNeeded();

        StringBuilder filtered = new StringBuilder(getRawInput());
        for (char c : clip.toCharArray()) {
            if (allowedPattern.matcher(String.valueOf(c)).matches() && filtered.length() < getMaxLength()) {
                filtered.append(Character.toUpperCase(c));
            }
        }

        setText(formatRawInput(filtered.toString()));
    }

    private String getRawInput() {
        return getText().replaceAll("[^0-9APM]", "");
    }

    private int getMaxLength() {
        return isAmPm ? 6 : 4; // 4 digits + AM/PM if needed
    }

    private String formatRawInput(String raw) {
        StringBuilder sb = new StringBuilder();
        int index = 0;

        // hh:mm
        if (raw.length() > index) sb.append(raw.charAt(index++));
        if (raw.length() > index) sb.append(raw.charAt(index++)).append(':');
        if (raw.length() > index) sb.append(raw.charAt(index++));
        if (raw.length() > index) sb.append(raw.charAt(index++));

        // AM/PM for 12h format
        if (isAmPm && raw.length() > 4) {
            sb.append(' ');
            for (int i = 4; i < raw.length(); i++) {
                char c = raw.charAt(i);
                if (c == 'A' || c == 'P' || c == 'M') {
                    sb.append(c);
                }
            }
        }

        return sb.toString();
    }
}
