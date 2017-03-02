package com.julia.android.worderly.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.julia.android.worderly.utils.Constants;


public class User implements Parcelable {

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


    protected User(Parcel in) {
        mId = in.readString();
        mUsername = in.readString();
        mEmail = in.readString();
        mPhotoUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
            this.mEmail = Constants.DEFAULT_EMAIL_VALUE;
        }
    }


    public String getPhotoUrl() {
        return mPhotoUrl;
    }


    public void setPhotoUrl(String photoUrl) {
        if (photoUrl != null) {
            this.mPhotoUrl = photoUrl;
        } else {
            this.mPhotoUrl = Constants.DEFAULT_USER_PHOTO_URL;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mUsername);
        dest.writeString(mEmail);
        dest.writeString(mPhotoUrl);
    }
}
