package com.julia.android.worderly.ui.game.interactor;

public interface GameInteractor {
    void notifyOpponentUserWin(String currentUserId, String opponentUserId);
    void listenOpponentUserWin(String currentUserId, String opponentUserId);
//    void addWord(String word, String currentUserId, String opponentUserId);
//    void getWord(String currentUserId, String opponentUserId);
}
