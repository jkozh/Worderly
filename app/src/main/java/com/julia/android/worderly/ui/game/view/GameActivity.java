package com.julia.android.worderly.ui.game.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.network.WordRequest;
import com.julia.android.worderly.ui.chat.view.ChatActivity;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.ui.game.presenter.GamePresenterImpl;
import com.julia.android.worderly.utils.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;

public class GameActivity extends AppCompatActivity implements GameView {

    private static final String TAG = GameActivity.class.getSimpleName();
    @BindView(R.id.toolbar_game_activity)
    Toolbar mToolbar;
    @BindView(R.id.text_current_user)
    TextView mCurrentUserTextView;
    @BindView(R.id.text_score_current_user)
    TextView mScoreCurrentUserTextView;
    @BindView(R.id.text_username_opponent)
    TextView mOpponentUserTextView;
    @BindView(R.id.text_word)
    TextView mWordTextView;
    @BindView(R.id.text_countdown)
    TextView mCountDownTextView;
    @BindView(R.id.edit_word)
    EditText mWordEditText;
    @BindView(R.id.button_send_word)
    Button mSendWordButton;

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

        // Fetching word from API
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        new WordRequest(requestQueue, mPresenter);
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

    @Override
    public void setWordView(String word) {
        mWordTextView.setText(word);
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

    @Override
    public void startCountDown() {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountDownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                // Show modal dialog with info
                showRoundFinishDialog(2, "wlwlwl");
            }
        }.start();
    }

    @Override
    public void showRoundFinishDialog(int roundNumber, String word) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Round " + roundNumber + "/5 finished");
        builder.setMessage("Word: " + word + "\n"
                + "Your score: 10" + "\n"
                + "Opponent score: 11");

        final TextView countdownView = new TextView(this);

        builder.setView(countdownView);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdownView.setText("Next Round starts in #..." + String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                dialog.cancel();
                // next round
            }
        }.start();
    }

    @Override
    public void setScoreView(int score) {
        mScoreCurrentUserTextView.setText("Score: " + score);
    }


    @OnTextChanged(value = R.id.edit_word)
    void onMessageInput(Editable editable) {
        if (editable.toString().trim().length() > 0) {
            mSendWordButton.setEnabled(true);
        } else {
            mSendWordButton.setEnabled(false);
        }
    }

    @OnClick(R.id.button_send_word)
    public void onClick() {
        mPresenter.onSendWordButtonClick(mWordEditText.getText().toString());
        //mWordEditText.setText("");
    }
}
