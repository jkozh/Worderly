package com.julia.android.worderly.data.remote;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.model.User;

public class UserService {
    private DatabaseReference databaseRef;

    public UserService() {
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public void createUser(User user) {
        //databaseRef.child("users").child(user.getUid()).setValue(user);
    }

    public DatabaseReference getUser(String userUid) {
        //return databaseRef.child("users").child(userUid);
    }

    public void updateUser(User user) {

    }

    public void deleteUser(String key) {

    }
}
