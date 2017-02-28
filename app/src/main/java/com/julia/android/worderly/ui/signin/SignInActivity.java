package com.julia.android.worderly.ui.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.main.MainActivity;
import com.julia.android.worderly.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class SignInActivity extends AbstractSignInActivity {

    private static final int REQUEST_SIGN_IN_GOOGLE = 9001;
    @BindView(R.id.toolbar_signin_activity)
    Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    private SignInPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        // Configure Google Sign In
        setUpGoogleSignIn();
        mPresenter = new SignInPresenterImpl(this);
        setSupportActionBar(mToolbar);
    }


    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }

    @Override
    public void signInFail(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.sign_in_google_button)
    public void onSignInWithGoogleButton() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_SIGN_IN_GOOGLE);
    }


    @OnClick(R.id.sign_in_anonymous_button)
    public void onSignInAnonymousButton() {
        mPresenter.firebaseAuthAnonymous();
    }


    @Override
    public void setSharedPrefs(User user) {
        SharedPreferences mPrefs = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(Constants.PREF_USER, json);
        prefsEditor.apply();
    }


    @Override
    public void navigateToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.msg_loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }


    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            mPresenter.firebaseAuthWithGoogle(account);
        } else {
            // Google Sign In failed, update UI appropriately
            Timber.e(getString(R.string.error_sign_in_failed));
            signInFail(getString(R.string.error_sign_in_failed));
        }
    }

}
