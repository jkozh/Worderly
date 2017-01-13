package com.julia.android.worderly.ui.randomopponent.interactor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.utils.FirebaseConstants;

public class RandomOpponentInteractorImpl implements RandomOpponentInteractor {

    private DatabaseReference mDatabase;

    public RandomOpponentInteractorImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void addUser(User user) {
        mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE)
                .child(user.getId()).setValue(user);
    }
}
