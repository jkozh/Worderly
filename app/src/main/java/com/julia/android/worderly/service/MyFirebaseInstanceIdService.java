package com.julia.android.worderly.service;

public class MyFirebaseInstanceIdService {

    private static final String LOG_TAG = MyFirebaseInstanceIdService.class.getSimpleName();
    private static final String FRIENDLY_ENGAGE_TOPIC = "friendly_engage";

    /**
     * The Application's current Instance ID token is no longer valid and thus a new one must be requested.
     */
    public void onTokenRefresh() {
    }

}
