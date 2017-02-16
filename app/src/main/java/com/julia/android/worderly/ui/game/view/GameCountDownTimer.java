package com.julia.android.worderly.ui.game.view;

import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

class GameCountDownTimer extends CountDownTimer {

    private ProgressBar mProgressBar;
    private TextView mTextProgress;
    private DialogListener dialogListener;

    GameCountDownTimer(long millisInFuture, long countDownInterval, ProgressBar progressBar,
                       TextView textProgress, DialogListener dialogListener) {
        super(millisInFuture, countDownInterval);
        this.mProgressBar = progressBar;
        this.mTextProgress = textProgress;
        this.dialogListener = dialogListener;
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
        dialogListener.showRoundFinishedDialog();
    }
}
