package com.julia.android.worderly.ui.game.view;

import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

class GameCountDownTimer extends CountDownTimer {

    private ProgressBar mProgressBar;
    private TextView mTextProgress;

    GameCountDownTimer(long millisInFuture, long countDownInterval, ProgressBar progressBar, TextView textProgress) {
        super(millisInFuture, countDownInterval);
        mProgressBar = progressBar;
        mTextProgress = textProgress;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int progress = (int) (millisUntilFinished / 1000);
        if (mProgressBar != null && mTextProgress != null) {
            int p = mProgressBar.getMax() - progress;
            mProgressBar.setProgress(p);
            mTextProgress.setText(String.valueOf(progress));
        }
    }

    @Override
    public void onFinish() {
        //finish();
    }
}
