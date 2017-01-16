package com.julia.android.worderly.ui.chat.presenter;

import com.julia.android.worderly.model.User;

public interface ChatPresenter {
    void setUserFromJson(User user);

    String getChatRoomChild();

    void setChatRoomChild(String opponentId);

    void onSendButtonClick(String message);
}
