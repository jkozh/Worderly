package com.julia.android.worderly.ui.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


interface SignInPresenter {

    void onStart();

    void onStop();

    void onDestroy();

    void firebaseAuthWithGoogle(GoogleSignInAccount account);

    void firebaseAuthAnonymous();

}
