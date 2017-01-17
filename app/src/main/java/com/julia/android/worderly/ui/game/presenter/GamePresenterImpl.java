package com.julia.android.worderly.ui.game.presenter;

import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.game.interactor.GameInteractor;
import com.julia.android.worderly.ui.game.interactor.GameInteractorImpl;
import com.julia.android.worderly.ui.game.view.GameView;

import java.util.Objects;
import java.util.Random;

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
    }



    @Override
    public void onStart() {
        mGameView.addCurrentUserView(mCurrentUser.getUsername());
        mGameView.addOpponentUserView(mOpponentUser.getUsername());
        mGameView.startCountDown();
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

    private String scramble(String word) {
        char w[] = word.toCharArray();
        // Scramble the letters using the standard Fisher-Yates shuffle
        for(int i = 0; i < w.length - 1; i++) {
            int j = new Random().nextInt(w.length - 1);
            char temp = w[i];
            w[i] = w[j];
            w[j] = temp;
        }
        return new String(w);
    }

    @Override
    public void onSendWordButtonClick(String word) {
        if (Objects.equals(mWord, word)) {
            mScore = word.length();
            mGameView.setScoreView(mScore);
        }
    }

    @Override
    public void setWord(String word) {
        mInteractor.addWord(word, mCurrentUser.getId(), mOpponentUser.getId());
        mInteractor.getWord(mCurrentUser.getId(), mOpponentUser.getId());
    }

    public GameView getGameView() {
        return mGameView;
    }

}
