package com.julia.android.worderly.utils;


public class Constants {
    // Links
    public static final String DEFAULT_USER_PHOTO_URL =
            "https://cdn4.iconfinder.com/data/icons/standard-free-icons/139/Profile01-128.png";

    public static final String GUEST = "Guest";

    // Length Limit
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 256;
    public static final String DEFAULT_EMAIL_VALUE = "0";

    // Shared Preferences SignInActivity -> MainActivity
    public static final String PREF_NAME = "com.julia.android.worderly";
    public static final String PREF_USER = "PREF_USER";
    public static final String PREF_USER_DEFAULT_VALUE = "missing_value";

    // Extra from SearchOpponentActivity -> GameActivity
    public static final String EXTRA_OPPONENT_ID = "EXTRA_OPPONENT_ID";
    public static final String EXTRA_OPPONENT_PHOTO_URL = "EXTRA_OPPONENT_PHOTO_URL";
    public static final String EXTRA_OPPONENT_USERNAME = "EXTRA_OPPONENT_USERNAME";
    public static final String EXTRA_OPPONENT_EMAIL = "EXTRA_OPPONENT_EMAIL";

    // Words Api Constants
    public static final String MASHAPE_KEY_PARAM = "X-Mashape-Key";
    public static final String ACCEPT_PARAM = "Accept";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String MESSAGE = "message";

    public static final String RANDOM = "true";
    public static final int NUMBER_OF_LETTERS = 7;
    public static final String PART_OF_SPEECH = "noun";
    public static final String HAS_DEFINITION = "hasDefinition";
    //String lettersPattern = "^[^\\d\\s]+$\"";

    // Construct the URL for the WordsApi query
    // Possible parameters are available at WordsApi page, at
    // https://www.wordsapi.com/docs#search
    public static final String WORDS_API_BASE_URL = "https://wordsapiv1.p.mashape.com/words";
    public static final String RANDOM_PARAM = "random";
    public static final String LETTERS_PARAM = "letters";
    public static final String PART_OF_SPEECH_PARAM = "partOfSpeech";
    public static final String HAS_DETAILS_PARAM = "hasDetails";
    //final String LETTERS_PATTERN_PARAM = "lettersPattern";

}
