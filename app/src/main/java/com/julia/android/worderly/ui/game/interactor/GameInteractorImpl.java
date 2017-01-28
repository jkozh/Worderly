package com.julia.android.worderly.ui.game.interactor;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        mGamesChildRef.child(currentUserId + "_" + opponentUserId)
                .child("win").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "YOU LOSSES!!!");
                mPresenter.showLossDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    public void addWord(String word, String currentUserId, String opponentUserId) {

//
//        mGamesChildRef.child(opponentUserId + "_" + currentUserId)
//                .child(FirebaseConstants.FIREBASE_WORD_CHILD).push().setValue(word);
//    }

    /**
     * Getting the same word for both players to be displayed.
     */
//    @Override
//    public void getWord(String currentUserId, String opponentUserId) {
//        mGamesChildRef.child(opponentUserId + "_" + currentUserId)
//                .child(FirebaseConstants.FIREBASE_WORD_CHILD)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.d(TAG, "INTERACTOR getWord" + dataSnapshot.getValue());
//                        //mPresenter.setOpponentWordView();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//    }
}
