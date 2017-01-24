package com.julia.android.worderly.data.database;


import android.provider.BaseColumns;

public class GameContract {

    /* GameEntry is an inner class that defines the contents of the game table */
    public static final class GameEntry implements BaseColumns {

        // Task table and column names
        public static final String TABLE_NAME = "games";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_SCORE = "score";

        /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows
        Note: Because this implements BaseColumns, the _id column is generated automatically
        games
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
        |  43   |  letters  |      2     |
         - - - - - - - - - - - - - - - - -
         */
    }
}
