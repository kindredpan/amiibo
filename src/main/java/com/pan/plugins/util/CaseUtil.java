package com.pan.plugins.util;

import com.pan.plugins.base.CharType;
import com.pan.plugins.base.Constants;
import com.pan.plugins.biz.camelCase.base.Word;
import com.pan.plugins.biz.sqlParse.base.Column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author panqj
 * @date 2023/05/31
 */

public class CaseUtil {

    public static List<Word> contextSplit(String content) throws IndexOutOfBoundsException {
        final char[] charArray = content.toCharArray();
        ArrayList<Word> wordList = new ArrayList<>();
        boolean spiltFlag = true;
        for (final char c : charArray) {
            CharType charType = CharType.getCharType(c);
            // 判断是否为字母或下划线
            boolean isLetter = charType.equals(CharType.LETTER) || c == Constants.UNDER_LINE_MARK;
            if (isLetter) {
                if (spiltFlag) {
                    wordList.add(new Word(String.valueOf(c), Word.Type.WORD));
                    spiltFlag = false;
                } else {
                    Word word = wordList.get(wordList.size() - 1);
                    word.setText(word.getText() + c);
                }
            } else {
                spiltFlag = true;
                Word.Type type = CharType.SYMBOL.equals(charType) ? Word.Type.SYMBOL : Word.Type.IGNORE_SYMBOL;
                wordList.add(new Word(String.valueOf(c), type));
            }
        }
        return wordList;
    }

    public static List<Column> sqlSpilt(String content) {
        ArrayList<Column> columns = new ArrayList<>();

        String[] sentence = content.split("[,;]");
        for (String s : sentence) {
            if (null != s && !"".equals(s.trim())) {
                s = s.trim();
                String[] words = s.split("\\s|\\n|\\t");
                String columnName = null;
                String columnType = null;
                String comment = null;
                if (words.length > 0) {
                    columnName = words[0];
                }
                if (words.length > 1) {
                    columnType = words[1];
                }
                if (words.length > 2) {
                    List<String> wordList = Arrays.stream(words).toList();
                    int commentIndex = Math.max(wordList.indexOf("COMMENT"), wordList.indexOf("comment"));
                    if (commentIndex > 0 && ++commentIndex < words.length) {
                        comment = words[commentIndex];
                    }
                }

                // 组装
                if (null == columnName || "".equals(columnName.trim())) {
                    continue;
                }
                if (null == columnType || "".equals(columnType.trim())) {
                    columnType = "VARCHAR";
                }
                if (null == comment) {
                    comment = "";
                }
                columns.add(new Column(columnName, columnType, comment));
            }
        }
        return columns;
    }

    public static String caseCamel(String content) {
        char[] contentCharArr = content.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contentCharArr.length; i++) {
            char c = contentCharArr[i];
            if (Constants.UNDER_LINE_MARK == c) {
                c = contentCharArr[++i];
                sb.append(charChange(c));
            } else {
                sb.append(c);
            }

        }
        return sb.toString();
    }

    public static String caseNormal(String content) {
        char[] contentCharArr = content.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(contentCharArr[0]);
        for (int i = 1; i < contentCharArr.length; i++) {
            char c = contentCharArr[i];
            if (c >= 'A' && c <= 'Z') {
                sb.append(Constants.UNDER_LINE_MARK);
                sb.append(charChange(c));
            } else {
                sb.append(c);
            }

        }
        return sb.toString();
    }

    public static char charChange(char c1) {
        if (c1 >= 'A' && c1 <= 'Z') {
            return (char) (c1 + 32);
        } else if (c1 >= 'a' && c1 <= 'z') {
            return (char) (c1 - 32);
        } else {
            return c1;
        }
    }

    public static String sqlVarFormat(String s) {
        return s.replaceAll("`", "");
    }

}
