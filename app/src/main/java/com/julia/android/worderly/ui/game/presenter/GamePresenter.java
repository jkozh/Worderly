package com.julia.android.worderly.ui.game.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.julia.android.worderly.model.Move;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.network.CheckWordCallback;
import com.julia.android.worderly.network.CheckWordRequest;
import com.julia.android.worderly.ui.game.interactor.GameInteractor;
import com.julia.android.worderly.ui.game.interactor.GameInteractorImpl;
import com.julia.android.worderly.utils.WordUtility;

import java.lang.ref.WeakReference;
import java.util.Objects;


public class GamePresenter {

    private WeakReference<GamePresenter.View> mWeakView;
    private GameInteractor mInteractor;
    private User mCurrentUser;
    private User mOpponentUser;
    private String mWord;


    public GamePresenter(GamePresenter.View view) {
        mWeakView = new WeakReference<>(view);
        mInteractor = new GameInteractorImpl(this);
    }


    public void setUserFromJson(User user) {
        mCurrentUser = user;
    }


    public void setOpponentFromBundle(User opponent) {
        mOpponentUser = opponent;
    }


    public void setOpponentUserView() {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            view.showOpponentUsernameView(mOpponentUser.getUsername());
        }
    }


    public void setWord(String word) {
        mWord = word;
        Log.d("GAME PRESENTER", "setWord mWord:" + mWord);
        mInteractor.listenOpponentUserWin(mCurrentUser.getId(), mOpponentUser.getId());
        mInteractor.listenOpponentUserResign(mCurrentUser.getId(), mOpponentUser.getId());
        mInteractor.listenOpponentWordAndScore(mCurrentUser.getId(), mOpponentUser.getId());
    }


    public void deleteGameRoom() {
        // Clean the database from finished game room
        mInteractor.deleteGameRoom(mCurrentUser.getId(), mOpponentUser.getId());
    }


    public void notifyOpponentAboutResign() {
        mInteractor.notifyOpponentAboutResign(mCurrentUser.getId(), mOpponentUser.getId());
    }


    public boolean isWordsEquals(String word) {
        return Objects.equals(word, mWord);
    }


    public void sendUserScoreAndWord(Move move) {
        mInteractor.notifyOpponentAboutWordAndScore(mCurrentUser.getId(), mOpponentUser.getId(), move);
    }


    public void setOpponentScore(String score) {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            view.showOpponentScore(score);
        }
    }


    public void getVolleyRequest(final Context context, final String word) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        new CheckWordRequest(requestQueue, word, new CheckWordCallback() {
            @Override
            public void onSuccess(String definition) {
                if (definition != null) {
                    Toast.makeText(context, definition, Toast.LENGTH_SHORT).show();
                    // check if new word score not less than old one
                    int wordValue = WordUtility.getWordValue(word);
                    GamePresenter.View view = mWeakView.get();
                    if (view != null) {
                        if (Integer.parseInt(view.getUserScoreText()) < wordValue) {
                            view.setUserScoreTextView(String.valueOf(wordValue));
                            sendUserScoreAndWord(new Move(word, String.valueOf(wordValue)));
                        }
                    }
                }
            }

            @Override
            public void onFail() {
                Toast.makeText(context, "WRONG WORD", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface View {

        void showOpponentUsernameView(String username);

        void showWrongWordToast();

        void showRoundFinishedDialog();

        void showOpponentScore(String score);

        String getUserScoreText();

        void setUserScoreTextView(String score);

    }

}
