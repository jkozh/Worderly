package com.julia.android.worderly.ui.game.presenter;

import android.support.annotation.Nullable;

import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.game.interactor.GameInteractor;
import com.julia.android.worderly.ui.game.interactor.GameInteractorImpl;

import java.lang.ref.WeakReference;

public class GamePresenter {

    private WeakReference<GamePresenter.View> mWeakView;
    private GameInteractor mInteractor;
    private User mCurrentUser;
    private User mOpponentUser;

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

    @Nullable
    private GamePresenter.View getView() {
        if (mWeakView == null) {
            return null;
        }
        return mWeakView.get();
    }

    public void onDetach() {
        mWeakView = null;
    }

    public interface View {
        void showCurrentUsernameView(String username);
        void showOpponentUsernameView(String username);
//    void onStart();
//    void onDestroy();
//    void setUserFromJson(User user);
//    void setUserFromBundle(String id, String username, String email, String photoUrl);
//    void onChatButtonClick();
//    void setWord(String word);
//    void setWordView(String word);
//    void onSendWordButtonClick(String word);
//    void changeChatIcon();
    }
}
