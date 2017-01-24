package com.julia.android.worderly.data.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

public class GameContentProvider extends ContentProvider {

    // COMPLETED (1) Define final integer constants for the directory of games and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int WORDS = 100;
    public static final int WORD_WITH_ID = WORDS;

    // COMPLETED (3) Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // COMPLETED (2) Define a static buildUriMatcher method that associates URI's with their int match
    // Member variable for a GameDbHelper that's initialized in the onCreate() method
    private GameDbHelper mGameDbHelper;

    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the game directory and a single item by ID.
         */
        uriMatcher.addURI(GameContract.AUTHORITY, GameContract.PATH_WORDS, WORDS);
        uriMatcher.addURI(GameContract.AUTHORITY, GameContract.PATH_WORDS + "/#", WORD_WITH_ID);

        return uriMatcher;
    }

    /* onCreate() is where you should initialize anything you’ll need to setup
      your underlying data source.
      In this case, you’re working with a SQLite database, so you’ll need to
      initialize a DbHelper to gain access to it.
       */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mGameDbHelper = new GameDbHelper(context);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}