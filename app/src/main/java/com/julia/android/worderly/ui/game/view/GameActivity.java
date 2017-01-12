package com.julia.android.worderly.ui.game.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.Game;
import com.julia.android.worderly.ui.chat.view.ChatActivity;
import com.julia.android.worderly.ui.randomopponent.view.RandomOpponentActivity;
import com.julia.android.worderly.utils.FirebaseConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_PATH = "EXTRA_GAME_PATH";
    public static final String EXTRA_GAME_PATH_REVERSED = "EXTRA_GAME_PATH_REVERSED";
    private static final String LOG_TAG = GameActivity.class.getSimpleName();
    @BindView(R.id.toolbar_main_activity)
    Toolbar mToolbar;
    @BindView(R.id.currentUserTextView)
    TextView mCurrentUserTextView;
    @BindView(R.id.randomUserTextView)
    TextView mRandomUserTextView;

    DatabaseReference mDatabase;
    String mCurrentUserId;
    String mOpponentUserId;
    String mOpponentUsername = "";
    String mGamePath = "";
    String mGamePathReversed = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        setUpActionBar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCurrentUserId = extras.getString(RandomOpponentActivity.EXTRA_CURRENT_USER_ID);
            mOpponentUserId = extras.getString(RandomOpponentActivity.EXTRA_OPPONENT_USER_ID);
            mOpponentUsername = extras.getString(RandomOpponentActivity.EXTRA_OPPONENT_USERNAME);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCurrentUserTextView.setText("UserID:"+mCurrentUserId+" NAME:"+getUserName());

        mGamePath = mCurrentUserId + "_" + mOpponentUserId;
        mGamePathReversed = mOpponentUserId + "_" + mCurrentUserId;

        mRandomUserTextView.setText(mOpponentUserId);
        Game game = new Game(mCurrentUserId, mOpponentUserId);

        //User currentUser = new User(getUserName(), getUserPhotoUrl(), "");

        //mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD).child(mGamePath).child(FirebaseConstants.FIREBASE_USER_CHILD)
        //        .child(getUserName()).setValue(currentUser);
        mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD).child(mGamePath).child(FirebaseConstants.FIREBASE_MOVES_CHILD)
                .push().setValue(game);
        mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD).child(mGamePath).child(FirebaseConstants.FIREBASE_MOVES_CHILD)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }
        );
    }

    private void setUpActionBar() {
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
        mToolbar.setTitle("You are playing with " + mOpponentUsername);
        //mToolbar.setSubtitle(mOpponentUsername);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.child(FirebaseConstants.FIREBASE_GAMES_CHILD).child(mGamePath).removeValue();
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
                Intent intent = new Intent(GameActivity.this, ChatActivity.class);
                intent.putExtra(EXTRA_GAME_PATH, mGamePath);
                intent.putExtra(EXTRA_GAME_PATH_REVERSED, mGamePathReversed);
                intent.putExtra(RandomOpponentActivity.EXTRA_OPPONENT_USERNAME, mOpponentUsername);
                startActivity(intent);
                return true;
            case R.id.action_resign:
                Toast.makeText(this, "Resigned", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getUserName() {
        //SharedPreferences prefs = getSharedPreferences(SignInActivity.PREF_SIGN_IN, MODE_PRIVATE);
        return "null";//prefs.getString(SignInActivity.PREF_USERNAME, "value_is_missing");
    }

    public String getUserPhotoUrl() {
        // TODO: Fix it below
        return "https://cdn4.iconfinder.com/data/icons/standard-free-icons/139/Profile01-128.png";
    }
}
