package com.julia.android.worderly.ui.chat.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.julia.android.worderly.App;
import com.julia.android.worderly.R;
import com.julia.android.worderly.StringPreference;
import com.julia.android.worderly.model.Message;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.chat.adapter.ChatFirebaseAdapter;
import com.julia.android.worderly.ui.chat.adapter.MessageViewHolder;
import com.julia.android.worderly.ui.chat.presenter.ChatPresenter;
import com.julia.android.worderly.utils.Constants;
import com.julia.android.worderly.utils.FirebaseConstants;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static com.julia.android.worderly.utils.Constants.DEFAULT_MSG_LENGTH_LIMIT;
import static com.julia.android.worderly.utils.Constants.EXTRA_OPPONENT;

public class ChatFragment extends Fragment implements ChatPresenter.View {

    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.messageRecyclerView) RecyclerView mMessageRecyclerView;
    @BindView(R.id.messageEditText) EditText mMessageEditText;
    @BindView(R.id.button_send) Button mSendButton;
    @Inject StringPreference mPrefs;
    private Unbinder mUnbinder;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private ChatPresenter mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(getContext()).component().inject(this);
        mPresenter = new ChatPresenter(this);
        mPresenter.setOpponentFromBundle(getOpponentBundleExtras());
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
//        if (getActivity() != null) {
//            ((GameActivity) getActivity()).setChatTabNewMessageTitle();
//        }
    }


    @Override
    public User getUserFromPrefs() {
        String json = mPrefs.get();
        if (!Objects.equals(json, Constants.PREF_USER_DEFAULT_VALUE)) {
            return new Gson().fromJson(json, User.class);
        }
        return null;
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


    private User getOpponentBundleExtras() {
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            return extras.getParcelable(EXTRA_OPPONENT);
        }
        return null;
    }

}
