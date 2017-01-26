package com.julia.android.worderly.ui.game.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.data.database.WordContract;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.network.WordRequest;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.utils.Constants;
import com.julia.android.worderly.utils.WordUtility;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;
import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;

public class GameFragment extends Fragment implements GamePresenter.View,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = GameFragment.class.getSimpleName();
    private static final int CURSOR_LOADER_ID = 0;
    @BindView(R.id.text_current_user) TextView mCurrentUsernameTextView;
    @BindView(R.id.text_score_current_user) TextView mScoreCurrentUserTextView;
    @BindView(R.id.text_username_opponent) TextView mOpponentUsernameTextView;
    @BindView(R.id.text_word_current) TextView mWordCurrentTextView;
    @BindView(R.id.text_word_opponent) TextView mWordOpponentTextView;
    @BindView(R.id.edit_word) EditText mWordEditText;
    @BindView(R.id.button_send_word) Button mSendWordButton;
    private Unbinder mUnbinder;
    private GamePresenter mPresenter;
    private SharedPreferences mPrefs;
    private boolean isFirstTime;

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach CALLED");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate CALLED");
        super.onCreate(savedInstanceState);
        mPresenter = new GamePresenter(this);
        mPrefs = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        getUserPrefs();
        getOpponentBundleExtras();
        getOpponentBundleExtras();
//        isFirstTime = mPrefs.getBoolean("FIRST_TIME7", false);
//        if(!isFirstTime) {
//            Log.d(TAG, "FIRST TIME");
        // Fetching word from API
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        new WordRequest(requestQueue, getContext(), mPresenter);
//            SharedPreferences.Editor editor = mPrefs.edit();
//            editor.putBoolean("FIRST_TIME7", true);
//            editor.apply();
//        } else {
//            Log.d(TAG, "SECOND TIME");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView CALLED");
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter.setCurrentUserView();
        mPresenter.setOpponentUserView();
        return view;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart CALLED");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume CALLED");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause CALLED");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop CALLED");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView CALLED");
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy CALLED");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach CALLED");
        super.onDetach();
        mPresenter.onDetach();
//        getContext().getContentResolver().delete(WordContract.WordEntry.CONTENT_URI, null, null);
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
    public void showCurrentWordView(String word) {
        mWordCurrentTextView.setText(word);
    }

    @Override
    public void showOpponentWordView(String word) {
        mWordOpponentTextView.setText(word);
    }

    @Override
    public void initLoader() {
        Log.d(TAG, "LOADER WAS INITIALIZED!!!");
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(), WordContract.WordEntry.CONTENT_URI, null, null, null, null);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        String word = data.getString(data.getColumnIndex(WordContract.WordEntry.COLUMN_WORD));
        mPresenter.setCurrentWord(word);
        String scrambledWord = mPrefs.getString("SCRAMBLED_WORD", "");
        if (Objects.equals(scrambledWord, ""))  {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("SCRAMBLED_WORD", WordUtility.scrambleWord(word));
            editor.apply();
        }
        showCurrentWordView(scrambledWord);
    }

    // Reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
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
