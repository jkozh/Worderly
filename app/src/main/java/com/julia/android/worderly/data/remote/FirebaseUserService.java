package com.julia.android.worderly.data.remote;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.utils.Constants;
import com.julia.android.worderly.utils.FirebaseConstants;


public class FirebaseUserService {

    private DatabaseReference mDatabase;


    public FirebaseUserService() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public User createUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setUsername(firebaseUser.getDisplayName());
        user.setEmail(firebaseUser.getEmail());
        if (firebaseUser.getPhotoUrl() != null) {
            user.setPhotoUrl(firebaseUser.getPhotoUrl().toString());
        } else {
            user.setPhotoUrl(Constants.DEFAULT_USER_PHOTO_URL);
        }

        mDatabase.child(FirebaseConstants.FIREBASE_USERS_CHILD).child(user.getId()).setValue(user);
        return user;
    }


    public DatabaseReference getUser(String userUid) {
        return mDatabase.child(FirebaseConstants.FIREBASE_USERS_CHILD).child(userUid);
    }


    public void updateUser(FirebaseUser user) {
    }


    public void deleteUser(String userUid) {
    }

}
