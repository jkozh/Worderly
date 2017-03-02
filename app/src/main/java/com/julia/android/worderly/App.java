package com.julia.android.worderly;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import timber.log.Timber;


public class App extends Application {

    private WorderlyComponent mComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = WorderlyComponent.Initializer.init(this);
        mComponent.inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Stetho.initializeWithDefaults(this);
    }


    public WorderlyComponent component() {
        return mComponent;
    }


    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

}
