package com.julia.android.worderly.ui.chat.interactor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.model.Message;
import com.julia.android.worderly.ui.chat.presenter.ChatPresenter;
import com.julia.android.worderly.utils.FirebaseConstants;

public class ChatInteractorImpl implements ChatInteractor {

    private ChatPresenter mPresenter;
    private DatabaseReference mDatabase;

    public ChatInteractorImpl(ChatPresenter presenter) {
        this.mPresenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void sendMessage(Message msg, String mChatRoomChild, String mChatRoomInvertChild) {
        mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD)
                .child(mChatRoomChild)
                .child(FirebaseConstants.FIREBASE_MESSAGES_CHILD)
                .push().setValue(msg);

        mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD)
                .child(mChatRoomInvertChild)
                .child(FirebaseConstants.FIREBASE_MESSAGES_CHILD)
                .push().setValue(msg);
    }
}
