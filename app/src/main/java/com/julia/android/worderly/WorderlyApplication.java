package com.julia.android.worderly;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.julia.android.worderly.volley.WordRequest;

public class WorderlyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        new WordRequest(requestQueue);
    }

}
