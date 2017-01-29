package com.julia.android.worderly.ui.signin;

import com.julia.android.worderly.model.User;

interface SignInView {
    void signInFail(String errorMessage);
    void setSharedPrefs(User user);
    void navigateToMainActivity();
    void showProgressDialog();
    void hideProgressDialog();
}
