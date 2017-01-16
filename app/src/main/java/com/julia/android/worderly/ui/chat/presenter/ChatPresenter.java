package com.julia.android.worderly.ui.chat.presenter;

import com.julia.android.worderly.model.User;

public interface ChatPresenter {
    void setUserFromJson(User user);
    String getCurrentUserUsername();
    String getChatRoomChild();
    void setChatRoomChild();
    void setChatRoomInvertChild();
    void setOpponentInfo(String opponentId, String opponentUsername);
    void onSendButtonClick(String message);
}
