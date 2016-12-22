package com.julia.android.worderly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomOpponentActivity extends AppCompatActivity {

    public static final String EXTRA_CURRENT_USER_ID = "EXTRA_CURRENT_USER_ID";
    public static final String EXTRA_OPPONENT_USER_ID = "EXTRA_OPPONENT_USER_ID";
    private static final String LOG_TAG = RandomOpponentActivity.class.getSimpleName();
    DatabaseReference mDatabase;
    boolean opponentFound = false;

    @BindView(R.id.currentUserTextView)
    TextView mCurrentUserTextView;
    @BindView(R.id.randomUserTextView)
    TextView mRandomUserTextView;
    @BindView(R.id.timerTextView)
    TextView mTimerTextView;

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getUserPhotoUrl() {
        return FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_opponent);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCurrentUserTextView.setText(getUid());

        mDatabase.child("usersWhoWantsToPlay").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "onChildAdded! dataSnapshot: " + dataSnapshot + ", s: " + s);

                final String currentUid =  getUid();
                final String opponentUid = dataSnapshot.getKey();

                if (!Objects.equals(currentUid, opponentUid) && !opponentFound) {
                    opponentFound = true;
                    mRandomUserTextView.setText(opponentUid);

                    new CountDownTimer(6000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            mTimerTextView.setText(Long.toString(millisUntilFinished / 1000));
                        }

                        public void onFinish() {
                            // Start new activity
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra(EXTRA_CURRENT_USER_ID, currentUid);
                            intent.putExtra(EXTRA_OPPONENT_USER_ID, opponentUid);
                            startActivity(intent);
                        }
                    }.start();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "onChildChanged! dataSnapshot: " + dataSnapshot + ", s: " + s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "onChildRemoved! dataSnapshot: ");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "onChildMoved! dataSnapshot: " + dataSnapshot + ", s: " + s);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "onCancelled! databaseError: " + databaseError);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
