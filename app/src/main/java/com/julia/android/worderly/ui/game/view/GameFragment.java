package com.julia.android.worderly.ui.game.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.data.database.WordContract;
import com.julia.android.worderly.data.database.WordContract.WordEntry;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.game.adapter.CustomList;
import com.julia.android.worderly.ui.game.adapter.WordListAdapter;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.ui.main.view.MainActivity;
import com.julia.android.worderly.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;
import static com.julia.android.worderly.utils.Constants.ACTION_DATA_UPDATED;
import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;
import static com.julia.android.worderly.utils.Constants.PREF_WORDS_FOR_LEARNING;

public class GameFragment extends Fragment implements GamePresenter.View,
        LoaderManager.LoaderCallbacks<Cursor>, Listener {

    // Constants for logging and referring to a unique loader
    private static final String TAG = GameFragment.class.getSimpleName();
    private static final int CURSOR_LOADER_ID = 0;

    // These indices must match the projection
    private static final int INDEX_WORD = 1;
    private static final int INDEX_SCRAMBLED_WORD = 2;
    private static final int INDEX_DEFINITION = 3;

    // Member variables for binding views using ButterKnife
    @BindView(R.id.text_current_user) TextView mCurrentUsernameTextView;
    @BindView(R.id.text_username_opponent) TextView mOpponentUsernameTextView;
    @BindView(R.id.recycler_view_top) RecyclerView mRecyclerViewTop;
    @BindView(R.id.recycler_view_bottom) RecyclerView mRecyclerViewBottom;
    @BindView(R.id.frame_top) FrameLayout mFrameTop;
    @BindView(R.id.frame_bottom) FrameLayout mFrameBottom;
    @BindView(R.id.image_holder) ImageView mImageHolder;
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

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManagerTop = new LinearLayoutManager(getContext());
        layoutManagerTop.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewTop.setLayoutManager(layoutManagerTop);
        List<CustomList> customList1 = new ArrayList<>();

        WordListAdapter mTopListAdapter = new WordListAdapter(customList1, this);
        mRecyclerViewTop.setAdapter(mTopListAdapter);
        mRecyclerViewTop.setOnDragListener(mTopListAdapter.getDragInstance());
        mFrameTop.setOnDragListener(mTopListAdapter.getDragInstance());

        LinearLayoutManager layoutManagerBottom = new LinearLayoutManager(getContext());
        layoutManagerBottom.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewBottom.setLayoutManager(layoutManagerBottom);

        List<CustomList> customList2 = new ArrayList<>();
        customList2.add(0, new CustomList("A", R.color.letter0));
        customList2.add(1, new CustomList("B", R.color.letter1));
        customList2.add(2, new CustomList("C", R.color.letter2));
        customList2.add(3, new CustomList("D", R.color.letter3));
        customList2.add(4, new CustomList("E", R.color.letter4));
        customList2.add(5, new CustomList("F", R.color.letter5));
        customList2.add(6, new CustomList("G", R.color.letter6));

        WordListAdapter mBottomListAdapter = new WordListAdapter(customList2, this);
        mRecyclerViewBottom.setAdapter(mBottomListAdapter);
        mRecyclerViewBottom.setOnDragListener(mBottomListAdapter.getDragInstance());

        mFrameBottom.setOnDragListener(mBottomListAdapter.getDragInstance());
        mImageHolder.setOnDragListener(mTopListAdapter.getDragInstance());
        mPresenter.setCurrentUserView();
        mPresenter.setOpponentUserView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void showCurrentUsernameView(String username) {
        mCurrentUsernameTextView.setText(username);
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
    public void showEndGameDialog(boolean isWon, String word) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if (isWon) {
                builder.setTitle(getString(R.string.title_you_won));
                builder.setMessage(getString(R.string.msg_good_game));
            } else {
                builder.setTitle(getString(R.string.title_you_lose));
                builder.setMessage(getString(R.string.msg_opponent_faster));
                builder.setMessage(getString(R.string.msg_word_was, word));
                String prefs = mPrefs.getString(PREF_WORDS_FOR_LEARNING, Constants.PREF_USER_DEFAULT_VALUE);
                if (!Objects.equals(prefs, Constants.PREF_USER_DEFAULT_VALUE)) {
                    mPrefs.edit().putString(PREF_WORDS_FOR_LEARNING, prefs + word + ",").apply();
                } else {
                    mPrefs.edit().putString(PREF_WORDS_FOR_LEARNING, word + ",").apply();
                }
                // Updating the widget
                updateWidget();
            }

            builder.setPositiveButton(getString(R.string.action_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteWord();
                            navigateToMainActivity();
                        }
                    });
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    private void updateWidget() {
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(getContext().getPackageName());
        getContext().sendBroadcast(dataUpdatedIntent);
    }

    /**
     * This loader will return words data as a Cursor or null if an error occurs.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), WordEntry.CONTENT_URI, null, null, null, null);
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            String word = data.getString(INDEX_WORD);
            String scrambledWord = data.getString(INDEX_SCRAMBLED_WORD);
            String definition = data.getString(INDEX_DEFINITION);
            mPresenter.setWord(word);
            mPresenter.setScrambledWord(scrambledWord);
            mPresenter.setDefinition(definition);
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
    }

    /**
     * Called when the user inputs a text in the text editor.
     * The user is able to send the word only when the length of the word equals 7.
     *
     */
//    @OnTextChanged(value = R.id.edit_word)
//    void onWordInput(Editable editable) {
//        int wordLength = editable.toString().trim().length();
//        if (wordLength == Constants.NUMBER_OF_LETTERS
//                || wordLength == Constants.NUMBER_OF_LETTERS + 1) {
//            mSendWordButton.setEnabled(true);
//        } else {
//            mSendWordButton.setEnabled(false);
//        }
//    }

//    @OnClick(R.id.button_send_word)
//    public void onClick() {
//        mPresenter.onSendWordClick(mWordEditText.getText().toString());
//    }

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
        }
    }

    @Override
    public void setEmptyListTop(boolean visibility) {
        mImageHolder.setVisibility(visibility ? View.VISIBLE : View.GONE);
        mFrameTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
        mRecyclerViewTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setEmptyListBottom(boolean visibility) {

    }
}
