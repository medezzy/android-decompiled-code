package com.budiyev.android.codescanner;

import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.support.annotation.NonNull;

final class DecoderWrapper {
    private final boolean mAutoFocusSupported;
    private final Camera mCamera;
    private final CameraInfo mCameraInfo;
    private final Decoder mDecoder;
    private final int mDisplayOrientation;
    private final boolean mFlashSupported;
    private final Point mFrameSize;
    private final Point mPreviewSize;

    public DecoderWrapper(@NonNull Camera camera, @NonNull CameraInfo cameraInfo, @NonNull Decoder decoder, @NonNull Point previewSize, @NonNull Point frameSize, int displayOrientation, boolean autoFocusSupported, boolean flashSupported) {
        this.mCamera = camera;
        this.mCameraInfo = cameraInfo;
        this.mDecoder = decoder;
        this.mPreviewSize = previewSize;
        this.mFrameSize = frameSize;
        this.mDisplayOrientation = displayOrientation;
        this.mAutoFocusSupported = autoFocusSupported;
        this.mFlashSupported = flashSupported;
    }

    @NonNull
    public Camera getCamera() {
        return this.mCamera;
    }

    @NonNull
    public CameraInfo getCameraInfo() {
        return this.mCameraInfo;
    }

    @NonNull
    public Decoder getDecoder() {
        return this.mDecoder;
    }

    @NonNull
    public Point getPreviewSize() {
        return this.mPreviewSize;
    }

    @NonNull
    public Point getFrameSize() {
        return this.mFrameSize;
    }

    public int getDisplayOrientation() {
        return this.mDisplayOrientation;
    }

    public boolean isAutoFocusSupported() {
        return this.mAutoFocusSupported;
    }

    public boolean isFlashSupported() {
        return this.mFlashSupported;
    }

    public void release() {
        this.mCamera.release();
        this.mDecoder.shutdown();
    }
}
