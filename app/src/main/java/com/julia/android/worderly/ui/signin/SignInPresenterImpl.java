package com.julia.android.worderly.ui.signin;

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
import com.julia.android.worderly.R;
import com.julia.android.worderly.data.remote.FirebaseUserService;
import com.julia.android.worderly.model.User;

class SignInPresenterImpl implements SignInPresenter {

    private static final String TAG = SignInPresenterImpl.class.getSimpleName();
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SignInView mSignInView;

    SignInPresenterImpl(SignInView signInView) {
        this.mSignInView = signInView;

        mAuth = FirebaseAuth.getInstance();
        setAuthStateListener();
    }

    private void setAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mFirebaseUser.getUid());
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
    public void onDestroy() {
        mSignInView = null;
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        if (mSignInView != null) {
            mSignInView.showProgressDialog();
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener((SignInActivity) mSignInView,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        handleFirebaseAuthResult(task);
                    }
                });
    }

    /**
     * If sign in fails, display a message to the user. If sign in succeeds
     * the auth state listener will be notified and logic to handle the
     * signed in user can be handled in the listener.
     */
    private void handleFirebaseAuthResult(Task<AuthResult> task) {
        if (!task.isSuccessful()) {
            Log.w(TAG, "signIn", task.getException());
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
                                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                                handleFirebaseAuthResult(task);
                            }
                        });
    }


//    @Override
//    public void setUsernameFromDialog(String username) {
//        if (!Objects.equals(username, "")) {
//            mUser.setUsername(username);
//        }
//        if (mSignInView != null) {
//            //mSignInView.setSharedPreferences(mUser);
//            mSignInView.navigateToMainActivity(mUser);
//        }
//    }

    public SignInView getSignInView() {
        return mSignInView;
    }
}
