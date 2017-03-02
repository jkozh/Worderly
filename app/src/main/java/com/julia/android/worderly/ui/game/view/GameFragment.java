/*
* Copyright 2017 Julia Kozhukhovskaya
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.julia.android.worderly.ui.game.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.Move;
import com.julia.android.worderly.model.Player;
import com.julia.android.worderly.model.Round;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.game.dialog.DialogListener;
import com.julia.android.worderly.ui.game.dialog.GameRoundDialogFragment;
import com.julia.android.worderly.ui.game.dragdrop.Listener;
import com.julia.android.worderly.ui.game.dragdrop.TilesList;
import com.julia.android.worderly.ui.game.dragdrop.TilesListAdapter;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.ui.main.MainActivity;
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
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static com.julia.android.worderly.utils.Constants.EXTRA_OPPONENT;
import static com.julia.android.worderly.utils.Constants.NUMBER_OF_LETTERS;
import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;


public class GameFragment extends Fragment implements GamePresenter.View, Listener,
        DialogListener {

    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.text_progress) TextView mTextProgressView;
    @BindView(R.id.text_username_opponent) TextView mOpponentUsernameTextView;
    @BindView(R.id.text_user_score) TextView mUserScoreTextView;
    @BindView(R.id.text_opponent_score) TextView mOpponentScoreTextView;
    @BindView(R.id.recycler_view_top) RecyclerView mRecyclerViewTop;
    @BindView(R.id.recycler_view_bottom) RecyclerView mRecyclerViewBottom;
    @BindView(R.id.frame_top) FrameLayout mFrameTop;
    @BindView(R.id.frame_bottom) FrameLayout mFrameBottom;
    @BindView(R.id.image_holder) ImageView mImageHolder;
    TilesListAdapter mTopListAdapter;
    TilesListAdapter mBottomListAdapter;
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
        mPresenter.setUserFromJson(getUserPrefs());
        mPresenter.setOpponentFromBundle(getOpponentBundleExtras());
        mPresenter.setWord(shuffledWord);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mTilesListTop = new ArrayList<>();

        mTopListAdapter = new TilesListAdapter(mTilesListTop, this);
        mRecyclerViewTop.setAdapter(mTopListAdapter);
        mRecyclerViewTop.setOnDragListener(mTopListAdapter.getDragInstance());
        mFrameTop.setOnDragListener(mTopListAdapter.getDragInstance());

        shuffleLetters();

        mBottomListAdapter = new TilesListAdapter(mTilesListBottom, this);
        mRecyclerViewBottom.setAdapter(mBottomListAdapter);
        mRecyclerViewBottom.setOnDragListener(mBottomListAdapter.getDragInstance());

        mRecyclerViewTop.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mRecyclerViewBottom.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));

        mFrameBottom.setOnDragListener(mBottomListAdapter.getDragInstance());
        mImageHolder.setOnDragListener(mTopListAdapter.getDragInstance());
        mPresenter.setOpponentUserView();
        mUserScoreTextView.setText("0");
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        new GameCountDownTimer((mProgressBar.getMax() - mProgressBar.getProgress())*1000, 1,
                mProgressBar, mTextProgressView, this).start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("progress", mProgressBar.getProgress());
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            int progress = savedInstanceState.getInt("progress");
            mProgressBar.setProgress(progress);
            mTextProgressView.setText(String.valueOf(mProgressBar.getMax() - progress));
        }
    }


    @Override
    public void showOpponentUsernameView(String username) {
        mOpponentUsernameTextView.setText(username);
    }


    /**
     * If the user sends a wrong word, then show him a toast, and empty edit text
     */
    @Override
    public void showWrongWordToast() {
        Toast.makeText(
                getActivity(), getString(R.string.msg_wrong_word), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showOpponentScore(String score) {
        mOpponentScoreTextView.setText(score);
    }

    @Override
    public String getUserScoreText() {
        return mUserScoreTextView.getText().toString();
    }


    public void setUserScoreTextView(String score) {
        mUserScoreTextView.setText(score);
    }


    @Override
    public void setEmptyListTop(boolean visibility) {
        mImageHolder.setVisibility(visibility ? View.VISIBLE : View.GONE);
        mFrameTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
        mRecyclerViewTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }


    private void shuffleLetters() {
        char[] c = WordUtility.scrambleWord(shuffledWord).toCharArray();
        Timber.d(Arrays.toString(c));
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
            mTopListAdapter = new TilesListAdapter(mTilesListTop, this);
            mRecyclerViewTop.setAdapter(mTopListAdapter);
            mTilesListBottom.clear();
            shuffleLetters();
            mBottomListAdapter = new TilesListAdapter(mTilesListBottom, this);
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
            // check if that word equals to fetched word, if yes - no need to do the request
            if (mTilesListTop.size() == NUMBER_OF_LETTERS && mPresenter.isWordsEquals(word)) {
                Toast.makeText(getContext(), "YOU GUESSED THE WHOLE WORD RIGHT!", Toast.LENGTH_SHORT).show();
                String score = String.valueOf(WordUtility.getWordValue(word));
                setUserScoreTextView(String.valueOf(score));
                mPresenter.sendUserScoreAndWord(new Move(word, score));
            } else {
                mPresenter.getVolleyRequest(getContext(), word);
            }
        } else {
            Toast.makeText(getContext(), "Nothing to send!", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.button_shuffle)
    public void onShuffleClick() {
        if (mTilesListBottom.size() != 0) {
            mTilesListTop.clear();
            mTopListAdapter = new TilesListAdapter(mTilesListTop, this);
            mRecyclerViewTop.setAdapter(mTopListAdapter);
            mTilesListBottom.clear();
            shuffleLetters();
            mBottomListAdapter = new TilesListAdapter(mTilesListBottom, this);
            mRecyclerViewBottom.setAdapter(mBottomListAdapter);
        }
    }

    public void resign() {
        mPresenter.notifyOpponentAboutResign();
        mPresenter.deleteGameRoom();
        //mPresenter.showLoseDialog();
    }


    private void navigateToMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }


    private User getUserPrefs() {
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_USER, Constants.PREF_USER_DEFAULT_VALUE);
        if (!Objects.equals(json, Constants.PREF_USER_DEFAULT_VALUE)) {
            return gson.fromJson(json, User.class);
        }
        return null;
    }


    private User getOpponentBundleExtras() {
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            shuffledWord = extras.getString("EXTRA_WORD");
            return extras.getParcelable(EXTRA_OPPONENT);
        }
        return null;
    }


    @Override
    public void showRoundFinishedDialog() {
        if (getActivity() != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Round round = new Round(1, "LETTERS", "definition", 10);
            Player user = new Player("You", "LET", 5, 10);
            Player opponent = new Player("Guest123", "LTR", 6, 11);
            GameRoundDialogFragment alertDialog = GameRoundDialogFragment
                    .newInstance(round, user, opponent);
            alertDialog.show(fm, "fragment_dialog_round_finished");
        }
    }

}
