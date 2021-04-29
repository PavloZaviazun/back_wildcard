package com.wildcard.back.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static final String SENTENCE_PATTERN = "[A-Z|0-9](\\w+\\p{Punct}*\\s*)+";
    public static final String WORD_PATTERN = "[A-Z][a-z]+";
    public static final String JSON_PATTERN = "\\{\\s*\"ru\"\\s*\\:\\s*\"[а-яА-ЯъЪёЁэЭ]*\"\\s*\\,\\s*\"ua\"\\s*\\:\\s*\"[а-яА-ЯїЇєЄ]*\"\\s*\\}";

    public static boolean checkValidation(String text, String expression) {
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static String firstToTitleCase(String text) {
            text = text.trim();
            char first = Character.toTitleCase(text.substring(0, 1).toCharArray()[0]);
            return text.replaceFirst("\\w", String.valueOf(first));
    }

    public static String lastsToLowerCase(String text) {
        StringBuilder formattedWord = new StringBuilder();
        text = text.trim();
        char[] chars = text.toCharArray();
        formattedWord.append(Character.toTitleCase(chars[0]));
        for(int i = 1; i < text.length(); i++) {
            formattedWord.append(Character.toLowerCase(chars[i]));
        }
        return formattedWord.toString();
    }
}
