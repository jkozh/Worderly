package com.julia.android.worderly.ui.game.presenter;

import android.util.Log;

import com.julia.android.worderly.model.Move;
import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.game.interactor.GameInteractor;
import com.julia.android.worderly.ui.game.interactor.GameInteractorImpl;

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

//    public void showLoseDialog() {
//        GamePresenter.View view = mWeakView.get();
//        if (view != null) {
//            deleteGameRoom();
//            view.showRoundFinishedDialog(false, mWord);
//        }
//    }
//
//    public void showWinDialog() {
//        GamePresenter.View view = mWeakView.get();
//        if (view != null) {
//            deleteGameRoom();
//            view.showRoundFinishedDialog(true, mWord);
//        }
//    }

    public void deleteGameRoom() {
        // Clean the database from finished game room
        mInteractor.deleteGameRoom(mCurrentUser.getId(), mOpponentUser.getId());
    }

    public void notifyOpponentAboutResign() {
        mInteractor.notifyOpponentAboutResign(mCurrentUser.getId(), mOpponentUser.getId());
    }

    public void onSendClick(String word) {
        GamePresenter.View view = mWeakView.get();
//        if (view != null) {
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            new CheckWordRequest(requestQueue, word, new CheckWordCallback() {
//                @Override
//                public void onSuccess(String definition) {
//
//                }
//            });
//            if (Objects.equals(word, mWord)) {
//                mInteractor.notifyOpponentUserWin(mCurrentUser.getId(), mOpponentUser.getId());
//                // Clean the database from finished game room
//                mInteractor.deleteGameRoom(mCurrentUser.getId(), mOpponentUser.getId());
//                view.showRoundFinishedDialog(true, mWord);
//            } else {
//                view.showWrongWordToast();
//            }
//        }
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

    public interface View {
        void showOpponentUsernameView(String username);
        void showWrongWordToast();
        void showRoundFinishedDialog();
        void showOpponentScore(String score);
    }
}
