package com.julia.android.worderly.data.remote;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.utils.FirebaseConstants;

public class FirebaseUserService {
    private DatabaseReference mDatabase;

    public FirebaseUserService() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void createUser(FirebaseUser firebaseUser) {
        String userUid = firebaseUser.getUid();
        String username = firebaseUser.getDisplayName();
        String userPhotoUrl;
        if (firebaseUser.getPhotoUrl() != null) {
            userPhotoUrl = firebaseUser.getPhotoUrl().toString();
        } else {
            userPhotoUrl = "none";
        }
        String userEmail = firebaseUser.getEmail();

        User user = new User(username, userPhotoUrl, userEmail);

        mDatabase.child(FirebaseConstants.FIREBASE_USERS_CHILD).child(userUid).setValue(user);
    }

    public DatabaseReference getUser(String userUid) {
        return mDatabase.child(FirebaseConstants.FIREBASE_USERS_CHILD).child(userUid);
    }

    public void updateUser(FirebaseUser user) {
    }

    public void deleteUser(String userUid) {
    }
}
