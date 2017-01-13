package com.julia.android.worderly.ui.randomopponent.presenter;


import com.julia.android.worderly.model.User;

public interface RandomOpponentPresenter {
    void onStart();
    void onDestroy();
    void addUserToOnlineUsers(User user);
}
