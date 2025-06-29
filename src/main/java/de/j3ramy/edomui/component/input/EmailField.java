package de.j3ramy.edomui.component.input;

import de.j3ramy.edomui.interfaces.IValueAction;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class EmailField extends TextField {
    // RFC 5322
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );

    public EmailField(int x, int y, int width, int height, String placeholder, @Nullable IValueAction onTextChange,
                      @Nullable IValueAction onEnter) {
        super(x, y, width, height, placeholder, onTextChange, onEnter);

        // Forbidden Email-Chars
        this.addForbiddenCharacters(new char[]{' ', '\t', '\n', '\r'});
    }

    public EmailField(int x, int y, int width, int height, String placeholder, @Nullable IValueAction onTextChange) {
        this(x, y, width, height, placeholder, onTextChange, null);
    }

    public EmailField(int x, int y, int width, int height, String placeholder) {
        this(x, y, width, height, placeholder, null, null);
    }

    public EmailField(int x, int y, int width, int height) {
        this(x, y, width, height, "john@doe.com", null, null);
    }

    public boolean isValid() {
        String email = getText().trim();
        return isValid(email);
    }

    public static boolean isValid(String email) {
        if (email.isEmpty()) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches() && isValidEmailStructure(email);
    }


    private static boolean isValidEmailStructure(String email) {
        // Check if exactly one @ car
        int atCount = 0;
        int atIndex = -1;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                atCount++;
                atIndex = i;
            }
        }

        if (atCount != 1 || atIndex == 0 || atIndex >= email.length() - 1) {
            return false;
        }

        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex + 1);

        // Local part
        if (localPart.length() > 64 || localPart.startsWith(".") || localPart.endsWith(".") ||
                localPart.contains("..")) {
            return false;
        }

        // Domain part
        if (domainPart.length() > 253 || domainPart.startsWith("-") || domainPart.endsWith("-") ||
                domainPart.startsWith(".") || domainPart.endsWith(".") || domainPart.contains("..")) {
            return false;
        }

        return domainPart.contains(".");
    }

    public String getNormalizedEmail() {
        return getText().trim().toLowerCase();
    }
}
