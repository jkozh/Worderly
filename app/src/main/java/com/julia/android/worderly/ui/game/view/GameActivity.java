package com.julia.android.worderly.ui.game.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.julia.android.worderly.R;
import com.julia.android.worderly.ui.chat.view.ChatFragment;
import com.julia.android.worderly.ui.game.adapter.GamePagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity { //implements GameView {

    private static final String TAG = GameActivity.class.getSimpleName();
    @BindView(R.id.toolbar_game_activity)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }
        mTabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_tab_chat);
    }

    // This method will call Adapter for ViewPager
    private void setupViewPager(ViewPager viewPager) {
        GamePagerAdapter adapter = new GamePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GameFragment(), "Game");
        adapter.addFragment(new ChatFragment(), "Chat");
        viewPager.setAdapter(adapter);
    }



//    @BindView(R.id.toolbar_game_activity)
//    Toolbar mToolbar;
//    @BindView(R.id.text_current_user)
//    TextView mCurrentUserTextView;
//    @BindView(R.id.text_score_current_user)
//    TextView mScoreCurrentUserTextView;
//    @BindView(R.id.text_username_opponent)
//    TextView mOpponentUserTextView;
//    @BindView(R.id.text_word)
//    TextView mWordTextView;
//    @BindView(R.id.text_countdown)
//    TextView mCountDownTextView;
//    @BindView(R.id.edit_word)
//    EditText mWordEditText;
//    @BindView(R.id.button_send_word)
//    Button mSendWordButton;
//    private MenuItem mChatMenuIcon;
//    private boolean mNewMessageChat;
//
//    private GamePresenter mPresenter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate CALLED");
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_game);
//        ButterKnife.bind(this);
//        mPresenter = new GamePresenterImpl(this);
//        getBundleExtras();
//        getSharedPrefs();
//        setUpActionBar();
//
//        // Fetching word from API
//        //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        //new WordRequest(requestQueue, mPresenter);
//    }
//
//    private void getBundleExtras() {
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            String id = extras.getString(Constants.EXTRA_OPPONENT_ID);
//            String username = extras.getString(Constants.EXTRA_OPPONENT_USERNAME);
//            String email = extras.getString(Constants.EXTRA_OPPONENT_EMAIL);
//            String photoUrl = extras.getString(Constants.EXTRA_OPPONENT_PHOTO_URL);
//            mPresenter.setUserFromBundle(id, username, email, photoUrl);
//        }
//    }
//
//    private void getSharedPrefs() {
//        SharedPreferences mPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = mPrefs.getString(PREF_USER, Constants.PREF_USER_DEFAULT_VALUE);
//        if (!Objects.equals(json, Constants.PREF_USER_DEFAULT_VALUE)) {
//            User user = gson.fromJson(json, User.class);
//            mPresenter.setUserFromJson(user);
//        }
//    }
//
//    private void setUpActionBar() {
//        setSupportActionBar(mToolbar);
//        final ActionBar ab = getSupportActionBar();
//        if (ab != null) {
//            ab.setDisplayHomeAsUpEnabled(true);
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mPresenter.onStart();
//        mNewMessageChat = false;
//        invalidateOptionsMenu();
//        Log.d(TAG, "onStart CALLED");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d(TAG, "onRestart CALLED");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume CALLED");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(TAG, "onPause CALLED");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop CALLED");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mPresenter.onDestroy();
//        Log.d(TAG, "onDestroy CALLED");
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d(TAG, "onCreateOptionsMenu CALLED");
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.game_menu, menu);
//        mChatMenuIcon = menu.findItem(R.id.action_chat);
//        if (mNewMessageChat) {
//            mChatMenuIcon.setIcon(R.drawable.ic_chat_bubble_red_24dp);
//        } else {
//            mChatMenuIcon.setIcon(R.drawable.ic_chat_bubble_white_24dp);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_chat:
//                mPresenter.onChatButtonClick();
//                return true;
//            case R.id.action_resign:
//                Toast.makeText(this, "Resigned", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean("mNewMessageChat", mNewMessageChat);
//        Log.d(TAG, "onSaveInstanceState CALLED");
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mNewMessageChat = savedInstanceState.getBoolean("mNewMessageChat");
//        Log.d(TAG, "onRestoreInstanceState CALLED");
//    }
//
//    @Override
//    public void addCurrentUserView(String username) {
//        mCurrentUserTextView.setText(username);
//    }
//
//    @Override
//    public void addOpponentUserView(String username) {
//        mOpponentUserTextView.setText(username);
//    }
//
//    @Override
//    public void setWordView(String word) {
//        mWordTextView.setText(word);
//    }
//
//    /**
//     * Launch from GameActivity -> ChatActivity
//     */
//    @Override
//    public void navigateToChatActivity(String opponentUid, String opponentUsername) {
//        Intent i = new Intent(this, ChatActivity.class);
//        i.putExtra(Constants.EXTRA_OPPONENT_ID, opponentUid);
//        i.putExtra(Constants.EXTRA_OPPONENT_USERNAME, opponentUsername);
//        startActivity(i);
//    }
//
//    @Override
//    public void startCountDown() {
//        new CountDownTimer(5000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                mCountDownTextView.setText(String.valueOf(millisUntilFinished / 1000));
//            }
//
//            public void onFinish() {
//                // Show modal dialog with info
//                showRoundFinishDialog(2, "wlwlwl");
//            }
//        }.start();
//    }
//
//    @Override
//    public void showRoundFinishDialog(int roundNumber, String word) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Round " + roundNumber + "/5 finished");
//        builder.setMessage("Word: " + word + "\n"
//                + "Your score: 10" + "\n"
//                + "Opponent score: 11");
//
//        final TextView countdownView = new TextView(this);
//        builder.setView(countdownView);
//        countdownView.setGravity(View.TEXT_ALIGNMENT_CENTER);
//
//        final AlertDialog dialog = builder.create();
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//
//        new CountDownTimer(5000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                long seconds = millisUntilFinished / 1000;
//                countdownView.setText(getString(R.string.msg_next_round_starts, seconds));
//            }
//
//            public void onFinish() {
//                dialog.cancel();
//                // next round
//            }
//        }.start();
//    }
//
//    @Override
//    public void setScoreView(int score) {
//        mScoreCurrentUserTextView.setText(getString(R.string.msg_score, score));
//    }
//
//    @OnTextChanged(value = R.id.edit_word)
//    void onWordInput(Editable editable) {
//        int wordLength = editable.toString().trim().length();
//        if (wordLength > 1 && wordLength <= Constants.NUMBER_OF_LETTERS) {
//            mSendWordButton.setEnabled(true);
//        } else {
//            mSendWordButton.setEnabled(false);
//        }
//    }
//
//    @OnClick(R.id.button_send_word)
//    public void onClick() {
//        mPresenter.onSendWordButtonClick(mWordEditText.getText().toString());
//    }
//
//    @Override
//    public void clearWordInput() {
//        mWordEditText.setText("");
//    }
//
//    @Override
//    public void setTitleText(String username) {
//        mToolbar.setTitle("Playing with " + username);
//    }
//
//    @Override
//    public void changeChatIcon() {
//        if (mChatMenuIcon != null ) {//&& mNewMessageChat) {
//            // TODO: Create icons for all densities
//            Log.d(TAG, "changeChatIcon() CALLED " + mNewMessageChat);
//            mChatMenuIcon.setIcon(R.drawable.ic_chat_bubble_red_24dp);
//            mNewMessageChat = true;
//        }
//    }
}
