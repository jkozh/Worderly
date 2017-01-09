package com.julia.android.worderly.ui.login.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.julia.android.worderly.data.remote.FirebaseUserService;
import com.julia.android.worderly.ui.login.view.SignInActivity;
import com.julia.android.worderly.ui.login.view.SignInView;

public class SignInPresenterImpl implements SignInPresenter {

    private static final String TAG = SignInPresenterImpl.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SignInView mSignInView;
    private SignInActivity mSignInActivity;

    public SignInPresenterImpl(SignInView signInView, SignInActivity signInActivity) {
        this.mSignInView = signInView;
        this.mSignInActivity = signInActivity;

        mAuth = FirebaseAuth.getInstance();
        setAuthStateListener();
    }

    private void setAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
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
    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        if (mSignInView != null) {
            mSignInView.showProgressDialog();
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mSignInActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            if (mSignInView != null) {
                                mSignInView.signInFail("Authentication failed.");
                            }
                        } else {
                            FirebaseUser user = task.getResult().getUser();
                            new FirebaseUserService().createUser(user);
                            if (mSignInView != null) {
                                mSignInView.setSharedPreferences(user);
                                mSignInView.navigateToMainActivity();
                            }
                        }
                        if (mSignInView != null) {
                            mSignInView.hideProgressDialog();
                        }
                    }
                });
    }

    public SignInView getSignInView() {
        return mSignInView;
    }
}
