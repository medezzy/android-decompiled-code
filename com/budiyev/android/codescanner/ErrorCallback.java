package com.budiyev.android.codescanner;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import com.budiyev.android.codescanner.Utils.SuppressErrorCallback;

public interface ErrorCallback {
    public static final ErrorCallback SUPPRESS = new SuppressErrorCallback();

    @WorkerThread
    void onError(@NonNull Exception exception);
}
