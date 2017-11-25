package com.budiyev.android.codescanner;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

final class DecodeTask {
    private final DecodeCallback mCallback;
    private final byte[] mData;
    private final int mDataHeight;
    private final int mDataWidth;
    private final int mFrameHeight;
    private final int mFrameWidth;
    private final int mOrientation;
    private final boolean mReverseHorizontal;
    private final boolean mSquareFrame;

    public DecodeTask(@NonNull byte[] data, int dataWidth, int dataHeight, int frameWidth, int frameHeight, int orientation, boolean squareFrame, boolean reverseHorizontal, @NonNull DecodeCallback callback) {
        this.mData = data;
        this.mDataWidth = dataWidth;
        this.mDataHeight = dataHeight;
        this.mFrameWidth = frameWidth;
        this.mFrameHeight = frameHeight;
        this.mOrientation = orientation;
        this.mSquareFrame = squareFrame;
        this.mReverseHorizontal = reverseHorizontal;
        this.mCallback = callback;
    }

    @NonNull
    public DecodeCallback getCallback() {
        return this.mCallback;
    }

    @NonNull
    public Result decode(@NonNull MultiFormatReader reader) throws ReaderException {
        byte[] data;
        int dataWidth;
        int dataHeight;
        if (this.mOrientation == 0) {
            data = this.mData;
            dataWidth = this.mDataWidth;
            dataHeight = this.mDataHeight;
        } else {
            data = Utils.rotateNV21(this.mData, this.mDataWidth, this.mDataHeight, this.mOrientation);
            if (this.mOrientation == 90 || this.mOrientation == 270) {
                dataWidth = this.mDataHeight;
                dataHeight = this.mDataWidth;
            } else {
                dataWidth = this.mDataWidth;
                dataHeight = this.mDataHeight;
            }
        }
        Rect frameRect = Utils.getImageFrameRect(this.mSquareFrame, dataWidth, dataHeight, this.mFrameWidth, this.mFrameHeight);
        return reader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new PlanarYUVLuminanceSource(data, dataWidth, dataHeight, frameRect.left, frameRect.top, frameRect.width(), frameRect.height(), this.mReverseHorizontal))));
    }
}
