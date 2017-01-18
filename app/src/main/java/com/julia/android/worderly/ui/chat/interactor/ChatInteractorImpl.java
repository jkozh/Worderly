package com.julia.android.worderly.ui.chat.interactor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.model.Message;
import com.julia.android.worderly.ui.chat.presenter.ChatPresenter;
import com.julia.android.worderly.utils.FirebaseConstants;

public class ChatInteractorImpl implements ChatInteractor {

    private ChatPresenter mPresenter;
    private DatabaseReference mDatabase;
    private DatabaseReference mGamesChildRef;

    public ChatInteractorImpl(ChatPresenter presenter) {
        this.mPresenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGamesChildRef = mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD);
    }

    @Override
    public void sendMessage(Message msg, String mChatRoomChild, String mChatRoomInvertChild) {
        mGamesChildRef.child(mChatRoomChild)
                .child(FirebaseConstants.FIREBASE_MESSAGES_CHILD)
                .push().setValue(msg);

        mGamesChildRef.child(mChatRoomInvertChild)
                .child(FirebaseConstants.FIREBASE_MESSAGES_CHILD)
                .push().setValue(msg);
    }
}
