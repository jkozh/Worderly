package com.julia.android.worderly.ui.game.view;

public interface GameView {
    void addCurrentUserView(String username);
    void addOpponentUserView(String username);
    void setWordView(String word);
    void navigateToChatActivity(String opponentUid, String opponentUsername);
    void startCountDown();
    void showRoundFinishDialog(int roundNumber, String word);
    void setScoreView(int score);
    void clearWordInput();
    void setTitleText(String username);
    void changeChatIcon();
}
