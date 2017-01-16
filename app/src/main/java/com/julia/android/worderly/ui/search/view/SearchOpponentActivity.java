package com.julia.android.worderly.ui.search.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.User;
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

    @BindView(R.id.toolbar_randomopponent_activity)
    Toolbar mToolbar;
    @BindView(R.id.text_searching_opponent)
    TextView mSearchingOpponentTextView;
    @BindView(R.id.image_avatar_opponent)
    CircleImageView mAvatarOpponentImageView;
    @BindView(R.id.text_username_opponent)
    TextView mUsernameOpponentTextView;
    @BindView(R.id.text_uid_opponent)
    TextView mUidOpponentTextView;

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
    public void addOpponentFoundView(String uid, String username, String photoUrl) {
        mSearchingOpponentTextView.setText(R.string.msg_opponent_found);
        if (photoUrl == null) {
            photoUrl = Constants.DEFAULT_USER_PHOTO_URL;
        }
        Glide.with(SearchOpponentActivity.this)
                .load(photoUrl)
                .into(mAvatarOpponentImageView);

        mUsernameOpponentTextView.setText(username);
        mUidOpponentTextView.setText(uid);
    }

    /**
     * Opponent found -> launch the Game activity
     */
    @Override
    public void navigateToGameActivity(User opponentUser) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra(Constants.EXTRA_OPPONENT_ID, opponentUser.getId());
        i.putExtra(Constants.EXTRA_OPPONENT_USERNAME, opponentUser.getUsername());
        i.putExtra(Constants.EXTRA_OPPONENT_EMAIL, opponentUser.getEmail());
        i.putExtra(Constants.EXTRA_OPPONENT_PHOTO_URL, opponentUser.getPhotoUrl());
        startActivity(i);
        finish();
    }
}
