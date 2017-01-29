package com.julia.android.worderly.ui.main.presenter;

import com.julia.android.worderly.model.User;

public interface MainPresenter {
    void onSignOutClicked();
    void showUserInfoInDrawer();
    void setUserFromJson(User user);
}
