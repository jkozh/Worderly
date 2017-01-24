package com.julia.android.worderly.data.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class WordContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.julia.android.worderly";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "words" directory
    public static final String PATH_WORDS = "words";

    /* WordEntry is an inner class that defines the contents of the word table */
    public static final class WordEntry implements BaseColumns {

        // WordEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORDS).build();


        // Word table and column names
        public static final String TABLE_NAME = "words";

        // Since WordEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_SCORE = "score";

        /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows
        Note: Because this implements BaseColumns, the _id column is generated automatically
        words
         - - - - - - - - - - - - - - - - -
        | _id  |    word    |    score   |
         - - - - - - - - - - - - - - - - -
        |  1   |  passion   |      7     |
         - - - - - - - - - - - - - - - - -
        |  2   |  ruining   |      8     |
         - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - -
        |  43  |  letters   |      2     |
         - - - - - - - - - - - - - - - - -
         */
    }
}
