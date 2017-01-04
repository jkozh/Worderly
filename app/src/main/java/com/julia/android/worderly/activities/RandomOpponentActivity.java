package com.julia.android.worderly.activities;

import android.content.Intent;
import android.net.Uri;
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
import com.julia.android.worderly.utils.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomOpponentActivity extends AppCompatActivity {

    public static final String EXTRA_CURRENT_USER_ID = "EXTRA_CURRENT_USER_ID";
    public static final String EXTRA_OPPONENT_USER_ID = "EXTRA_OPPONENT_USER_ID";
    public static final String EXTRA_OPPONENT_USERNAME = "EXTRA_OPPONENT_USERNAME";

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
    String mOpponentUsername = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_opponent);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCurrentUserTextView.setText(getUid());

        // Add the user looking for the opponent under usersLookingForOpponent path
        final User user = new User(getUserName(),
                "https://cdn4.iconfinder.com/data/icons/standard-free-icons/139/Profile01-128.png",
                "");
        mCurrentUid =  getUid();
        mDatabase.child(Constants.USERS_LOOKING_FOR_OPPONENT_CHILD)
                .child(mCurrentUid).setValue(user);

        mDatabase.child(Constants.USERS_LOOKING_FOR_OPPONENT_CHILD)
                .addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final int MAX_USERS = 2;
                        // Need to avoid confusion
                        //if (dataSnapshot.getChildrenCount() < MAX_USERS) {

                            mOpponentUid = dataSnapshot.getKey();

                            if (!Objects.equals(mCurrentUid, mOpponentUid) && !mOpponentFound) {
                                mOpponentFound = true;
                                mRandomUserTextView.setText(mOpponentUid);

                                String gamePath = mCurrentUid + "_" + mOpponentUid;
                                final String gamePathReversed = mOpponentUid + "_" + mCurrentUid;

                                mDatabase.child(Constants.GAMES_CHILD).child(gamePath).setValue(true);
                                // Listen for the opponent ready
                                mDatabase.child(Constants.GAMES_CHILD).child(gamePathReversed)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                mDatabase.child(Constants.GAMES_CHILD)
                                                        .child(gamePathReversed)
                                                        .child(Constants.USER_CHILD)
                                                        .addChildEventListener(
                                                                new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                        User user = dataSnapshot.getValue(User.class);
                                                        mOpponentUsername = user.getUsername();
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

                                                new CountDownTimer(1000, 1000) {

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
                                                        intent.putExtra(
                                                                EXTRA_OPPONENT_USERNAME, mOpponentUsername);
                                                        startActivity(intent);
                                                        // Clean up users
                                                        mDatabase.child(Constants.USERS_LOOKING_FOR_OPPONENT_CHILD)
                                                                .child(mCurrentUid).removeValue();
                                                        mOpponentFound = false;
                                                        mOpponentUid = "";
                                                        mOpponentUsername = "";
                                                    }
                                                }.start();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }
                        //}
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
        mDatabase.child(Constants.USERS_LOOKING_FOR_OPPONENT_CHILD).child(mCurrentUid).removeValue();
        mOpponentFound = false;
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getUserPhotoUrl() {
        Uri userPhoto = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (userPhoto == null) {
            return null;
        }
        return userPhoto.toString();
    }

}
