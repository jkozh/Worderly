package com.julia.android.worderly.service;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService {

    private static final String LOG_TAG = MyFirebaseMessagingService.class.getSimpleName();

    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(LOG_TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(LOG_TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(LOG_TAG, "FCM Data Message: " + remoteMessage.getData());
    }

}
