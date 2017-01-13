package com.julia.android.worderly.model;

import android.net.Uri;

import com.julia.android.worderly.utils.Constants;

public class User {

    private String mId;
    private String mUsername;
    private String mEmail;
    private String mPhotoUrl;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String username, String email, String photoUrl) {
        this.mId = id;
        this.mUsername = username;
        this.mEmail = email;
        this.mPhotoUrl = photoUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        if (username != null) {
            this.mUsername = username;
        } else {
            this.mUsername = Constants.GUEST + System.currentTimeMillis();
        }
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        if (email != null) {
            this.mEmail = email;
        } else {
            this.mEmail = "0";
        }
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        if (photoUrl != null) {
            this.mPhotoUrl = photoUrl.toString();
        } else {
            this.mPhotoUrl = Constants.DEFAULT_USER_PHOTO_URL;
        }
    }
}
