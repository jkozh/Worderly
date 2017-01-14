package com.julia.android.worderly.ui.randomopponent.presenter;

import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.randomopponent.interactor.SearchOpponentInteractor;
import com.julia.android.worderly.ui.randomopponent.interactor.SearchOpponentInteractorImpl;
import com.julia.android.worderly.ui.randomopponent.view.RandomOpponentView;

public class SearchOpponentPresenterImpl implements SearchOpponentPresenter {

    private final SearchOpponentInteractor mInteractor;
    private RandomOpponentView mRandomOpponentView;
    private User mOpponentUser;

    public SearchOpponentPresenterImpl(RandomOpponentView randomOpponentView) {
        this.mRandomOpponentView = randomOpponentView;
        this.mInteractor = new SearchOpponentInteractorImpl(this);
    }

    @Override
    public void addUserToOnlineUsers(User user){
        mInteractor.addUser(user);
    }

    @Override
    public void removeUserFromOnlineUsers(User user) {
        mInteractor.removeUser(user);
    }

    @Override
    public void searchForOpponent(User user) {
        mInteractor.searchForOpponent(user);
    }

    @Override
    public void sendOpponentUser(User opponentUser) {
        this.mOpponentUser = opponentUser;
        mRandomOpponentView.addOpponentView(
                mOpponentUser.getId(),
                mOpponentUser.getUsername(),
                mOpponentUser.getPhotoUrl());
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onDestroy() {
        mRandomOpponentView = null;
    }

    public RandomOpponentView getRandomOpponentView() {
        return mRandomOpponentView;
    }
}
