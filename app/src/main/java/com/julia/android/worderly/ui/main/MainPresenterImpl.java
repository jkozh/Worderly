package com.julia.android.worderly.ui.main;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julia.android.worderly.model.User;

public class MainPresenterImpl implements MainPresenter {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private MainView mMainView;
    private User mUser;


    public MainPresenterImpl(MainView mainView) {
        this.mMainView = mainView;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null && mMainView != null) {
            // Not signed in, launch the Sign In activity
            mMainView.navigateToSignInActivity();
        }
    }


    @Override
    public void onSignOutClicked() {
        mFirebaseAuth.signOut();
        if (mMainView != null) {
            mMainView.navigateToSignInActivity();
        }
    }


    @Override
    public void showUserInfoInDrawer() {
        if (mMainView != null && mUser != null) {
            mMainView.setUpDrawer(mUser.getUsername(), mUser.getPhotoUrl());
        }
    }


    @Override
    public void setUserFromJson(User user) {
        mUser = user;
    }

}
