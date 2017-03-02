package com.julia.android.worderly.ui.chat.presenter;

import com.julia.android.worderly.model.Message;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.chat.interactor.ChatInteractor;
import com.julia.android.worderly.ui.chat.interactor.ChatInteractorImpl;

import java.lang.ref.WeakReference;


public class ChatPresenter {

    private WeakReference<ChatPresenter.View> mWeakView;
    private ChatInteractor mInteractor;
    private User mCurrentUser;
    private User mOpponent;
    private String mChatRoomChild;
    private String mChatRoomInvertChild;


    public ChatPresenter(ChatPresenter.View v) {
        mWeakView = new WeakReference<>(v);
        mInteractor = new ChatInteractorImpl(this);
        ChatPresenter.View view = mWeakView.get();
        if (view != null) {
            mCurrentUser = view.getUserFromPrefs();
        }
    }


    public void setOpponentFromBundle(User opponent) {
        mOpponent = opponent;
        setChatRoomChilds();
    }


    public void onDetach() {
        mWeakView = null;
    }


    public String getChatRoomChild() {
        return mChatRoomChild;
    }


    private String getChatRoomChildInverted() {
        return mChatRoomInvertChild;
    }


    public void onSendButtonClick(String message) {
        Message msg = new Message(message, mCurrentUser.getUsername(), mCurrentUser.getPhotoUrl());
        mInteractor.sendMessage(msg, mChatRoomChild, mChatRoomInvertChild);
    }


    private void setChatRoomChilds() {
        mChatRoomChild = mCurrentUser.getId() + "_" + mOpponent.getId();
        mChatRoomInvertChild = mOpponent.getId() + "_" + mCurrentUser.getId();
    }


    public void addListenerForNewMessage() {
        mInteractor.onNewMessageReceived(getChatRoomChildInverted());
    }


    public void notifyChatTabTitle() {
        ChatPresenter.View view = mWeakView.get();
        if (view != null) {
            view.setChatTabTitleText();
        }
    }


    public interface View {

        void hideProgressBar();

        void setChatTabTitleText();

        User getUserFromPrefs();

    }

}
