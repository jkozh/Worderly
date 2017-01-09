package com.julia.android.worderly.ui.login.presenter;

import android.content.Intent;

public interface SignInPresenter {
    void onStart();
    void onResume();
    void onStop();
    void onDestroy();
    Intent signInWithGoogle();
    void signInAnonymously();
}
