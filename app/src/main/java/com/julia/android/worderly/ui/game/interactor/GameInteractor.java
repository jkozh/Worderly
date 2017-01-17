package com.julia.android.worderly.ui.game.interactor;

public interface GameInteractor {
    void addWord(String word, String currentUserId, String opponentUserId);
    void getWord(String currentUserId, String opponentUserId);
}
