package com.julia.android.worderly.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.R;
import com.julia.android.worderly.models.Game;
import com.julia.android.worderly.models.Message;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.julia.android.worderly.activities.RandomOpponentActivity.EXTRA_CURRENT_USER_ID;
import static com.julia.android.worderly.activities.RandomOpponentActivity.EXTRA_OPPONENT_USER_ID;

public class GameActivity extends AppCompatActivity {

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 256;
    public static final String MESSAGES_CHILD = "messages";
    public static final String GAMES_CHILD = "games";
    public static final String MOVES_CHILD = "moves";
    private static final String LOG_TAG = GameActivity.class.getSimpleName();
    DatabaseReference mDatabase;
    String mCurrentUserId;
    String mOpponentUserId;
    String mGamePath = "";
    String mGamePathReversed = "";
    @BindView(R.id.currentUserTextView)
    TextView mCurrentUserTextView;
    @BindView(R.id.randomUserTextView)
    TextView mRandomUserTextView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.messageRecyclerView)
    RecyclerView mMessageRecyclerView;
    @BindView(R.id.messageEditText)
    EditText mMessageEditText;
    @BindView(R.id.sendButton)
    Button mSendButton;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        mCurrentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        mOpponentUserId = getIntent().getStringExtra(EXTRA_OPPONENT_USER_ID);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCurrentUserTextView.setText(mCurrentUserId);

        mMessageEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.equals(mGamePath, "") && !Objects.equals(mGamePathReversed, "")) {

                    Message message = new
                            Message(mMessageEditText.getText().toString(),
                            getUserName(),
                            getUserPhotoUrl());

                    mDatabase.child(GAMES_CHILD).child(mGamePath).child(MESSAGES_CHILD)
                            .push().setValue(message);
                    mDatabase.child(GAMES_CHILD).child(mGamePathReversed).child(MESSAGES_CHILD)
                            .push().setValue(message);

                    mMessageEditText.setText("");
                }
            }
        });

        mGamePath = mCurrentUserId + "_" + mOpponentUserId;
        mGamePathReversed = mOpponentUserId + "_" + mCurrentUserId;
        mRandomUserTextView.setText(mOpponentUserId);
        Game game = new Game(mCurrentUserId, mOpponentUserId);

        mDatabase.child(GAMES_CHILD).child(mGamePath).child(MOVES_CHILD).push().setValue(game);
        mDatabase.child(GAMES_CHILD).child(mGamePath).child(MOVES_CHILD).addChildEventListener(
                new ChildEventListener() {
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
        setUpFirebaseAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

    private void setUpFirebaseAdapter() {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mDatabase.child(GAMES_CHILD).child(mGamePathReversed).child(MESSAGES_CHILD)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              Message message, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.messageTextView.setText(message.getText());
                viewHolder.messengerTextView.setText(message.getName());
                if (message.getPhotoUrl() == null) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat.getDrawable(GameActivity.this,
                                    R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(GameActivity.this)
                            .load(message.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getUserPhotoUrl() {
        return FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
    }
}
