package com.julia.android.worderly.ui.main;


public interface MainView {

    void setUpDrawer(String username, String photoUrl);

    void signInFail(String errorMessage);

    void navigateToSignInActivity();

}
