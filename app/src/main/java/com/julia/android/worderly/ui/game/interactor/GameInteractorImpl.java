package com.julia.android.worderly.ui.game.interactor;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.model.Move;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.utils.FirebaseConstants;

import static com.julia.android.worderly.utils.FirebaseConstants.FIREBASE_RESIGN_CHILD;
import static com.julia.android.worderly.utils.FirebaseConstants.FIREBASE_WIN_CHILD;

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
                .child(FIREBASE_WIN_CHILD).push().setValue(true);
    }

    @Override
    public void listenOpponentUserWin(String currentUserId, String opponentUserId) {
        mGamesChildRef.child(opponentUserId + "_" + currentUserId)
                .child(FIREBASE_WIN_CHILD).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        mPresenter.showLoseDialog();
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
    public void deleteGameRoom(String currentUserId, String opponentUserId) {
        mGamesChildRef.child(currentUserId + "_" + opponentUserId).removeValue();
    }

    @Override
    public void listenOpponentUserResign(String currentUserId, String opponentUserId) {
        mGamesChildRef.child(opponentUserId + "_" + currentUserId)
                .child(FIREBASE_RESIGN_CHILD).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        mPresenter.showWinDialog();
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
    public void notifyOpponentAboutResign(String currentUserId, String opponentUserId) {
        mGamesChildRef.child(currentUserId + "_" + opponentUserId)
                .child(FIREBASE_RESIGN_CHILD).push().setValue(true);
    }

    @Override
    public void notifyOpponentAboutWordAndScore(String currentUserId, String opponentUserId, Move move) {
        mGamesChildRef.child(currentUserId + "_" + opponentUserId)
                .child("moves").push().setValue(move);
    }
}
