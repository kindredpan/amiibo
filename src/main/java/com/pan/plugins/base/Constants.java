package com.pan.plugins.base;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author panqj
 * @date 2023/05/31
 */

public class Constants {

    public static final char UNDER_LINE_MARK = '_';

    public static final char QUOTES = '"';

    private static Set<String> RETAIN_WORD = new HashSet<>(Arrays.asList("private", "protected", "public", "static", "final"));

    public static boolean isRetainWord(String word) {
        return RETAIN_WORD.contains(word);
    }


    public static final String PRIMARY_KEY = "PRIMARY KEY";

    public static final String AUTO_INCREMENT = "AUTO_INCREMENT";

    public static final String COMMENT = "COMMENT";

}
