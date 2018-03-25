package com.project.glib.model;

public class Document {
    public static final String BOOK = "BOOK";
    public static final String JOURNAL = "JOURNAL";
    public static final String AV = "AUDIO_VIDEO";
    public static final String BESTSELLER = "BESTSELLER";
    public static final String REFERENCE = "REFERENCE";
    public static final String DEFAULT_NOTE = "NA";
    private static final String[] TYPES = {BOOK, JOURNAL, AV};

    public static boolean isType(String type) {
        for (String t : TYPES) {
            if (t.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
