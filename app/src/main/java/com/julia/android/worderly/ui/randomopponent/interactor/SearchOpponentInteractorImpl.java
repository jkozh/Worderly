package com.julia.android.worderly.ui.randomopponent.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.randomopponent.presenter.SearchOpponentPresenter;
import com.julia.android.worderly.utils.FirebaseConstants;

import java.util.Objects;

public class SearchOpponentInteractorImpl implements SearchOpponentInteractor {

    private final SearchOpponentPresenter mPresenter;
    private DatabaseReference mDatabase;

    public SearchOpponentInteractorImpl(SearchOpponentPresenter presenter) {
        this.mPresenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void addUser(User user) {
        mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE)
                .child(user.getId()).setValue(user);
    }

    @Override
    public void removeUser(User user) {
        mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE)
                .child(user.getId()).removeValue();
    }

    @Override
    public void searchForOpponent(final User user) {
        mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String opponentUid = dataSnapshot.getKey();
                        if (!Objects.equals(user.getId(), opponentUid)) {
                            User opponentUser = dataSnapshot.getValue(User.class);
                            Log.d("Random Opponent found:", opponentUser.getUsername());
                            mPresenter.sendOpponentUser(opponentUser);
                            mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE).removeEventListener(this);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
