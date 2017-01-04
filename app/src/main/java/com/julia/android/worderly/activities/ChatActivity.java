package com.julia.android.worderly.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.julia.android.worderly.R;
import com.julia.android.worderly.authenticator.SignInActivity;
import com.julia.android.worderly.models.Message;
import com.julia.android.worderly.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ChatActivity extends AppCompatActivity {

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 256;
    private static final String LOG_TAG = ChatActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.messageRecyclerView)
    RecyclerView mMessageRecyclerView;
    @BindView(R.id.messageEditText)
    EditText mMessageEditText;
    @BindView(R.id.sendButton)
    Button mSendButton;

    DatabaseReference mDatabase;
    String mGamePath = "";
    String mGamePathReversed = "";
    String mOpponentUsername = "";

    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setUpActionBar();

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mGamePath = extras.getString(GameActivity.EXTRA_GAME_PATH);
            mGamePathReversed = extras.getString(GameActivity.EXTRA_GAME_PATH_REVERSED);
            mOpponentUsername = extras.getString(RandomOpponentActivity.EXTRA_OPPONENT_USERNAME);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mMessageEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        setUpFirebaseAdapter();

        // Add separation lines between message items in RecyclerView
        mMessageRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @OnTextChanged(value = R.id.messageEditText)
    void onMessageInput(Editable editable) {
        if (editable.toString().trim().length() > 0) {
            mSendButton.setEnabled(true);
        } else {
            mSendButton.setEnabled(false);
        }
    }

    @OnClick(R.id.sendButton)
    public void onClick() {
        Message message = new
                Message(mMessageEditText.getText().toString(),
                getUserName(),
                getUserPhotoUrl());

        mDatabase.child(Constants.GAMES_CHILD).child(mGamePath).child(Constants.MESSAGES_CHILD)
                .push().setValue(message);
        mDatabase.child(Constants.GAMES_CHILD).child(mGamePathReversed).child(Constants.MESSAGES_CHILD)
                .push().setValue(message);

        mMessageEditText.setText("");
    }

    private void setUpActionBar() {
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);

        }
        mToolbar.setTitle("Chat with " + mOpponentUsername);
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
        mFirebaseAdapter.cleanup();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpFirebaseAdapter() {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mDatabase.child(Constants.GAMES_CHILD).child(mGamePathReversed).child(Constants.MESSAGES_CHILD)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              Message message, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                String date = "";
                Object ts = message.getTimeStamp();
                if (ts != null) {
                    if (DateUtils.isToday((long)ts)) {
                        date = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(ts);
                    } else {
                        // TODO: Think about it below
                        date = new SimpleDateFormat("EEE d, hh:mm", Locale.getDefault()).format(ts);
                    }
                }

                viewHolder.messengerTextView.setText(message.getName() + ": ");
                viewHolder.messageTextView.setText(message.getText());
                viewHolder.timeTextView.setText(date);
                if (message.getPhotoUrl() == null) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat.getDrawable(ChatActivity.this,
                                    R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(ChatActivity.this)
                            .load(message.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }

                // Set striped view for the opponent messages
                if(Objects.equals(message.getName(), mOpponentUsername)) {
                    viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(
                            viewHolder.itemView.getContext(), R.color.colorChatDark));
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

        // Hide ProgressBar when empty chat log
        if (mFirebaseAdapter.getItemCount() == 0) {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    public String getUserName() {
        SharedPreferences prefs = getSharedPreferences(SignInActivity.PREF_SIGN_IN, MODE_PRIVATE);
        return prefs.getString(SignInActivity.PREF_USERNAME, "value_is_missing");
    }

    public String getUserPhotoUrl() {
        Uri userPhoto = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (userPhoto == null) {
            return null;
        }
        return userPhoto.toString();
    }
}
