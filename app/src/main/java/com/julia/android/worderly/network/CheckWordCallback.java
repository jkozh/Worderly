package com.julia.android.worderly.network;

public interface CheckWordCallback {
    void onSuccess(String definition);

    void onFail();
}
