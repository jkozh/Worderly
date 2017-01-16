package com.julia.android.worderly.ui.game.interactor;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.utils.FirebaseConstants;

public class GameInteractorImpl implements GameInteractor {

    private static final String TAG = GameInteractorImpl.class.getSimpleName();
    private final GamePresenter mPresenter;
    private DatabaseReference mDatabase;

    public GameInteractorImpl(GamePresenter presenter) {
        this.mPresenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void removeUserFromOnlineUsers(String id) {
        Log.d(TAG, "removeUserFromOnlineUsers, id=" + id);
        mDatabase.child(FirebaseConstants.FIREBASE_USERS_ONLINE).child(id).removeValue();
    }
}
