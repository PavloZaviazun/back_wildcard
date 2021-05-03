package com.wildcard.back.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static final String SENTENCE_PATTERN = "[A-Z|0-9](\\w+\\p{Punct}*\\s*)+";
    public static final String WORD_PATTERN = "[A-Z][a-z]+";
    public static final String JSON_PATTERN = "\\{\\s*\"ru\"\\s*\\:\\s*\"[а-яА-ЯъЪёЁэЭ]*\"\\s*\\,\\s*\"ua\"\\s*\\:\\s*\"[а-яА-ЯїЇіІєЄ'']*\"\\s*\\}";
    public static final String LOGIN_PATTERN = "[\\w.%+-]{3,30}";
    public static final String PASSWORD_PATTERN = "[\\w\\p{Punct}]{5,30}";
    public static final String EMAIL_PATTERN = "^[\\w.%+-]{3,30}@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

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

    public static String wordValidation(String word) {
        if(!checkValidation(word, WORD_PATTERN)) {
            word = firstToTitleCase(word);
        }
        if(!checkValidation(word, WORD_PATTERN)) {
            word = lastsToLowerCase(word);
        }
        if(checkValidation(word, WORD_PATTERN)) {
            return word;
        }
        return null;
    }

    public static PartOfSpeech partOfSpeechValidation(String partOfSpeech) {
        switch(partOfSpeech.toUpperCase()) {
            case "NOUN" : return PartOfSpeech.NOUN;
            case "PRONOUN" : return PartOfSpeech.PRONOUN;
            case "VERB" : return PartOfSpeech.VERB;
            case "ADVERB" : return PartOfSpeech.ADVERB;
            case "ADJECTIVE" : return PartOfSpeech.ADJECTIVE;
            case "PREPOSITION" : return PartOfSpeech.PREPOSITION;
            default : {
                System.out.println("Inappropriate part of speech");
                return null;
            }
        }
    }

    public static String sentenceValidation(String sentence) {
        if(!checkValidation(sentence, SENTENCE_PATTERN)) {
            sentence = firstToTitleCase(sentence);
        }
        if(checkValidation(sentence, SENTENCE_PATTERN)) {
            return sentence;
        }
        return null;
    }

    public static String oneStepValidation(String text, String pattern) {
        if(checkValidation(text, pattern)) {
            return text;
        }
        return null;
    }

    public static NativeLang nativeLangValidation(String nativeLang) {
        switch(nativeLang.toUpperCase()) {
            case "UA" : return NativeLang.UA;
            case "RU" : return NativeLang.RU;
            default : {
                System.out.println("Inappropriate native language");
                return null;
            }
        }
    }


}
