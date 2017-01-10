package com.julia.android.worderly.ui.login;

import com.julia.android.worderly.model.User;

interface SignInView {
    void signInFail(String errorMessage);
    void navigateToMainActivity();
    void showProgressDialog();
    void hideProgressDialog();
    void setSharedPreferences(User user);
    void showEnterNicknameDialog(String username);
}
