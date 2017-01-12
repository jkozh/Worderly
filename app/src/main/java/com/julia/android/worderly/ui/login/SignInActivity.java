package com.julia.android.worderly.ui.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.main.view.MainActivity;
import com.julia.android.worderly.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AbstractSignInActivity {

    public static final String PREF_SIGN_IN = "PREF_SIGN_IN";
    public static final String PREF_USERNAME = "PREF_USERNAME";
    public static final String PREF_USER_PHOTO_URL = "PREF_USER_PHOTO_URL";
    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final int REQUEST_SIGN_IN_GOOGLE = 9001;
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

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            mPresenter.firebaseAuthWithGoogle(account);
        } else {
            // Google Sign In failed, update UI appropriately
            Log.e(TAG, getString(R.string.error_google_sign_in_failed));
            signInFail(getString(R.string.error_google_sign_in_failed));
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
    public void navigateToMainActivity() {
        hideProgressDialog();
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
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

    @Override
    public void setSharedPreferences(User user) {
        SharedPreferences prefs = getSharedPreferences(PREF_SIGN_IN, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_USERNAME, user.getUsername());
        editor.putString(PREF_USER_PHOTO_URL, user.getPhotoUrl());
        editor.apply();
    }

    @Override
    public void showEnterNicknameDialog(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_enter_nickname));
        builder.setMessage(getString(R.string.msg_by_default, username));
        final EditText nicknameEditText = new EditText(this);
        nicknameEditText.setFilters(new InputFilter[] {
                // Maximum 20 characters
                new InputFilter.LengthFilter(Constants.MAX_USERNAME_INPUT_DIALOG),
        });
        builder.setView(nicknameEditText);

        builder.setPositiveButton(getString(R.string.action_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.setUsernameFromDialog(nicknameEditText.getText().toString());
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}
