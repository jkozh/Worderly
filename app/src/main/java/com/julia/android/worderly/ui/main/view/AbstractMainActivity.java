package com.julia.android.worderly.ui.main.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public abstract class AbstractMainActivity extends AppCompatActivity implements MainView,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = AbstractMainActivity.class.getSimpleName();
    protected GoogleApiClient mGoogleApiClient;

    protected void setGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    protected void onSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        signInFail("Google Play Services error.");
    }
}
