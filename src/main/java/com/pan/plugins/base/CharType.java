package com.pan.plugins.base;

public enum CharType {

    LETTER,
    SYMBOL,
    OTHER;

    public static CharType getCharType(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            return CharType.LETTER;
        } else if (c <= 32) {
            return CharType.OTHER;
        } else {
            return CharType.SYMBOL;
        }
    }
}
