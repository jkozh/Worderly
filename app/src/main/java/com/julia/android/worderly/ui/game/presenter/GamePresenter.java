package com.julia.android.worderly.ui.game.presenter;

import com.julia.android.worderly.model.User;

public interface GamePresenter {
    void onStart();
    void onDestroy();
    void setUserFromJson(User user);
    void setUserFromBundle(String id, String username, String email, String photoUrl);
    void onChatButtonClick();
    void setWord(String word);
    void setWordView(String word);
    void onSendWordButtonClick(String word);
    void changeChatIcon();
}
