package com.julia.android.worderly.ui.main.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.julia.android.worderly.R;
import com.julia.android.worderly.ui.login.SignInActivity;
import com.julia.android.worderly.ui.main.presenter.MainPresenter;
import com.julia.android.worderly.ui.main.presenter.MainPresenterImpl;
import com.julia.android.worderly.ui.randomopponent.view.RandomOpponentActivity;
import com.julia.android.worderly.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AbstractMainActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.button_random_play)
    Button mRandomPlayButton;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar_main_activity)
    Toolbar mToolbar;

    private MainPresenter mPresenter;
    private String mUsername;
    private String mUserPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new MainPresenterImpl(this);
        setGoogleApiClient();

        SharedPreferences prefs = getSharedPreferences(SignInActivity.PREF_SIGN_IN, MODE_PRIVATE);
        mUsername = prefs.getString(SignInActivity.PREF_USERNAME, "value_is_missing");
        mUserPhotoUrl = prefs.getString(SignInActivity.PREF_USER_PHOTO_URL, "value_is_missing");

        setUpActionBar();
        // Set up Drawer after the username was fetched
        setUpDrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    private void setUpActionBar() {
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUpDrawer() {
        if (mNavigationView != null) {
            View headerView = mNavigationView.getHeaderView(0);
            TextView usernameTextView = ButterKnife.findById(headerView, R.id.usernameTextView);
            ImageView avatarImageView = ButterKnife.findById(headerView, R.id.avatarImageView);

            // Set up username in Navigation Drawer
            usernameTextView.setText(mUsername);
            // Set up user photo in Navigation Drawer
            setUserAvatarPhoto(avatarImageView, mUserPhotoUrl);
        }
    }

    void setUserAvatarPhoto(ImageView avatarImageView, String photoUrl ) {
        if (photoUrl == null) {
            photoUrl = Constants.DEFAULT_USER_PHOTO_URL;
        }
        Glide.with(MainActivity.this)
                .load(photoUrl)
                .into(avatarImageView);
    }

    @OnClick(R.id.button_random_play)
    public void onClick() {
        // Launch the RandomOpponentActivity
        startActivity(new Intent(this, RandomOpponentActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mPresenter.onSignOutClicked();
                onSignOut(); // Google
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void signInFail(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToSignInActivity() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}