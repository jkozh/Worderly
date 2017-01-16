package com.julia.android.worderly.ui.game.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.chat.view.ChatActivity;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.ui.game.presenter.GamePresenterImpl;
import com.julia.android.worderly.utils.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;

public class GameActivity extends AppCompatActivity implements GameView {

    private static final String TAG = GameActivity.class.getSimpleName();
    @BindView(R.id.toolbar_game_activity)
    Toolbar mToolbar;
    @BindView(R.id.text_current_user)
    TextView mCurrentUserTextView;
    @BindView(R.id.text_username_opponent)
    TextView mOpponentUserTextView;

    private GamePresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        mPresenter = new GamePresenterImpl(this);
        getBundleExtras();
        getSharedPrefs();
        setUpActionBar();
    }

    private void getBundleExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString(Constants.EXTRA_OPPONENT_ID);
            String username = extras.getString(Constants.EXTRA_OPPONENT_USERNAME);
            String email = extras.getString(Constants.EXTRA_OPPONENT_EMAIL);
            String photoUrl = extras.getString(Constants.EXTRA_OPPONENT_PHOTO_URL);
            mPresenter.setUserFromBundle(id, username, email, photoUrl);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chat:
                mPresenter.onChatButtonClick();
                return true;
            case R.id.action_resign:
                Toast.makeText(this, "Resigned", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void addCurrentUserView(String username) {
        mCurrentUserTextView.setText(username);
    }

    @Override
    public void addOpponentUserView(String username) {
        mOpponentUserTextView.setText(username);
    }

    /**
     * Launch from GameActivity -> ChatActivity
     */
    @Override
    public void navigateToChatActivity(String opponentUid, String opponentUsername) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra(Constants.EXTRA_OPPONENT_ID, opponentUid);
        i.putExtra(Constants.EXTRA_OPPONENT_USERNAME, opponentUsername);
        startActivity(i);
    }
}
