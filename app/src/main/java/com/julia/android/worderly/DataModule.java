package com.julia.android.worderly;

import android.app.Application;
import android.content.SharedPreferences;

import com.julia.android.worderly.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;


@Module
public final class DataModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        // We are using Application as a Context here to prevent memory leak problems
        return app.getSharedPreferences(Constants.PREF_USER, MODE_PRIVATE);
    }

}
