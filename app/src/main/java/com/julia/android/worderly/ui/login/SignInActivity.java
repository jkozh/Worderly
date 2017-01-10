package com.julia.android.worderly.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseUser;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.main.view.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AbstractSignInActivity implements SignInView {

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
            Log.e(TAG, "Google Sign In failed.");
            signInFail("Google Sign In failed.");
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
            mProgressDialog.setMessage(getString(R.string.loading));
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
    public void setSharedPreferences(FirebaseUser user) {

    }

    @Override
    public void showEnterNicknameDialog(final User user) {
        //.setTitle("Insert your nickname");
        //.setMessage("Or leave it as Guest23232");
        //final EditText nicknameEditText = new EditText(this);
        //.setView(nicknameEditText);
        //.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        //    public void onClick(DialogInterface dialog, int whichButton) {
        //        mPresenter.createUser(user, nicknameEditText.getText().toString());
        //    }
        //});
        //.show();
    }
}
