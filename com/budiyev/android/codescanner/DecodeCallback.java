package com.budiyev.android.codescanner;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import com.google.zxing.Result;

public interface DecodeCallback {
    @WorkerThread
    void onDecoded(@NonNull Result result);
}
