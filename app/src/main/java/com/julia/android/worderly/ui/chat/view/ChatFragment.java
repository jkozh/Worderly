package com.julia.android.worderly.ui.chat.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.Message;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.chat.adapter.ChatFirebaseAdapter;
import com.julia.android.worderly.ui.chat.adapter.MessageViewHolder;
import com.julia.android.worderly.ui.chat.presenter.ChatPresenter;
import com.julia.android.worderly.ui.game.view.GameActivity;
import com.julia.android.worderly.utils.Constants;
import com.julia.android.worderly.utils.FirebaseConstants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;
import static com.julia.android.worderly.utils.Constants.DEFAULT_MSG_LENGTH_LIMIT;
import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;

public class ChatFragment extends Fragment implements ChatPresenter.View {

    private static final String TAG = ChatFragment.class.getSimpleName();
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.messageRecyclerView) RecyclerView mMessageRecyclerView;
    @BindView(R.id.messageEditText) EditText mMessageEditText;
    @BindView(R.id.button_send) Button mSendButton;
    private Unbinder mUnbinder;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private ChatPresenter mPresenter;
    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ChatPresenter(this);
        mPrefs = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        getUserPrefs();
        getOpponentBundleExtras();
        mPresenter.addListenerForNewMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mMessageEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        setUpFirebaseAdapter();

        // Add separation lines between message items in RecyclerView
        mMessageRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.onDetach();
    }

    @Override
    public void hideProgressBar() {
        if (mFirebaseAdapter.getItemCount() == 0) {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    @Override
    public void setChatTabTitleText() {
        if (getActivity() != null) {
            ((GameActivity) getActivity()).setChatTabNewMessageTitle();
        } else {
            Log.d(TAG, "Parent activity is null");
        }
    }

    @OnTextChanged(value = R.id.messageEditText)
    void onMessageInput(Editable editable) {
        if (editable.toString().trim().length() > 0) {
            mSendButton.setEnabled(true);
        } else {
            mSendButton.setEnabled(false);
        }
    }

    @OnClick(R.id.button_send)
    void onClick() {
        mPresenter.onSendButtonClick(mMessageEditText.getText().toString());
        mMessageEditText.setText("");
    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new ChatFirebaseAdapter(
                Message.class,
                R.layout.item_message,
                MessageViewHolder.class,
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseConstants.FIREBASE_GAMES_CHILD)
                        .child(mPresenter.getChatRoomChild())
                        .child(FirebaseConstants.FIREBASE_MESSAGES_CHILD));

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
        hideProgressBar();
    }

    private void getUserPrefs() {
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_USER, Constants.PREF_USER_DEFAULT_VALUE);
        if (!Objects.equals(json, Constants.PREF_USER_DEFAULT_VALUE)) {
            User user = gson.fromJson(json, User.class);
            mPresenter.setUserFromJson(user);
        }
    }

    private void getOpponentBundleExtras() {
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString(Constants.EXTRA_OPPONENT_ID);
            String username = extras.getString(Constants.EXTRA_OPPONENT_USERNAME);
            String email = extras.getString(Constants.EXTRA_OPPONENT_EMAIL);
            String photoUrl = extras.getString(Constants.EXTRA_OPPONENT_PHOTO_URL);
            User opponent = new User(id, username, email, photoUrl);
            mPresenter.setOpponentFromBundle(opponent);
        }
    }
}
