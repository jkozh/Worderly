package com.julia.android.worderly.ui.game.presenter;

import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.game.interactor.GameInteractor;
import com.julia.android.worderly.ui.game.interactor.GameInteractorImpl;
import com.julia.android.worderly.ui.game.view.GameView;

import java.util.Objects;

import static com.julia.android.worderly.utils.WordUtility.scramble;

public class GamePresenterImpl implements GamePresenter {

    private static final String TAG = GamePresenterImpl.class.getSimpleName();
    private final GameInteractor mInteractor;
    private GameView mGameView;
    private User mCurrentUser;
    private User mOpponentUser;
    private String mWord;
    private int mScore;

    public GamePresenterImpl(GameView gameView) {
        this.mGameView = gameView;
        mInteractor = new GameInteractorImpl(this);
        mGameView.startCountDown();
        mGameView.setScoreView(0);
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
    }

    @Override
    public void setUserFromBundle(String id, String username, String email, String photoUrl) {
        mOpponentUser = new User(id, username, email, photoUrl);
        mGameView.setTitleText(mOpponentUser.getUsername());
    }

    @Override
    public void onChatButtonClick() {
        mGameView.navigateToChatActivity(mOpponentUser.getId(), mOpponentUser.getUsername());
    }

    @Override
    public void setWordView(String word) {
        this.mWord = word;
        mGameView.setWordView(scramble(word));
    }

    @Override
    public void onSendWordButtonClick(String word) {
        if (Objects.equals(mWord, word)) {
            mScore = word.length();
            mGameView.setScoreView(mScore);
            mGameView.clearWordInput();
        }
    }

    @Override
    public void changeChatIcon() {
        mGameView.changeChatIcon();
    }

    @Override
    public void setWord(String word) {
        mInteractor.addWord(word, mCurrentUser.getId(), mOpponentUser.getId());
        mInteractor.getWord(mCurrentUser.getId(), mOpponentUser.getId());
        mInteractor.chatIconChange(mCurrentUser.getId(), mOpponentUser.getId());
    }

    public GameView getGameView() {
        return mGameView;
    }

}
