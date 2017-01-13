package com.julia.android.worderly.ui.randomopponent.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.julia.android.worderly.R;
import com.julia.android.worderly.ui.randomopponent.presenter.RandomOpponentPresenter;
import com.julia.android.worderly.ui.randomopponent.presenter.RandomOpponentPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RandomOpponentActivity extends AppCompatActivity implements RandomOpponentView {

    private static final String TAG = RandomOpponentActivity.class.getSimpleName();

    @BindView(R.id.toolbar_randomopponent_activity)
    Toolbar mToolbar;
    @BindView(R.id.image_avatar_opponent)
    CircleImageView mAvatarOpponentImageView;
    @BindView(R.id.text_username_opponent)
    TextView mUsernameOpponentTextView;
    @BindView(R.id.text_uid_opponent)
    TextView mUidOpponentTextView;

    private RandomOpponentPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_opponent);
        ButterKnife.bind(this);

        mPresenter = new RandomOpponentPresenterImpl(this);
        setUpActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
        //mPresenter.addUserToOnlineUsers();
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
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
}
