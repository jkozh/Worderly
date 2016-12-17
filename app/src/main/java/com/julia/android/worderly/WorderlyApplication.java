package com.julia.android.worderly;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class WorderlyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //new WordRequest(requestQueue);
    }

}
