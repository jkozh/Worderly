package com.julia.android.worderly;

import android.app.Application;

public class WorderlyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        Stetho.initializeWithDefaults(this);
//        new OkHttpClient.Builder()
//                .addNetworkInterceptor(new StethoInterceptor())
//                .build();
    }

}
