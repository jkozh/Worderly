package com.julia.android.worderly.ui.search.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.network.DataCallback;
import com.julia.android.worderly.network.WordRequest;
import com.julia.android.worderly.ui.game.view.GameActivity;
import com.julia.android.worderly.ui.search.presenter.SearchOpponentPresenter;
import com.julia.android.worderly.ui.search.presenter.SearchOpponentPresenterImpl;
import com.julia.android.worderly.utils.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;

public class SearchOpponentActivity extends AppCompatActivity implements SearchOpponentView {

    private static final String TAG = SearchOpponentActivity.class.getSimpleName();
    @BindView(R.id.toolbar_randomopponent_activity) Toolbar mToolbar;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.text_searching_opponent) TextView mSearchingOpponentTextView;
    @BindView(R.id.image_avatar_opponent) CircleImageView mAvatarOpponentImageView;
    @BindView(R.id.text_username_opponent) TextView mUsernameOpponentTextView;
    private SearchOpponentPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_opponent);
        ButterKnife.bind(this);
        mPresenter = new SearchOpponentPresenterImpl(this);
        getSharedPrefs();
        setUpActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addOpponentFoundView(final String username, final String photoUrl) {
        mSearchingOpponentTextView.setText(R.string.msg_opponent_found);
        if(photoUrl != null) {
            Glide.with(SearchOpponentActivity.this).load(photoUrl)
                    .into(mAvatarOpponentImageView);
        }
        mUsernameOpponentTextView.setText(username);
    }

    /**
     * Opponent found -> launch the Game activity
     */
    @Override
    public void navigateToGameActivity(final User opponentUser) {
        // Fetching word from API
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        new WordRequest(requestQueue, new DataCallback() {
            @Override
            public void onSuccess(String word, String definition) {
                Log.d(TAG, "WORD:" + word + " DEF:" + definition);
                Intent i = new Intent(SearchOpponentActivity.this, GameActivity.class);
                i.putExtra(Constants.EXTRA_OPPONENT_ID, opponentUser.getId());
                i.putExtra(Constants.EXTRA_OPPONENT_USERNAME, opponentUser.getUsername());
                i.putExtra(Constants.EXTRA_OPPONENT_EMAIL, opponentUser.getEmail());
                i.putExtra(Constants.EXTRA_OPPONENT_PHOTO_URL, opponentUser.getPhotoUrl());
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                startActivity(i);
                finish();
            }
        });
    }

    private void getSharedPrefs() {
        SharedPreferences mPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_USER, Constants.PREF_USER_DEFAULT_VALUE);
        if (!Objects.equals(json, Constants.PREF_USER_DEFAULT_VALUE)) {
            User user = gson.fromJson(json, User.class);
            mPresenter.setUserFromJson(user);
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
}
