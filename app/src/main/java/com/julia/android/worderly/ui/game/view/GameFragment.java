package com.julia.android.worderly.ui.game.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.data.database.WordContract;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.network.CheckWordCallback;
import com.julia.android.worderly.network.CheckWordRequest;
import com.julia.android.worderly.ui.game.dragdrop.Listener;
import com.julia.android.worderly.ui.game.dragdrop.TilesList;
import com.julia.android.worderly.ui.game.dragdrop.WordListAdapter;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.ui.main.view.MainActivity;
import com.julia.android.worderly.utils.Constants;
import com.julia.android.worderly.utils.WordUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;
import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;

public class GameFragment extends Fragment implements GamePresenter.View, Listener {

    private static final String TAG = GameFragment.class.getSimpleName();

    // Member variables for binding views using ButterKnife
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.text_username_opponent) TextView mOpponentUsernameTextView;
    @BindView(R.id.text_user_score) TextView mUserScoreTextView;
    @BindView(R.id.text_opponent_score) TextView mOpponentScoreTextView;
    @BindView(R.id.recycler_view_top) RecyclerView mRecyclerViewTop;
    @BindView(R.id.recycler_view_bottom) RecyclerView mRecyclerViewBottom;
    @BindView(R.id.frame_top) FrameLayout mFrameTop;
    @BindView(R.id.frame_bottom) FrameLayout mFrameBottom;
    @BindView(R.id.image_holder) ImageView mImageHolder;
    WordListAdapter mTopListAdapter;
    WordListAdapter mBottomListAdapter;
    String shuffledWord = "";
    private List<TilesList> mTilesListTop;
    private List<TilesList> mTilesListBottom;
    private Unbinder mUnbinder;
    private GamePresenter mPresenter;
    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new GamePresenter(this);
        mPrefs = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        getUserPrefs();
        getOpponentBundleExtras();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManagerTop = new LinearLayoutManager(getContext());
        layoutManagerTop.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewTop.setLayoutManager(layoutManagerTop);
        mTilesListTop = new ArrayList<>();

        mTopListAdapter = new WordListAdapter(mTilesListTop, this);
        mRecyclerViewTop.setAdapter(mTopListAdapter);
        mRecyclerViewTop.setOnDragListener(mTopListAdapter.getDragInstance());
        mFrameTop.setOnDragListener(mTopListAdapter.getDragInstance());

        LinearLayoutManager layoutManagerBottom = new LinearLayoutManager(getContext());
        layoutManagerBottom.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewBottom.setLayoutManager(layoutManagerBottom);

        shuffleLetters();

        mBottomListAdapter = new WordListAdapter(mTilesListBottom, this);
        mRecyclerViewBottom.setAdapter(mBottomListAdapter);
        mRecyclerViewBottom.setOnDragListener(mBottomListAdapter.getDragInstance());

        mRecyclerViewTop.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mRecyclerViewBottom.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));

        mFrameBottom.setOnDragListener(mBottomListAdapter.getDragInstance());
        mImageHolder.setOnDragListener(mTopListAdapter.getDragInstance());
        mPresenter.setCurrentUserView();
        mPresenter.setOpponentUserView();
        new GameCountDownTimer(30000, 1).start();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void showCurrentUsernameView(String username) {
        //mCurrentUsernameTextView.setText(username);
    }

    @Override
    public void showOpponentUsernameView(String username) {
        mOpponentUsernameTextView.setText(username);
    }

    @Override
    public void showWordView(String word) {
//        mWordTextView.setText(word);
    }

    @Override
    public void showDefinitionView(String definition) {
//        mWordDefinitionTextView.setText(definition);
    }

    /**
     * If the user sends a wrong word, then show him a toast, and empty edit text
     */
    @Override
    public void showWrongWordToast() {
        Toast.makeText(
                getActivity(), getString(R.string.msg_wrong_word), Toast.LENGTH_SHORT).show();
        // Remove letters in Edit Text
//        mWordEditText.setText("");
    }

    @Override
    public void showRoundFinishedDialog(boolean isWon, String word) {
        if (getActivity() != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            RoundFinishedDialogFragment alertDialog =
                    RoundFinishedDialogFragment.newInstance("Round finished");
            alertDialog.show(fm, "fragment_alert");
        }
    }

    @Override
    public void setEmptyListTop(boolean visibility) {
        mImageHolder.setVisibility(visibility ? View.VISIBLE : View.GONE);
        mFrameTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
        mRecyclerViewTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    private void shuffleLetters() {
        char[] c = WordUtility.scrambleWord(shuffledWord).toCharArray();
        Log.d(TAG, Arrays.toString(c));
        mTilesListBottom = new ArrayList<>();
        for (int i = 0; i < Constants.NUMBER_OF_LETTERS; i++) {
            int color = getResources().getIdentifier(
                    "tile" + i, "color", getContext().getPackageName());
            mTilesListBottom.add(i, new TilesList(c[i], color, WordUtility.getTileValue(c[i])));
        }
    }

    @OnClick(R.id.button_clear)
    public void onClearClick() {
        if (mTilesListTop.size() != 0) {
            mTilesListTop.clear();
            mTopListAdapter = new WordListAdapter(mTilesListTop, this);
            mRecyclerViewTop.setAdapter(mTopListAdapter);
            mTilesListBottom.clear();
            shuffleLetters();
            mBottomListAdapter = new WordListAdapter(mTilesListBottom, this);
            mRecyclerViewBottom.setAdapter(mBottomListAdapter);
        }
    }

    @OnClick(R.id.button_send)
    public void onSendClick() {
        // Check if length of tiles is not empty
        if (mTilesListTop.size() != 0) {
            String word = "";
            for (int i = 0; i < mTilesListTop.size(); i++) {
                TilesList q = mTilesListTop.get(i);
                word += q.letter;
            }
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            new CheckWordRequest(requestQueue, word, new CheckWordCallback() {
                @Override
                public void onSuccess(String definition) {
                    Toast.makeText(getContext(), "YEP", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(getContext(), "NOPE", Toast.LENGTH_SHORT).show();
                }
            });
            //mPresenter.onSendClick(qwerty);

        } else {
            Toast.makeText(getContext(), "Nothing to send!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.button_shuffle)
    public void onShuffleClick() {
        if (mTilesListBottom.size() != 0) {
            mTilesListTop.clear();
            mTopListAdapter = new WordListAdapter(mTilesListTop, this);
            mRecyclerViewTop.setAdapter(mTopListAdapter);
            mTilesListBottom.clear();
            shuffleLetters();
            mBottomListAdapter = new WordListAdapter(mTilesListBottom, this);
            mRecyclerViewBottom.setAdapter(mBottomListAdapter);
        }
    }

    public void resign() {
        mPresenter.notifyOpponentAboutResign();
        mPresenter.deleteGameRoom();
        mPresenter.showLoseDialog();
    }

    private void navigateToMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    private void deleteWord() {
        // Remove word from database
        Uri uri = WordContract.WordEntry.CONTENT_URI;
        getContext().getContentResolver().delete(uri, null, null);
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
            shuffledWord = extras.getString("EXTRA_WORD");
        }
    }

}
