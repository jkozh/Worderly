package com.julia.android.worderly.ui.search;

import com.julia.android.worderly.model.User;

import java.lang.ref.WeakReference;


public class SearchOpponentPresenter {

    private final SearchOpponentInteractor mInteractor;
    private WeakReference<SearchOpponentPresenter.View> mWeakView;
    private User mOpponentUser;
    private User mCurrentUser;


    public SearchOpponentPresenter(SearchOpponentPresenter.View v) {
        this.mWeakView = new WeakReference<>(v);
        this.mInteractor = new SearchOpponentInteractorImpl(this);
        SearchOpponentPresenter.View view = mWeakView.get();
        if (view != null) {
            mCurrentUser = view.getUserFromPrefs();
        }
    }


    public void addUserToOnlineUsers(User user){
        mInteractor.addUser(user);
    }


    public void removeUserFromOnlineUsers(String uid) {
        mInteractor.removeUser(uid);
    }


    public void searchForOpponent(User user) {
        mInteractor.searchForOpponent(user);
    }


    public void sendOpponentUser(User opponent) {
        this.mOpponentUser = opponent;
        SearchOpponentPresenter.View view = mWeakView.get();
        if (view != null) {
            view.addOpponentFoundView(mOpponentUser.getUsername(), mOpponentUser.getPhotoUrl());
            mInteractor.createCurrentGameRoom(mCurrentUser.getId(), mOpponentUser.getId());
        }
    }


    public void startGame() {
        SearchOpponentPresenter.View view = mWeakView.get();
        if (view != null) {
            view.navigateToGameActivity(mOpponentUser);
        }
    }


    public void onStart() {
        if (mCurrentUser != null) {
            addUserToOnlineUsers(mCurrentUser);
            searchForOpponent(mCurrentUser);
        }
    }


    public void onDestroy() {
        if (mCurrentUser != null) {
            removeUserFromOnlineUsers(mCurrentUser.getId());
        }
    }


    public interface View {

        void addOpponentFoundView(String username, String photoUrl);

        void navigateToGameActivity(User opponentUser);

        User getUserFromPrefs();
    }

}
