package com.julia.android.worderly.ui.search.presenter;


import com.julia.android.worderly.model.User;

public interface SearchOpponentPresenter {
    void onStart();
    void onDestroy();
    void addUserToOnlineUsers(User user);
    void removeUserFromOnlineUsers(String uid);
    void searchForOpponent(User user);
    void sendOpponentUser(User opponentUser);
    void setUserFetchedFromJson(User user);
    void startGame();
}
