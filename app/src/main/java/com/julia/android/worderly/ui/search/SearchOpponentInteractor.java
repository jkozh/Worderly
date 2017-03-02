package com.julia.android.worderly.ui.search;

import com.julia.android.worderly.model.User;

public interface SearchOpponentInteractor {
    void addUser(User user);
    void removeUser(String uid);
    void searchForOpponent(User user);
    void createCurrentGameRoom(String currentUserId, String opponentUserId);
    void listenForOpponentGameRoom(String currentUserId, String opponentUserId);
}
