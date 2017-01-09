package com.julia.android.worderly.ui.login.presenter;


import android.content.Intent;

import com.julia.android.worderly.ui.login.view.SignInView;


public class SignInPresenterImpl implements SignInPresenter {

    private SignInView signInView;

    public SignInPresenterImpl(SignInView signInView) {
        this.signInView = signInView;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

        if (signInView != null) {
            signInView.hideProgressDialog();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public Intent signInWithGoogle() {
        return null;
    }

    @Override
    public void signInAnonymously() {

    }
}
