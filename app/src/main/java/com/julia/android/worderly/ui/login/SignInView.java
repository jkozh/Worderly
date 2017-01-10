package com.julia.android.worderly.ui.login;

import com.google.firebase.auth.FirebaseUser;
import com.julia.android.worderly.model.User;

public interface SignInView {
    void signInFail(String errorMessage);
    void navigateToMainActivity();
    void showProgressDialog();
    void hideProgressDialog();
    void setSharedPreferences(FirebaseUser user);
    void showEnterNicknameDialog(User user);
}
