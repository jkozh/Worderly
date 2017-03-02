package com.julia.android.worderly;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * A module for Android-specific dependencies which require a Context to create.
 */
@Module
final class AppModule {

    private final App mApp;


    AppModule(App app) {
        this.mApp = app;
    }


    @Provides
    @Singleton
    Application provideApplication() {
        return mApp;
    }

}
