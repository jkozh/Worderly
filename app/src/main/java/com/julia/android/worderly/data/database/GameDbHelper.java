package com.julia.android.worderly.data.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "gamesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    // Constructor
    GameDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the games database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create games table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + GameContract.GameEntry.TABLE_NAME + " (" +
                GameContract.GameEntry._ID          + " INTEGER PRIMARY KEY, " +
                GameContract.GameEntry.COLUMN_WORD  + " TEXT NOT NULL, " +
                GameContract.GameEntry.COLUMN_SCORE + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GameContract.GameEntry.TABLE_NAME);
        onCreate(db);
    }
}
