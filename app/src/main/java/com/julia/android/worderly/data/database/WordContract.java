package com.julia.android.worderly.data.database;

import android.net.Uri;
import android.provider.BaseColumns;


class WordContract {

    // The authority, which is how your code knows which Content Provider to access
    static final String AUTHORITY = "com.julia.android.worderly";
    // Define the possible paths for accessing data in this contract
    // This is the path for the "words" directory
    static final String PATH_WORDS = "words";
    // The base content URI = "content://" + <authority>
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /* WordEntry is an inner class that defines the contents of the word table */
    static final class WordEntry implements BaseColumns {

        // WordEntry content URI = base content URI + path
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORDS).build();
        // Since WordEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        static final String COLUMN_WORD = "word";
        static final String COLUMN_SCRAMBLED_WORD = "scrambled";
        static final String COLUMN_DEFINITION = "definition";

        // Word table and column names
        static final String TABLE_NAME = "words";

        /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows
        Note: Because this implements BaseColumns, the _id column is generated automatically
        words
         - - - - - - - - - - - - - - - - - - - - - - - - - -
        | _id  |    word    |   scrambled   |  definition  |
         - - - - - - - - - - - - - - - - - - - - - - - - - -
        |  1   |  passion   |    sasonip    |  some def... |
         - - - - - - - - - - - - - - - - - - - - - - - - - -
        |  2   |  ruining   |    uniigrn    |  some def... |
         - - - - - - - - - - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - - - - - - - - - - -
        |  43  |  letters   |    etreslt    |  some def... |
         - - - - - - - - - - - - - - - - - - - - - - - - - -
         */
    }
}
