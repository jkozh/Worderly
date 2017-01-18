package com.julia.android.worderly.ui.chat.view;

import android.support.v7.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {//implements ChatView {

    private static final String TAG = ChatActivity.class.getSimpleName();





//    @BindView(R.id.toolbar_main_activity)
//    Toolbar mToolbar;
//    @BindView(R.id.progressBar)
//    ProgressBar mProgressBar;
//    @BindView(R.id.messageRecyclerView)
//    RecyclerView mMessageRecyclerView;
//    @BindView(R.id.messageEditText)
//    EditText mMessageEditText;
//    @BindView(R.id.button_send)
//    Button mSendButton;
//
//    private LinearLayoutManager mLinearLayoutManager;
//    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
//    private ChatPresenter mPresenter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//        ButterKnife.bind(this);
//        mPresenter = new ChatPresenterImpl(this);
//        getSharedPrefs();
//        getBundleExtras();
//        setUpActionBar();
//
//        mLinearLayoutManager = new LinearLayoutManager(this);
//        mLinearLayoutManager.setStackFromEnd(true);
//        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
//
//        mMessageEditText.setFilters(new InputFilter[]{
//                new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
//
//        setUpFirebaseAdapter();
//
//        // Add separation lines between message items in RecyclerView
//        mMessageRecyclerView.addItemDecoration(
//                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mFirebaseAdapter.cleanup();
//    }
//
//    private void getBundleExtras() {
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            String opponentId = extras.getString(Constants.EXTRA_OPPONENT_ID);
//            String opponentUsername = extras.getString(Constants.EXTRA_OPPONENT_USERNAME);
//            mPresenter.setOpponentInfo(opponentId, opponentUsername);
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
//            //ab.setDisplayShowTitleEnabled(false);
//        }
//    }
//
//    @Override
//    public void setToolbarTitle(String title) {
//        mToolbar.setTitle(getString(R.string.title_chat_with, title));
//    }
//
//    private void setUpFirebaseAdapter() {
//        mFirebaseAdapter = new ChatFirebaseAdapter(
//                Message.class,
//                R.layout.item_message,
//                MessageViewHolder.class,
//                FirebaseDatabase.getInstance().getReference()
//                        .child(FirebaseConstants.FIREBASE_GAMES_CHILD)
//                        .child(mPresenter.getChatRoomChild())
//                        .child(FirebaseConstants.FIREBASE_MESSAGES_CHILD));
//
//        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
//                int lastVisiblePosition =
//                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
//                // If the recycler view is initially being loaded or the
//                // user is at the bottom of the list, scroll to the bottom
//                // of the list to show the newly added message.
//                if (lastVisiblePosition == -1 ||
//                        (positionStart >= (friendlyMessageCount - 1) &&
//                                lastVisiblePosition == (positionStart - 1))) {
//                    mMessageRecyclerView.scrollToPosition(positionStart);
//                }
//            }
//        });
//        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
//
//        // Hide ProgressBar when empty chat log
//        hideProgressBar();
//    }
//
//    @OnTextChanged(value = R.id.messageEditText)
//    void onMessageInput(Editable editable) {
//        if (editable.toString().trim().length() > 0) {
//            mSendButton.setEnabled(true);
//        } else {
//            mSendButton.setEnabled(false);
//        }
//    }
//
//    @OnClick(R.id.button_send)
//    public void onClick() {
//        mPresenter.onSendButtonClick(mMessageEditText.getText().toString());
//        mMessageEditText.setText("");
//    }
//
//
//    @Override
//    public void hideProgressBar() {
//        if (mFirebaseAdapter.getItemCount() == 0) {
//            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
//        }
//    }
}
