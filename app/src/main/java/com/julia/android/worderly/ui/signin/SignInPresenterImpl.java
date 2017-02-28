package com.julia.android.worderly.ui.signin;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.julia.android.worderly.R;
import com.julia.android.worderly.data.remote.FirebaseUserService;
import com.julia.android.worderly.model.User;

import timber.log.Timber;


class SignInPresenterImpl implements SignInPresenter {

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SignInView mSignInView;


    SignInPresenterImpl(SignInView signInView) {
        this.mSignInView = signInView;
        mAuth = FirebaseAuth.getInstance();
        setAuthStateListener();
    }


    @Override
    public void onStart() {
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (mSignInView != null) {
            mSignInView.hideProgressDialog();
        }
    }


    @Override
    public void onDestroy() {
        mSignInView = null;
    }


    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Timber.d("firebaseAuthWithGoogle:%s", account.getId());

        if (mSignInView != null) {
            mSignInView.showProgressDialog();
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener((SignInActivity) mSignInView,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Timber.d("signInWithCredential:onComplete:%s", task.isSuccessful());
                        handleFirebaseAuthResult(task);
                    }
                });
    }


    @Override
    public void firebaseAuthAnonymous() {
        if (mSignInView != null) {
            mSignInView.showProgressDialog();
        }

        mAuth.signInAnonymously()
                .addOnCompleteListener((SignInActivity) mSignInView,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Timber.d("signInAnonymously:onComplete:%s", task.isSuccessful());
                                handleFirebaseAuthResult(task);
                            }
                        });
    }


    private void setAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    // User is signed in
                    Timber.d("onAuthStateChanged:signed_in:%s", mFirebaseUser.getUid());
                } else {
                    // User is signed out
                    Timber.d("onAuthStateChanged:signed_out");
                }
            }
        };
    }


    /**
     * If sign in fails, display a message to the user. If sign in succeeds
     * the auth state listener will be notified and logic to handle the
     * signed in user can be handled in the listener.
     */
    private void handleFirebaseAuthResult(Task<AuthResult> task) {
        if (!task.isSuccessful()) {
            Timber.e("signIn %s", task.getException());
            if (mSignInView != null) {
                mSignInView.signInFail(((SignInActivity) mSignInView).getResources()
                        .getString(R.string.error_sign_in_failed));
            }
        } else {
            mFirebaseUser = task.getResult().getUser();
            User user = new FirebaseUserService().createUser(mFirebaseUser);
            if (mSignInView != null) {
                mSignInView.setSharedPrefs(user);
                mSignInView.navigateToMainActivity();
            }
        }
        if (mSignInView != null) {
            mSignInView.hideProgressDialog();
        }
    }

}
