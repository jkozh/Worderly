package com.julia.android.worderly.model;

import android.net.Uri;

import com.julia.android.worderly.utils.Constants;

public class User {

    private String username;
    private String photoUrl;
    private String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String photoUrl, String email) {
        this.username = username;
        this.photoUrl = photoUrl;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null) {
            this.username = username;
        } else {
            this.username = "Guest";
        }
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        if (photoUrl != null) {
            this.photoUrl = photoUrl.toString();
        } else {
            this.photoUrl = Constants.DEFAULT_USER_PHOTO_URL;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email;
        } else {
            this.email = "0";
        }
    }
}
