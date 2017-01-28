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

    public void onDetach() {
//        mWeakView = null;
    }

    @Nullable
    private GamePresenter.View getView() {
        if (mWeakView == null) {
            return null;
        }
        return mWeakView.get();
    }

    public void setWord(String word) {
        mWord = word;
        Log.d("GAME PRESENTER", "setWord mWord:" + mWord);
        mInteractor.listenOpponentUserWin(mCurrentUser.getId(), mOpponentUser.getId());
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
        if (Objects.equals(sendWord.toUpperCase(), mWord.toUpperCase())) {
            GamePresenter.View view = mWeakView.get();
            if (view != null) {
                mInteractor.notifyOpponentUserWin(mCurrentUser.getId(), mOpponentUser.getId());
                view.showWinDialog();
            }
        }
    }

    public void showLossDialog() {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            view.showLossDialog(mWord);
        }
    }

    public interface View {
        void showCurrentUsernameView(String username);
        void showOpponentUsernameView(String username);
        void showWordView(String word);
        void showDefinitionView(String definition);
        void showWinDialog();
        void showLossDialog(String word);
    }
}
