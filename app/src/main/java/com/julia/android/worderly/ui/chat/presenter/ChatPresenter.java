package com.julia.android.worderly.ui.chat.presenter;

import android.support.annotation.Nullable;

import com.julia.android.worderly.model.Message;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.chat.interactor.ChatInteractor;
import com.julia.android.worderly.ui.chat.interactor.ChatInteractorImpl;

import java.lang.ref.WeakReference;

public class ChatPresenter {

    private WeakReference<ChatPresenter.View> mWeakView;
    private String mChatRoomChild;
    private String mChatRoomInvertChild;
    private User mCurrentUser;
    private ChatInteractor mInteractor;
    private String mOpponentId;
    private String mOpponentUsername;

    public ChatPresenter(ChatPresenter.View view) {
        mWeakView = new WeakReference(view);
        mInteractor = new ChatInteractorImpl(this);
    }

    public void setUserFromJson(User user) {
        this.mCurrentUser = user;
    }

    @Nullable
    private ChatPresenter.View getView() {
        if (mWeakView == null) {
            return null;
        }
        return mWeakView.get();
    }



//    @Override
//    public void viewDetached(boolean changingConfigurations) {
//        mWeakView = null;
//    }

    public String getCurrentUserUsername() {
        return mCurrentUser.getUsername();
    }

    public String getChatRoomChild() {
        return mChatRoomChild;
    }

    public void setChatRoomChild() {
        this.mChatRoomChild = mCurrentUser.getId() + "_" + mOpponentId;
    }

    public void setChatRoomInvertChild() {
        this.mChatRoomInvertChild = mOpponentId + "_" + mCurrentUser.getId();
    }

    public void setOpponentInfo(String opponentId, String opponentUsername) {
        this.mOpponentId = opponentId;
        this.mOpponentUsername = opponentUsername;
        setChatRoomChild();
        setChatRoomInvertChild();
    }

    public void onSendButtonClick(String message) {
        Message msg = new Message(message, "ok", "ok1");//mCurrentUser.getUsername(), mCurrentUser.getPhotoUrl());
        mInteractor.sendMessage(msg, "ok", "okok");//mChatRoomChild, mChatRoomInvertChild);
    }

//    public void loadXXX(String param1, String param2) {
//        ChatPresenter.View view = mWeakView.get();
//        if (view != null) {
//            //view.showAsLoading();
//            // Do stuff, e.g. make the Api call and finally call view.showUserDetails(userDetails);
//        }
//    }

    public interface View {
//    void setUserFromJson(User user);
//    String getCurrentUserUsername();
//    String getChatRoomChild();
//    void setChatRoomChild();
//    void setChatRoomInvertChild();
//    void setOpponentInfo(String opponentId, String opponentUsername);
//    void onSendButtonClick(String message);
    }
}
