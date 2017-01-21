package com.julia.android.worderly.ui.main.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.main.view.MainView;

public class MainPresenterImpl implements MainPresenter {

    private static final String TAG = MainPresenterImpl.class.getSimpleName();

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
    public void onDestroy() {
        mMainView = null;
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
        mMainView.setUpDrawer(mUser.getUsername(), mUser.getPhotoUrl());
    }

    @Override
    public void setUserFromJson(User user) {
        mUser = user;
    }

    public MainView getMainView() {
        return mMainView;
    }
}
