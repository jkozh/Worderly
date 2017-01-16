package com.julia.android.worderly.ui.chat.interactor;

import com.julia.android.worderly.model.Message;

public interface ChatInteractor {
    void sendMessage(Message msg, String mChatRoomChild, String mChatRoomInvertChild);
}
