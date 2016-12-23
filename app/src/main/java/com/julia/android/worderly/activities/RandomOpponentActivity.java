package com.julia.android.worderly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.julia.android.worderly.R;
import com.julia.android.worderly.models.User;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomOpponentActivity extends AppCompatActivity {

    public static final String EXTRA_CURRENT_USER_ID = "EXTRA_CURRENT_USER_ID";
    public static final String EXTRA_OPPONENT_USER_ID = "EXTRA_OPPONENT_USER_ID";
    public static final String USERS_LOOKING_FOR_OPPONENT_CHILD = "usersLookingForOpponent";
    public static final String GAMES_CHILD = "games";
    private static final String LOG_TAG = RandomOpponentActivity.class.getSimpleName();
    @BindView(R.id.currentUserTextView)
    TextView mCurrentUserTextView;
    @BindView(R.id.randomUserTextView)
    TextView mRandomUserTextView;
    @BindView(R.id.timerTextView)
    TextView mTimerTextView;

    DatabaseReference mDatabase;
    boolean mOpponentFound = false;
    String mCurrentUid = "";
    String mOpponentUid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_opponent);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCurrentUserTextView.setText(getUid());

        // Add the user looking for the opponent under usersLookingForOpponent path
        User user = new User(getUserName(), getUserPhotoUrl());
        mCurrentUid =  getUid();
        mDatabase.child(USERS_LOOKING_FOR_OPPONENT_CHILD).child(mCurrentUid).setValue(user);

        mDatabase.child(USERS_LOOKING_FOR_OPPONENT_CHILD)
                .addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        mOpponentUid = dataSnapshot.getKey();

                        if (!Objects.equals(mCurrentUid, mOpponentUid) && !mOpponentFound) {
                            mOpponentFound = true;
                            mRandomUserTextView.setText(mOpponentUid);

                            String gamePath = mCurrentUid + "_" + mOpponentUid;
                            String gamePathReversed = mOpponentUid + "_" + mCurrentUid;

                            mDatabase.child(GAMES_CHILD).child(gamePath).setValue(true);
                            // Listen for the opponent ready
                            mDatabase.child(GAMES_CHILD).child(gamePathReversed)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            new CountDownTimer(6000, 1000) {

                                                public void onTick(long millisUntilFinished) {
                                                    mTimerTextView.setText(String.valueOf(
                                                            millisUntilFinished / 1000));
                                                }

                                                public void onFinish() {
                                                    // Start new activity
                                                    Intent intent = new Intent(
                                                            getApplicationContext(),
                                                            GameActivity.class);
                                                    intent.putExtra(
                                                            EXTRA_CURRENT_USER_ID, mCurrentUid);
                                                    intent.putExtra(
                                                            EXTRA_OPPONENT_USER_ID, mOpponentUid);
                                                    startActivity(intent);
                                                    // Clean up users
                                                    mDatabase.child(USERS_LOOKING_FOR_OPPONENT_CHILD)
                                                            .child(mCurrentUid).removeValue();
                                                }
                                            }.start();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
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
                });
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
        mDatabase.child(USERS_LOOKING_FOR_OPPONENT_CHILD).child(mCurrentUid).removeValue();
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getUserPhotoUrl() {
        return FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
    }

}
