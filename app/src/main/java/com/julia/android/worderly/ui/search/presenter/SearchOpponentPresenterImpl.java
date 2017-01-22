package com.julia.android.worderly.ui.search.presenter;

import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.search.interactor.SearchOpponentInteractor;
import com.julia.android.worderly.ui.search.interactor.SearchOpponentInteractorImpl;
import com.julia.android.worderly.ui.search.view.SearchOpponentView;

public class SearchOpponentPresenterImpl implements SearchOpponentPresenter {

    private final SearchOpponentInteractor mInteractor;
    private SearchOpponentView mSearchOpponentView;
    private User mOpponentUser;
    private User mCurrentUser;

    public SearchOpponentPresenterImpl(SearchOpponentView randomOpponentView) {
        this.mSearchOpponentView = randomOpponentView;
        this.mInteractor = new SearchOpponentInteractorImpl(this);
    }

    @Override
    public void addUserToOnlineUsers(User user){
        mInteractor.addUser(user);
    }

    @Override
    public void removeUserFromOnlineUsers(String uid) {
        mInteractor.removeUser(uid);
    }

    @Override
    public void searchForOpponent(User user) {
        mInteractor.searchForOpponent(user);
    }

    @Override
    public void sendOpponentUser(User opponentUser) {
        this.mOpponentUser = opponentUser;
        mSearchOpponentView.addOpponentFoundView(
                mOpponentUser.getId(),
                mOpponentUser.getUsername(),
                mOpponentUser.getPhotoUrl());
        mInteractor.createCurrentGameRoom(mCurrentUser.getId(), mOpponentUser.getId());
    }

    @Override
    public void setUserFromJson(User user) {
        this.mCurrentUser = user;
    }

    @Override
    public void startGame() {
        mSearchOpponentView.navigateToGameActivity(mOpponentUser);
    }

    @Override
    public void onStart() {
        if (mCurrentUser != null) {
            addUserToOnlineUsers(mCurrentUser);
            searchForOpponent(mCurrentUser);
        }
    }

    @Override
    public void onDestroy() {
        if (mCurrentUser != null) {
            removeUserFromOnlineUsers(mCurrentUser.getId());
        }
        mSearchOpponentView = null;
    }

    public SearchOpponentView getRandomOpponentView() {
        return mSearchOpponentView;
    }
}
