package com.julia.android.worderly.ui.game.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.julia.android.worderly.R;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.game.presenter.GamePresenter;
import com.julia.android.worderly.utils.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;
import static com.julia.android.worderly.utils.Constants.PREF_NAME;
import static com.julia.android.worderly.utils.Constants.PREF_USER;

public class GameFragment extends Fragment implements GamePresenter.View {

    private static final String TAG = GameFragment.class.getSimpleName();
    @BindView(R.id.text_current_user)
    TextView mCurrentUsernameTextView;
    @BindView(R.id.text_score_current_user)
    TextView mScoreCurrentUserTextView;
    @BindView(R.id.text_username_opponent)
    TextView mOpponentUsernameTextView;
    @BindView(R.id.text_word)
    TextView mWordTextView;
    @BindView(R.id.text_countdown)
    TextView mCountDownTextView;
    @BindView(R.id.edit_word)
    EditText mWordEditText;
    @BindView(R.id.button_send_word)
    Button mSendWordButton;
    boolean isFirstTime;
    private Unbinder mUnbinder;
    private GamePresenter mPresenter;

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
        getUserPrefs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView CALLED");
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter.setCurrentUserView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated CALLED");
        super.onActivityCreated(savedInstanceState);
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
    }

    @Override
    public void showCurrentUsernameView(String username) {
        mCurrentUsernameTextView.setText(username);
    }

    @Override
    public void setOpponentUsernameView(String username) {
        mOpponentUsernameTextView.setText(username);
    }

    private void getUserPrefs() {
        Log.d(TAG, "SHARED PREFS CALLED");
        SharedPreferences prefs = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isFirstTime = prefs.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            Log.d(TAG, "FIRST TIME");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();

            Gson gson = new Gson();
            String json = prefs.getString(PREF_USER, Constants.PREF_USER_DEFAULT_VALUE);
            if (!Objects.equals(json, Constants.PREF_USER_DEFAULT_VALUE)) {
                User user = gson.fromJson(json, User.class);
                mPresenter.setUserFromJson(user);
            }
        } else {
            Log.d(TAG, "SECOND TIME");
        }
    }
}
