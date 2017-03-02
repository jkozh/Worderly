package com.julia.android.worderly;

import android.content.SharedPreferences;

import com.julia.android.worderly.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module(includes = DataModule.class)
public class WorderlyModule {


    @Provides
    @Singleton
    StringPreference provideMyValue(SharedPreferences prefs) {
        return new StringPreference(prefs, Constants.PREF_USER, Constants.PREF_USER_DEFAULT_VALUE);
    }

}
