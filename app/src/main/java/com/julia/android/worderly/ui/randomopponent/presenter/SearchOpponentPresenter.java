package com.julia.android.worderly.ui.randomopponent.presenter;


import com.julia.android.worderly.model.User;

public interface SearchOpponentPresenter {
    void onStart();
    void onDestroy();
    void addUserToOnlineUsers(User user);
    void removeUserFromOnlineUsers(User user);
    void searchForOpponent(User user);
    void sendOpponentUser(User opponentUser);
}
