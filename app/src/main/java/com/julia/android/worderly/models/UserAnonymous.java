package com.julia.android.worderly.models;

public class UserAnonymous {

    public String username;

    public UserAnonymous() {
        // Default constructor required for calls to DataSnapshot.getValue(UserAnonymous.class)
    }

    public UserAnonymous(String username) {
        this.username = username;
    }

}
