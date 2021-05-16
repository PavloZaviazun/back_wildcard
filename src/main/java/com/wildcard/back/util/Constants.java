package com.wildcard.back.util;

public class Constants {

    public static final String SENTENCE_PATTERN = "[A-Z|0-9](\\w+\\p{Punct}*\\s*)+";
    public static final String WORD_PATTERN = "[A-Z][a-z]+";
    public static final String JSON_PATTERN = "\\{\\s*\"ru\"\\s*\\:\\s*\"([а-яА-ЯъЪёЁэЭ;\\s]*;?)*\"\\s*\\,\\s*\"ua\"\\s*\\:\\s*\"[а-яА-ЯїЇіІєЄ'';\\s]*\"\\s*\\}";
    public static final String LOGIN_PATTERN = "[\\w.%+-]{3,30}";
    public static final String PASSWORD_PATTERN = "[\\w\\p{Punct}]{5,30}";
    public static final String EMAIL_PATTERN = "^[\\w.%+-]{3,30}@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public static final String GET_RANDOM_WORDS = "SELECT w FROM Word w WHERE w.isApproved = true";
    public static final String SEARCH_BY_WORD = "SELECT w FROM Word w WHERE w.word LIKE %:word% ORDER BY w.word";
    public static final String SEARCH_BY_LETTER = "SELECT w FROM Word w WHERE w.word LIKE :letter% ORDER BY w.word";
    public static final String SEARCH_LIB = "SELECT l FROM Lib l WHERE l.name LIKE %:name%";
    public static final String FIND_ONE_LIB = "SELECT l FROM Lib l WHERE l.name = :name";
    public static final String GET_LIBS_WITH_PAGINATION = "SELECT l FROM Lib l ORDER BY l.name";
    public final static String GET_LIB_ID_BY_WORD_ID = "SELECT lib_id from word_lib where word_id = ?";
    public final static String GET_WORD_ID_BY_LIB_ID = "SELECT word_id FROM word_lib WHERE lib_id = ?";
    public static final String GET_USERS_WITH_PAGINATION = "SELECT u FROM User u WHERE u.email <> 'englishwildcard@gmail.com' ORDER BY u.id";
    public static final String USER_EXISTS_ALREADY = "Користувач з таким e-mail вже існує";
    public static final String USER_ENABLE_REGISTRATION = "На ваш e-mail відправлено листа для підтвердження реєстрації";
    public static final String USER_IS_ENABLED_ALREADY = "Обліковий запис вже було активовано";
    public static final String USER_IS_ENABLED = "Ваш аккаунт активовано, дякуємо за реєстрацію!";
    public static final String ENABLE_MISTAKE = "Виникла помилка активації користувача";
    public static final String USER_UPDATE_EMAIL = "Ваш e-mail успішно змінено!";
    public static final String USER_NOT_UPDATE_EMAIL = "Ваш e-mail не був змінений";
    public static final String PASSWORD_DOESNT_FIT = "Пароль має бути від 5 до 30 символів, містити англійські літери та дозволені знаки пунктуації !\"#$%&'()*+,-./:;<=>?@[\\]^_\\`{|}~";
    public static final String EMAIL_DOESNT_FIT = "Перевірте, будь ласка, чи вірно написано e-mail адресу";
    public static final String USER_UPDATE = "Обліковий запис користувача успішно змінено";
    public static final String USER_NOT_UPDATE = "Обліковий запис користувача не було змінено";
    public static final String WORD_DOESNT_FIT = "Перевірте чи вірно написано слово англійською";
    public static final String PART_OF_SPEECH_DOESNT_FIT = "Перевірте частину мови";
    public static final String DESCRIPTION_DOESNT_FIT = "Перевірте правильність написаного у полі Description";
    public static final String EXAMPLE_DOESNT_FIT = "Перевірте правильність написаного у полі Example";
    public static final String TRANSLATION_DOESNT_FIT = "Перевірте правильність написаного у полі Translation";
    public static final String WORD_UPDATE_SUCCESS = "Слово успішно оновлено";
    public static final String WORD_UPDATE_UNSUCCESS = "Слово не було оновлено";
    public static final String WORD_SAVE_SUCCESS = "Слово успішно додано";
    public static final String WORD_SAVE_UNSUCCESS = "Слово не було додано";
    public static final String LIB_NAME_DOESNT_FIT = "Перевірте правильність написаного у полі Lib name";
    public static final String LIB_SAVE_SUCCESS = "Бібліотека успішно додана";
    public static final String LIB_UPDATE_SUCCESS = "Бібліотека успішно оновлена";
    public static final String LIB_UPDATE_UNSUCCESS = "Бібліотека не була оновлена";
    public static final String WORD_ADDED_TO_FAV = "Слово додано до бібліотеки обраних";
    public static final String WORD_NOT_ADDED_TO_FAV = "Слово не було додано до бібліотеки обраних";
    public static final String WORD_IS_PRESENT_IN_FAV = "Слово вже є у бібліотеці обраних";
    public static final String WORD_DELETED_FROM_FAV = "Слово видалено з бібліотеки обраних";
    public static final String WORD_NOT_DELETED_FROM_FAV = "Слово не було видалено з бібліотеки обраних";
    public static final String WORD_IS_ABSENT_IN_FAV = "Такого слова немає у бібліотеці обраних";
    public static final String LIB_ADDED_TO_FAV = "Бібліотеку додано в обране";
    public static final String LIB_NOT_ADDED_TO_FAV = "Бібліотеку не було додано в обране";
    public static final String LIB_DELETED_FROM_FAV = "Бібліотеку видалено з обраного";
    public static final String LIB_NOT_DELETED_FROM_FAV = "Бібліотеку не було видалено з обраного";

    private Constants() {}

}
