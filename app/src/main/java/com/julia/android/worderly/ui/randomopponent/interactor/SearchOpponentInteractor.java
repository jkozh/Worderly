package com.julia.android.worderly.ui.randomopponent.interactor;

import com.julia.android.worderly.model.User;

public interface SearchOpponentInteractor {
    void addUser(User user);
    void removeUser(User user);
    void searchForOpponent(User user);
}
