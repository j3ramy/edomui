package de.j3ramy.edomui.component.input;

import de.j3ramy.edomui.enums.DateFormat;
import de.j3ramy.edomui.interfaces.IValueAction;
import de.j3ramy.edomui.util.style.GuiUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class DateField extends TextField {
    private final String formatMask;
    private final List<Integer> separatorPositions = new ArrayList<>();
    private final char separatorChar;
    private final Pattern numericPattern = Pattern.compile("[0-9]");

    public DateField(int x, int y, int width, int height, DateFormat format, @Nullable IValueAction onTextChangeAction,
                     @Nullable IValueAction onPressEnterAction) {

        super(x, y, width, height, GuiUtils.getDateFormat(format), onTextChangeAction, onPressEnterAction);

        this.formatMask = GuiUtils.getDateFormat(format);
        char sep = '.';
        for (char c : formatMask.toCharArray()) {
            if (!Character.isLetter(c)) {
                sep = c;
                break;
            }
        }
        this.separatorChar = sep;

        for (int i = 0; i < formatMask.length(); i++) {
            if (formatMask.charAt(i) == separatorChar) {
                separatorPositions.add(i);
            }
        }
    }

    public DateField(int x, int y, int width, int height, DateFormat format, @Nullable IValueAction onTextChangeAction) {
        this(x, y, width, height, format, onTextChangeAction, null);
    }

    public DateField(int x, int y, int width, int height, DateFormat format) {
        this(x, y, width, height, format, null, null);
    }

    @Override
    public void charTyped(char codePoint) {
        if (!isFocused()) return;
        if (!numericPattern.matcher(String.valueOf(codePoint)).matches()) return;

        String raw = getText().replaceAll("[^0-9]", "");
        if (raw.length() >= formatMask.replaceAll("[^dMy]", "").length()) return;

        raw += codePoint;
        setText(formatRawInput(raw));
    }

    @Override
    public void paste() {
        String clipboard = net.minecraft.client.Minecraft.getInstance().keyboardHandler.getClipboard();

        String raw = clipboard.replaceAll("[^0-9]", "");
        int maxDigits = formatMask.replaceAll("[^dMy]", "").length();
        if (raw.length() > maxDigits) {
            raw = raw.substring(0, maxDigits);
        }

        setText(formatRawInput(raw));
    }
    @Override
    public void backspace() {
        if (caretCharPos != selectionStart && selectionStart != -1) {
            deleteSelectionIfNeeded();
            triggerTextChanged();
            return;
        }

        String raw = getText().replaceAll("[^0-9]", "");
        if (raw.isEmpty()) return;

        raw = raw.substring(0, raw.length() - 1);
        setText(formatRawInput(raw));
    }


    private String formatRawInput(String raw) {
        StringBuilder formatted = new StringBuilder();
        int digitIndex = 0;
        for (int i = 0; i < formatMask.length(); i++) {
            if (separatorPositions.contains(i)) {
                formatted.append(separatorChar);
            } else if (digitIndex < raw.length()) {
                formatted.append(raw.charAt(digitIndex++));
            } else {
                break;
            }
        }
        return formatted.toString();
    }
}
