package com.julia.android.worderly.ui.game.view;

import android.os.CountDownTimer;

class GameCountDownTimer extends CountDownTimer {

    GameCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
//        int progress = (int) (millisUntilFinished / 1000);
//        if (mProgressBar != null) {
//            mProgressBar.setProgress(mProgressBar.getMax() - progress);
//        }
    }

    @Override
    public void onFinish() {
        //finish();
    }
}
