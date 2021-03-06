package com.julia.android.worderly.ui.search;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.utils.FirebaseConstants;

import java.util.Objects;

import timber.log.Timber;

public class SearchOpponentInteractorImpl implements SearchOpponentInteractor {

    private final SearchOpponentPresenter mPresenter;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsersOnlineChildRef;
    private DatabaseReference mGamesChildRef;
    private boolean mOpponentFound;


    public SearchOpponentInteractorImpl(SearchOpponentPresenter presenter) {
        this.mPresenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsersOnlineChildRef = mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE_CHILD);
        mGamesChildRef = mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD);
        mOpponentFound = false;
    }


    @Override
    public void addUser(User user) {
        mUsersOnlineChildRef.child(user.getId()).setValue(user);
    }


    @Override
    public void removeUser(String uid) {
        mUsersOnlineChildRef.child(uid).removeValue();
    }


    @Override
    public void searchForOpponent(final User user) {
        mUsersOnlineChildRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String opponentUid = dataSnapshot.getKey();
                if (!mOpponentFound && !Objects.equals(user.getId(), opponentUid)) {
                    mOpponentFound = true;
                    User opponentUser = dataSnapshot.getValue(User.class);
                    Timber.d("Random opponent found: %s", opponentUser.getUsername());
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
        mGamesChildRef.child(gameRoomCurrentChild).setValue(true);
        listenForOpponentGameRoom(currentUserId, opponentUserId);
    }


    @Override
    public void listenForOpponentGameRoom(final String currentUserId, String opponentUserId) {
        final String gameRoomOpponentChild = opponentUserId + "_" + currentUserId;
        mGamesChildRef.child(gameRoomOpponentChild)
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
