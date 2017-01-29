package com.julia.android.worderly.ui.chat.interactor;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    public void sendMessage(Message msg, String chatRoomChild, String chatRoomInvertChild) {
        mGamesChildRef.child(chatRoomChild)
                .child(FirebaseConstants.FIREBASE_MESSAGES_CHILD)
                .push().setValue(msg);

        mGamesChildRef.child(chatRoomInvertChild)
                .child(FirebaseConstants.FIREBASE_MESSAGES_CHILD)
                .push().setValue(msg);
    }

    @Override
    public void onNewMessageReceived(String chatRoomInvertChild) {
        mGamesChildRef.child(chatRoomInvertChild).child(FirebaseConstants.FIREBASE_MESSAGES_CHILD)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        mPresenter.notifyChatTabTitle();
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
