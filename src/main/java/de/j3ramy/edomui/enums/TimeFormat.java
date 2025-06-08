package de.j3ramy.edomui.enums;

public enum TimeFormat {
    HOURS_12("hh:mm a"),
    HOURS_24("HH:mm");

    private final String format;

    TimeFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
