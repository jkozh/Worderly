package com.julia.android.worderly.ui.main;

import com.julia.android.worderly.model.User;


public interface MainPresenter {

    void onSignOutClicked();

    void showUserInfoInDrawer();

    void setUserFromJson(User user);

}
