package com.julia.android.worderly.ui.login;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

interface SignInPresenter {
    void onStart();
    void onStop();
    void firebaseAuthWithGoogle(GoogleSignInAccount account);
    void firebaseAuthAnonymous();
}
