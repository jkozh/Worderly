package com.julia.android.worderly.ui.main;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julia.android.worderly.model.User;

import java.lang.ref.WeakReference;


public class MainPresenter {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private WeakReference<MainPresenter.View> mWeakView;


    public MainPresenter(MainPresenter.View v) {
        this.mWeakView = new WeakReference<>(v);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null && mWeakView.get() != null) {
            // Not signed in, launch the Sign In activity
            mWeakView.get().navigateToSignInActivity();
        }
    }


    public void onSignOutClicked() {
        mFirebaseAuth.signOut();
        MainPresenter.View view = mWeakView.get();
        if (view != null) {
            view.navigateToSignInActivity();
        }
    }


    public void showUserInfoInDrawer() {
        MainPresenter.View view = mWeakView.get();
        if (view != null) {
            User user = view.getUserFromPrefs();
            if (user != null) {
                view.setUpDrawer(user.getUsername(), user.getPhotoUrl());
            }
        }
    }


    public interface View {

        void setUpDrawer(String username, String photoUrl);

        void navigateToSignInActivity();

        User getUserFromPrefs();

        void signInFail(String errorMessage);

    }

}