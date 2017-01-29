package com.julia.android.worderly.ui.game.interactor;

public interface GameInteractor {
    void notifyOpponentUserWin(String currentUserId, String opponentUserId);
    void listenOpponentUserWin(String currentUserId, String opponentUserId);
    void deleteGameRoom(String currentUserId, String opponentUserId);
}
