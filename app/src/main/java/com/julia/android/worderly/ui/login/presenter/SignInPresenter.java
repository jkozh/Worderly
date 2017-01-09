package com.julia.android.worderly.ui.login.presenter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface SignInPresenter {
    void onStart();
    void onStop();
    void firebaseAuthWithGoogle(GoogleSignInAccount account);
}
