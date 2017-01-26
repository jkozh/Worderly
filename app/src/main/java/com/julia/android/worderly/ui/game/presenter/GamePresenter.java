package com.julia.android.worderly.ui.game.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.game.interactor.GameInteractor;
import com.julia.android.worderly.ui.game.interactor.GameInteractorImpl;

import java.lang.ref.WeakReference;

public class GamePresenter {

    private WeakReference<GamePresenter.View> mWeakView;
    private GameInteractor mInteractor;
    private User mCurrentUser;
    private User mOpponentUser;
    private String mCurrentWord;
    private String mScrambledCurrentWord;

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

//    private void setWordView() {
//        GamePresenter.View view = mWeakView.get();
//        if (view != null) {
//            view.showWordView(mWord);
//            Log.d("GAME PRESENTER", "WORD:"+mWord);
//        }
//    }

    public void onDetach() {
//        mWeakView = null;
    }

    public void addWordFromRequest(String word) {
        //mWord = word;
        //Log.d("GAME PRESENTER", "mWORD:"+mWord);
        //setWordView();
    }

    @Nullable
    private GamePresenter.View getView() {
        if (mWeakView == null) {
            return null;
        }
        return mWeakView.get();
    }

    public void setCurrentWord(String word) {
        mCurrentWord = word;
        GamePresenter.View view = mWeakView.get();
        if (view != null) {

            // TODO: If the word was scrambled, then do not scramble it again
            // i.e. after screen rotation

            //mScrambledCurrentWord = scrambleWord(mCurrentWord);

            //view.showCurrentWordView(mScrambledCurrentWord);
            Log.d("GAME PRESENTER", "setCurrentWord mCurrentWORD:" + mCurrentWord);
            //Log.d("GAME PRESENTER", "mScrambledCurrentWord:" + mScrambledCurrentWord);
        }
    }

    public void initLoader() {
        GamePresenter.View view = mWeakView.get();
        if (view != null) {
            view.initLoader();
            Log.d("GAME PRESENTER", "Loader was INITIALIZED!!!!");
        }
    }

    public interface View {
        void showCurrentUsernameView(String username);
        void showOpponentUsernameView(String username);
        void showOpponentWordView(String word);
        void showCurrentWordView(String word);
        void initLoader();
    }
}
