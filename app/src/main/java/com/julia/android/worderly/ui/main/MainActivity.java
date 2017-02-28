package com.julia.android.worderly.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import com.julia.android.worderly.ui.search.view.SearchOpponentActivity;
import com.julia.android.worderly.ui.signin.SignInActivity;
import com.julia.android.worderly.utils.Constants;
import com.julia.android.worderly.utils.NetworkUtility;

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

    @BindView(R.id.coordinator_layout_main) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.button_random_play) Button mRandomPlayButton;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    @BindView(R.id.toolbar_main_activity) Toolbar mToolbar;
    private MainPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new MainPresenterImpl(this);
        getSharedPrefs();
        setGoogleApiClient();
        setUpActionBar();
        mPresenter.showUserInfoInDrawer();
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
                onSignOut(); // via Google
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void setUpDrawer(String username, String photoUrl) {
        if (mNavigationView != null) {
            View headerView = mNavigationView.getHeaderView(0);
            TextView usernameTextView = ButterKnife.findById(headerView, R.id.usernameTextView);
            ImageView avatarImageView = ButterKnife.findById(headerView, R.id.avatarImageView);
            // Set up username in Navigation Drawer
            setUsernameInDrawer(usernameTextView, username);
            // Set up mUser photo in Navigation Drawer
            setUserPhotoInDrawer(avatarImageView, photoUrl);
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


    /**
     * On click start the game with random opponent when network is available
     */
    @OnClick(R.id.button_random_play)
    public void onClick() {
        // Launch the SearchOpponentActivity if network is online
        if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {
            startActivity(new Intent(this, SearchOpponentActivity.class));
        } else {
            Snackbar.make(mCoordinatorLayout, getString(R.string.error_network_offline),
                    Snackbar.LENGTH_LONG).show();
        }
    }


    private void getSharedPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(PREF_USER, Constants.PREF_USER_DEFAULT_VALUE);
        if (!Objects.equals(json, Constants.PREF_USER_DEFAULT_VALUE)) {
            User user = gson.fromJson(json, User.class);
            mPresenter.setUserFromJson(user);
        }
    }


    private void setUpActionBar() {
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
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

}