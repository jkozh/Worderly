package com.julia.android.worderly.ui.search.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.search.presenter.SearchOpponentPresenter;
import com.julia.android.worderly.utils.FirebaseConstants;

import java.util.Objects;

public class SearchOpponentInteractorImpl implements SearchOpponentInteractor {

    private static final String TAG = SearchOpponentInteractorImpl.class.getSimpleName();
    private final SearchOpponentPresenter mPresenter;
    private DatabaseReference mDatabase;
    private boolean mOpponentFound;

    public SearchOpponentInteractorImpl(SearchOpponentPresenter presenter) {
        Log.d(TAG, "SearchOpponentInteractorImpl constructor");
        this.mPresenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mOpponentFound = false;
    }

    @Override
    public void addUser(User user) {
        mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE_CHILD)
                .child(user.getId()).setValue(user);
    }

    @Override
    public void removeUser(String uid) {
        mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE_CHILD)
                .child(uid).removeValue();
    }

    @Override
    public void searchForOpponent(final User user) {
        Log.d(TAG, "searchForOpponent");
        mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE_CHILD)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String opponentUid = dataSnapshot.getKey();
                        Log.d(TAG, "mOpponentFound=" + mOpponentFound);
                        if (!mOpponentFound && !Objects.equals(user.getId(), opponentUid)) {
                            mOpponentFound = true;
                            User opponentUser = dataSnapshot.getValue(User.class);
                            Log.d(TAG, "Random Opponent found: " + opponentUser.getUsername());
                            mPresenter.sendOpponentUser(opponentUser);
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

    @Override
    public void createCurrentGameRoom(String currentUserId, String opponentUserId) {
        final String gameRoomCurrentChild = currentUserId + "_" + opponentUserId;
        mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD)
                .child(gameRoomCurrentChild).setValue(true);
        listenForOpponentGameRoom(currentUserId, opponentUserId);
    }

    @Override
    public void listenForOpponentGameRoom(final String currentUserId, String opponentUserId) {
        final String gameRoomOpponentChild = opponentUserId + "_" + currentUserId;

        mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD).child(gameRoomOpponentChild)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        removeUser(currentUserId);
                        mPresenter.startGame();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
