package com.julia.android.worderly.ui.game.view;

public interface GameView {
    void addCurrentUserView(String username);
    void addOpponentUserView(String username);
    void navigateToChatActivity(String opponentUid, String opponentUsername);
}
