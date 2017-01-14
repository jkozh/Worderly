package com.julia.android.worderly.ui.randomopponent.view;

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
import com.julia.android.worderly.ui.randomopponent.presenter.SearchOpponentPresenter;
import com.julia.android.worderly.ui.randomopponent.presenter.SearchOpponentPresenterImpl;
import com.julia.android.worderly.utils.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;

public class SearchOpponentActivity extends AppCompatActivity implements RandomOpponentView {

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
    User mUser;
    private SearchOpponentPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_opponent);
        ButterKnife.bind(this);
        getSharedPrefs();
        mPresenter = new SearchOpponentPresenterImpl(this);
        setUpActionBar();
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
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
        mPresenter.addUserToOnlineUsers(mUser);
        mPresenter.searchForOpponent(mUser);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.removeUserFromOnlineUsers(mUser);
        mPresenter.onDestroy();
    }

    private void setUpActionBar() {
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void addOpponentView(String uid, String username, String photoUrl) {
        mSearchingOpponentTextView.setText("Opponent found:");
        if (photoUrl == null) {
            photoUrl = Constants.DEFAULT_USER_PHOTO_URL;
        }
        Glide.with(SearchOpponentActivity.this)
                .load(photoUrl)
                .into(mAvatarOpponentImageView);

        mUsernameOpponentTextView.setText(username);
        mUidOpponentTextView.setText(uid);
    }
}
