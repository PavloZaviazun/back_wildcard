package com.wildcard.back.util;

public class Constants {
    public static final String SEARCH_BY_WORD = "SELECT w FROM Word w WHERE w.word LIKE %:word% ORDER BY w.word";
    public static final String SEARCH_BY_LETTER = "SELECT w FROM Word w WHERE w.word LIKE :letter% ORDER BY w.word";
    public static final String SEARCH_LIB = "SELECT l FROM Lib l WHERE l.name LIKE %:name%";
    public static final String GET_LIBS_WITH_PAGINATION = "SELECT l FROM Lib l ORDER BY l.name";
    public final static String GET_LIB_ID_BY_WORD_ID = "SELECT lib_id from word_lib where word_id = ?";
    public final static String GET_WORD_ID_BY_LIB_ID = "SELECT word_id FROM word_lib WHERE lib_id = ?";
    public static final String GET_USERS_WITH_PAGINATION = "SELECT u FROM User u ORDER BY u.id";
    public static final String USER_EXISTS_ALREADY = "Користувач з таким e-mail вже існує";
    public static final String USER_ENABLE_REGISTRATION = "На ваш e-mail відправлено листа для підтвердження реєстрації";
    public static final String USER_IS_ENABLED_ALREADY = "Обліковий запис вже було активовано";
    public static final String USER_IS_ENABLED = "Ваш аккаунт активовано, дякуємо за реєстрацію!";
    public static final String ENABLE_MISTAKE = "Виникла помилка активації користувача";
    public static final String USER_UPDATE_EMAIL = "Ваш e-mail успішно змінено!";
    public static final String USER_NOT_UPDATE_EMAIL = "Ваш e-mail не був змінений";



    private Constants() {}

}
