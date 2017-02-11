package com.julia.android.worderly.ui.game.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

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

    public void setCurrentUserView() {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            view.showCurrentUsernameView(mCurrentUser.getUsername());
        }
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
    }

    public void setScrambledWord(String scrambledWord) {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            Log.d("GAME PRESENTER", "setScrambledWord scrambledWord:" + scrambledWord);
            view.showWordView(scrambledWord);
        }
    }

    public void setDefinition(String definition) {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            Log.d("GAME PRESENTER", "setDefinition definition:" + definition);
            view.showDefinitionView(definition);
        }
    }

    public void onSendWordClick(String sendWord) {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            if (Objects.equals(sendWord.toUpperCase(), mWord.toUpperCase())) {
                mInteractor.notifyOpponentUserWin(mCurrentUser.getId(), mOpponentUser.getId());
                // Clean the database from finished game room
                mInteractor.deleteGameRoom(mCurrentUser.getId(), mOpponentUser.getId());
                view.showRoundFinishedDialog(true, mWord);
            } else {
                view.showWrongWordToast();
            }
        }
    }

    public void showLoseDialog() {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            deleteGameRoom();
            view.showRoundFinishedDialog(false, mWord);
        }
    }

    public void showWinDialog() {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            deleteGameRoom();
            view.showRoundFinishedDialog(true, mWord);
        }
    }

    public void deleteGameRoom() {
        // Clean the database from finished game room
        mInteractor.deleteGameRoom(mCurrentUser.getId(), mOpponentUser.getId());
    }

    public void notifyOpponentAboutResign() {
        mInteractor.notifyOpponentAboutResign(mCurrentUser.getId(), mOpponentUser.getId());
    }

    @Nullable
    private GamePresenter.View getView() {
        if (mWeakView == null) {
            return null;
        }
        return mWeakView.get();
    }

    public interface View {
        void showCurrentUsernameView(String username);
        void showOpponentUsernameView(String username);
        void showWordView(String word);
        void showDefinitionView(String definition);
        void showWrongWordToast();
        void showRoundFinishedDialog(boolean isWon, String word);
    }
}
