package com.julia.android.worderly.ui.game.presenter;

import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.game.interactor.GameInteractor;
import com.julia.android.worderly.ui.game.interactor.GameInteractorImpl;
import com.julia.android.worderly.ui.game.view.GameView;

public class GamePresenterImpl implements GamePresenter {

    private final GameInteractor mInteractor;
    private GameView mGameView;
    private User mCurrentUser;
    private User mOpponentUser;

    public GamePresenterImpl(GameView gameView) {
        this.mGameView = gameView;
        mInteractor = new GameInteractorImpl(this);
    }

    @Override
    public void onStart() {
        mGameView.addCurrentUserView(mCurrentUser.getUsername());
        mGameView.addOpponentUserView(mOpponentUser.getUsername());
    }

    @Override
    public void onDestroy() {
        mGameView = null;
    }

    @Override
    public void setUserFromJson(User user) {
        this.mCurrentUser = user;
        //mInteractor.removeUserFromOnlineUsers(mCurrentUser.getId());
    }

    @Override
    public void setUserFromBundle(String id, String username, String email, String photoUrl) {
        mOpponentUser = new User(id, username, email, photoUrl);
    }

    @Override
    public void onChatButtonClick() {
        mGameView.navigateToChatActivity(mOpponentUser.getId(), mOpponentUser.getUsername());
    }

    public GameView getGameView() {
        return mGameView;
    }
}
