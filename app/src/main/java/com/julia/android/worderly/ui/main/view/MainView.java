package com.julia.android.worderly.ui.main.view;

public interface MainView {
    void setUpDrawer(String username, String photoUrl);
    void signInFail(String errorMessage);
    void navigateToSignInActivity();
}
