package com.julia.android.worderly.ui.randomopponent.presenter;

import com.julia.android.worderly.model.User;
import com.julia.android.worderly.ui.randomopponent.interactor.RandomOpponentInteractor;
import com.julia.android.worderly.ui.randomopponent.interactor.RandomOpponentInteractorImpl;
import com.julia.android.worderly.ui.randomopponent.view.RandomOpponentView;

public class RandomOpponentPresenterImpl implements RandomOpponentPresenter {

    private final RandomOpponentInteractor mInteractor;
    private RandomOpponentView mRandomOpponentView;

    public RandomOpponentPresenterImpl(RandomOpponentView randomOpponentView) {
        this.mRandomOpponentView = randomOpponentView;
        this.mInteractor = new RandomOpponentInteractorImpl();
    }

    @Override
    public void addUserToOnlineUsers(User user){
        mInteractor.addUser(user);
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
