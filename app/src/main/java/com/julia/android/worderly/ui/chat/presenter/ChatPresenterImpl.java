package com.julia.android.worderly.ui.chat.presenter;

import com.julia.android.worderly.model.Message;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.chat.interactor.ChatInteractor;
import com.julia.android.worderly.ui.chat.interactor.ChatInteractorImpl;
import com.julia.android.worderly.ui.chat.view.ChatView;

public class ChatPresenterImpl implements ChatPresenter {

    private ChatView mChatView;
    private String mChatRoomChild;
    private String mChatRoomInvertChild;
    private User mCurrentUser;
    private ChatInteractor mInteractor;
    private String mOpponentId;
    private String mOpponentUsername;

    public ChatPresenterImpl(ChatView chatView) {
        this.mChatView = chatView;
        this.mInteractor = new ChatInteractorImpl(this);
    }

    @Override
    public String getCurrentUserUsername() {
        return mCurrentUser.getUsername();
    }

    @Override
    public void setUserFromJson(User user) {
        this.mCurrentUser = user;
    }

    @Override
    public String getChatRoomChild() {
        return mChatRoomChild;
    }

    @Override
    public void setChatRoomChild() {
        this.mChatRoomChild = mCurrentUser.getId() + "_" + mOpponentId;
    }

    @Override
    public void setChatRoomInvertChild() {
        this.mChatRoomInvertChild = mOpponentId + "_" + mCurrentUser.getId();
    }

    @Override
    public void setOpponentInfo(String opponentId, String opponentUsername) {
        this.mOpponentId = opponentId;
        this.mOpponentUsername = opponentUsername;
        setChatRoomChild();
        setChatRoomInvertChild();
        mChatView.setToolbarTitle(opponentUsername);
    }

    @Override
    public void onSendButtonClick(String message) {
        Message msg = new Message(message, mCurrentUser.getUsername(), mCurrentUser.getPhotoUrl());
        mInteractor.sendMessage(msg, mChatRoomChild, mChatRoomInvertChild);
    }
}
