package com.julia.android.worderly.ui.search.view;

import com.julia.android.worderly.model.User;

public interface SearchOpponentView {
    void addOpponentFoundView(String username, String photoUrl);
    void navigateToGameActivity(User opponentUser);
}
