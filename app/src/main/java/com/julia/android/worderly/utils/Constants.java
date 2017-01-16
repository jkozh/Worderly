package com.julia.android.worderly.utils;


public class Constants {
    // Links
    public static final String DEFAULT_USER_PHOTO_URL =
            "https://cdn4.iconfinder.com/data/icons/standard-free-icons/139/Profile01-128.png";

    public static final String GUEST = "Guest";

    // Length Limit
    public static final int MAX_USERNAME_INPUT_DIALOG = 20;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 256;

    // Shared Preferences SignInActivity -> MainActivity
    public static final String PREF_NAME = "com.julia.android.worderly";
    public static final String PREF_USER = "PREF_USER";
    public static final String PREF_USER_DEFAULT_VALUE = "missing_value";

    // Extra from SearchOpponentActivity -> GameActivity
    public static final String EXTRA_OPPONENT_ID = "EXTRA_OPPONENT_ID";
    public static final String EXTRA_OPPONENT_PHOTO_URL = "EXTRA_OPPONENT_PHOTO_URL";
    public static final String EXTRA_OPPONENT_USERNAME = "EXTRA_OPPONENT_USERNAME";
    public static final String EXTRA_OPPONENT_EMAIL = "EXTRA_OPPONENT_EMAIL";
}
