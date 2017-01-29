package com.julia.android.worderly.ui.game.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.utils.FirebaseConstants;

public class GameInteractorImpl implements GameInteractor {

    private static final String TAG = GameInteractorImpl.class.getSimpleName();
    private final GamePresenter mPresenter;
    private DatabaseReference mDatabase;
    private DatabaseReference mGamesChildRef;

    public GameInteractorImpl(GamePresenter presenter) {
        this.mPresenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGamesChildRef = mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD);
    }

    @Override
    public void notifyOpponentUserWin(String currentUserId, String opponentUserId) {
        mGamesChildRef.child(currentUserId + "_" + opponentUserId)
                .child("win").push().setValue(true);
    }

    @Override
    public void listenOpponentUserWin(String currentUserId, String opponentUserId) {
        mGamesChildRef.child(opponentUserId + "_" + currentUserId)
                .child("win").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "Opponent WIN!!! data snapshot:" + dataSnapshot);
                        mPresenter.showLossDialog();
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
