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
import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.main.presenter.MainPresenter;
import com.julia.android.worderly.ui.main.presenter.MainPresenterImpl;
import com.julia.android.worderly.ui.randomopponent.view.SearchOpponentActivity;
import com.julia.android.worderly.ui.signin.SignInActivity;
import com.julia.android.worderly.utils.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;

/**
 * MainActivity shows a screen after SignIn. It contains some buttons to start a game.
 * Also the left Drawer contains some info about the logged in mUser, and statistics of games.
 */
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
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSharedPrefs();
        mPresenter = new MainPresenterImpl(this);
        setGoogleApiClient();
        setUpActionBar();
        // Set up Drawer after the username was fetched
        setUpDrawer();
    }

    private void getSharedPrefs() {
        SharedPreferences mPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_USER, Constants.PREF_USER_DEFAULT_VALUE);
        if (!Objects.equals(json, Constants.PREF_USER_DEFAULT_VALUE)) {
            mUser = gson.fromJson(json, User.class);
        }
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

            if (mUser != null) {
                // Set up username in Navigation Drawer
                setUsernameInDrawer(usernameTextView, mUser.getUsername());

                // Set up mUser photo in Navigation Drawer
                setUserPhotoInDrawer(avatarImageView, mUser.getPhotoUrl());
            }
        }
    }

    private void setUsernameInDrawer(TextView usernameTextView, String username) {
        if (username == null) {
            username = Constants.GUEST;
        }
        usernameTextView.setText(username);
    }

    private void setUserPhotoInDrawer(ImageView avatarImageView, String photoUrl ) {
        if (photoUrl == null) {
            photoUrl = Constants.DEFAULT_USER_PHOTO_URL;
        }
        Glide.with(MainActivity.this)
                .load(photoUrl)
                .into(avatarImageView);
    }

    /**
     * Start the game with random opponent
     */
    @OnClick(R.id.button_random_play)
    public void onClick() {
        // Launch the SearchOpponentActivity
        startActivity(new Intent(this, SearchOpponentActivity.class));
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

    /**
     * Not signed in, launch the Sign In activity
     */
    @Override
    public void navigateToSignInActivity() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}