package com.julia.android.worderly.ui.game.interactor;

import com.julia.android.worderly.model.Move;

public interface GameInteractor {
    void notifyOpponentUserWin(String currentUserId, String opponentUserId);
    void listenOpponentUserWin(String currentUserId, String opponentUserId);
    void deleteGameRoom(String currentUserId, String opponentUserId);
    void listenOpponentUserResign(String currentUserId, String opponentUserId);
    void notifyOpponentAboutResign(String currentUserId, String opponentUserId);
    void notifyOpponentAboutWordAndScore(String currentUserId, String opponentUserId, Move move);
    void listenOpponentWordAndScore(String currentUserId, String opponentUserId);
}
