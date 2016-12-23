package com.julia.android.worderly.models;

public class User {

    public String username;
    public String photoUrl;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String photoUrl) {
        this.username = username;
        this.photoUrl = photoUrl;
    }

}
