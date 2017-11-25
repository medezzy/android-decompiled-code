package com.budiyev.android.codescanner;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import com.budiyev.android.codescanner.Decoder.StateListener;
import com.google.zxing.BarcodeFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class CodeScanner {
    public static final List<BarcodeFormat> ALL_FORMATS = Arrays.asList(new BarcodeFormat[]{BarcodeFormat.AZTEC, BarcodeFormat.CODABAR, BarcodeFormat.CODE_39, BarcodeFormat.CODE_93, BarcodeFormat.CODE_128, BarcodeFormat.DATA_MATRIX, BarcodeFormat.EAN_8, BarcodeFormat.EAN_13, BarcodeFormat.ITF, BarcodeFormat.MAXICODE, BarcodeFormat.PDF_417, BarcodeFormat.QR_CODE, BarcodeFormat.RSS_14, BarcodeFormat.RSS_EXPANDED, BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.UPC_EAN_EXTENSION});
    private static final long AUTO_FOCUS_INTERVAL = 1500;
    private static final int FOCUS_ATTEMPTS_THRESHOLD = 2;
    public static final List<BarcodeFormat> ONE_DIMENSIONAL_FORMATS = Arrays.asList(new BarcodeFormat[]{BarcodeFormat.CODABAR, BarcodeFormat.CODE_39, BarcodeFormat.CODE_93, BarcodeFormat.CODE_128, BarcodeFormat.EAN_8, BarcodeFormat.EAN_13, BarcodeFormat.ITF, BarcodeFormat.RSS_14, BarcodeFormat.RSS_EXPANDED, BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.UPC_EAN_EXTENSION});
    public static final List<BarcodeFormat> TWO_DIMENSIONAL_FORMATS = Arrays.asList(new BarcodeFormat[]{BarcodeFormat.AZTEC, BarcodeFormat.DATA_MATRIX, BarcodeFormat.MAXICODE, BarcodeFormat.PDF_417, BarcodeFormat.QR_CODE});
    private static final int UNSPECIFIED = -1;
    private final android.hardware.Camera.AutoFocusCallback mAutoFocusCallback;
    private volatile boolean mAutoFocusEnabled;
    private final Runnable mAutoFocusTask;
    private final int mCameraId;
    private final Context mContext;
    private volatile DecodeCallback mDecodeCallback;
    private final DecoderStateListener mDecoderStateListener;
    private volatile DecoderWrapper mDecoderWrapper;
    private volatile ErrorCallback mErrorCallback;
    private volatile boolean mFlashEnabled;
    private int mFocusAttemptsCount;
    private boolean mFocusing;
    private volatile List<BarcodeFormat> mFormats;
    private volatile boolean mInitialization;
    private final Lock mInitializeLock;
    private volatile boolean mInitialized;
    private final Handler mMainThreadHandler;
    private boolean mPreviewActive;
    private final android.hardware.Camera.PreviewCallback mPreviewCallback;
    private final CodeScannerView mScannerView;
    private final Runnable mStopPreviewTask;
    private volatile boolean mStoppingPreview;
    private final Callback mSurfaceCallback;
    private final SurfaceHolder mSurfaceHolder;

    private final class AutoFocusCallback implements android.hardware.Camera.AutoFocusCallback {
        private AutoFocusCallback() {
        }

        public void onAutoFocus(boolean success, Camera camera) {
            CodeScanner.this.mFocusing = false;
        }
    }

    private final class AutoFocusTask implements Runnable {
        private AutoFocusTask() {
        }

        public void run() {
            CodeScanner.this.autoFocusCamera();
        }
    }

    public static final class Builder {
        private boolean mAutoFocusEnabled;
        private int mCameraId;
        private DecodeCallback mDecodeCallback;
        private ErrorCallback mErrorCallback;
        private boolean mFlashEnabled;
        private List<BarcodeFormat> mFormats;

        private Builder() {
            this.mCameraId = -1;
            this.mFormats = CodeScanner.ALL_FORMATS;
            this.mAutoFocusEnabled = true;
        }

        @NonNull
        public Builder camera(int cameraId) {
            this.mCameraId = cameraId;
            return this;
        }

        @NonNull
        public Builder formats(@NonNull BarcodeFormat... formats) {
            this.mFormats = Arrays.asList(formats);
            return this;
        }

        @NonNull
        public Builder formats(@NonNull List<BarcodeFormat> formats) {
            this.mFormats = formats;
            return this;
        }

        @NonNull
        public Builder onDecoded(@Nullable DecodeCallback callback) {
            this.mDecodeCallback = callback;
            return this;
        }

        @NonNull
        public Builder onError(@Nullable ErrorCallback callback) {
            this.mErrorCallback = callback;
            return this;
        }

        @NonNull
        public Builder autoFocus(boolean enabled) {
            this.mAutoFocusEnabled = enabled;
            return this;
        }

        @NonNull
        public Builder flash(boolean enabled) {
            this.mFlashEnabled = enabled;
            return this;
        }

        @NonNull
        public CodeScanner build(@NonNull Context context, @NonNull CodeScannerView view) {
            CodeScanner scanner = new CodeScanner(context, view, this.mCameraId);
            scanner.mFormats = this.mFormats;
            scanner.mDecodeCallback = this.mDecodeCallback;
            scanner.mErrorCallback = this.mErrorCallback;
            scanner.mAutoFocusEnabled = this.mAutoFocusEnabled;
            scanner.mFlashEnabled = this.mFlashEnabled;
            return scanner;
        }
    }

    private final class FinishInitializationTask implements Runnable {
        private final Point mFrameSize;

        private FinishInitializationTask(@NonNull Point frameSize) {
            this.mFrameSize = frameSize;
        }

        public void run() {
            if (CodeScanner.this.mInitialized) {
                CodeScanner.this.mScannerView.setFrameSize(this.mFrameSize);
                CodeScanner.this.mScannerView.setCodeScanner(CodeScanner.this);
                CodeScanner.this.startPreview();
            }
        }
    }

    private final class InitializationThread extends Thread {
        private final int mHeight;
        private final int mWidth;

        public InitializationThread(int width, int height) {
            super("Code scanner initialization thread");
            if (getPriority() != 5) {
                setPriority(5);
            }
            if (isDaemon()) {
                setDaemon(false);
            }
            this.mWidth = width;
            this.mHeight = height;
        }

        public void run() {
            try {
                initialize();
            } catch (Exception e) {
                CodeScanner.this.releaseResourcesInternal();
                ErrorCallback errorCallback = CodeScanner.this.mErrorCallback;
                if (errorCallback != null) {
                    errorCallback.onError(e);
                    return;
                }
                throw e;
            }
        }

        private void initialize() {
            Camera camera = null;
            CameraInfo cameraInfo = new CameraInfo();
            if (CodeScanner.this.mCameraId == -1) {
                int numberOfCameras = Camera.getNumberOfCameras();
                for (int i = 0; i < numberOfCameras; i++) {
                    Camera.getCameraInfo(i, cameraInfo);
                    if (cameraInfo.facing == 0) {
                        camera = Camera.open(i);
                        break;
                    }
                }
            } else {
                camera = Camera.open(CodeScanner.this.mCameraId);
                Camera.getCameraInfo(CodeScanner.this.mCameraId, cameraInfo);
            }
            if (camera == null) {
                throw new RuntimeException("Unable to access camera");
            }
            Parameters parameters = camera.getParameters();
            if (parameters == null) {
                throw new RuntimeException("Unable to configure camera");
            }
            int orientation = Utils.getDisplayOrientation(CodeScanner.this.mContext, cameraInfo);
            boolean portrait = Utils.isPortrait(orientation);
            Point previewSize = Utils.findSuitablePreviewSize(parameters, portrait ? this.mHeight : this.mWidth, portrait ? this.mWidth : this.mHeight);
            parameters.setPreviewSize(previewSize.x, previewSize.y);
            Point frameSize = Utils.getFrameSize(portrait ? previewSize.y : previewSize.x, portrait ? previewSize.x : previewSize.y, this.mWidth, this.mHeight);
            List<String> focusModes = parameters.getSupportedFocusModes();
            boolean autoFocusSupported = focusModes != null && focusModes.contains("auto");
            if (!autoFocusSupported) {
                CodeScanner.this.mAutoFocusEnabled = false;
            }
            if (autoFocusSupported && CodeScanner.this.mAutoFocusEnabled) {
                parameters.setFocusMode("auto");
            }
            List<String> flashModes = parameters.getSupportedFlashModes();
            boolean flashSupported = flashModes != null && flashModes.contains("torch");
            if (!flashSupported) {
                CodeScanner.this.mFlashEnabled = false;
            }
            camera.setParameters(Utils.optimizeParameters(parameters));
            camera.setDisplayOrientation(orientation);
            CodeScanner.this.mInitializeLock.lock();
            try {
                Decoder decoder = new Decoder(CodeScanner.this.mDecoderStateListener, CodeScanner.this.mFormats);
                CodeScanner.this.mDecoderWrapper = new DecoderWrapper(camera, cameraInfo, decoder, previewSize, frameSize, orientation, autoFocusSupported, flashSupported);
                decoder.start();
                CodeScanner.this.mInitialization = false;
                CodeScanner.this.mInitialized = true;
                CodeScanner.this.mInitializeLock.unlock();
                CodeScanner.this.mMainThreadHandler.post(new FinishInitializationTask(frameSize));
            } catch (Throwable th) {
                CodeScanner.this.mInitializeLock.unlock();
            }
        }
    }

    private final class PreviewCallback implements android.hardware.Camera.PreviewCallback {
        private PreviewCallback() {
        }

        public void onPreviewFrame(byte[] data, Camera camera) {
            boolean z = true;
            if (CodeScanner.this.mInitialized && !CodeScanner.this.mStoppingPreview) {
                DecoderWrapper decoderWrapper = CodeScanner.this.mDecoderWrapper;
                Decoder decoder = decoderWrapper.getDecoder();
                if (!decoder.isProcessing()) {
                    Point previewSize = decoderWrapper.getPreviewSize();
                    Point frameSize = decoderWrapper.getFrameSize();
                    int i = previewSize.x;
                    int i2 = previewSize.y;
                    int i3 = frameSize.x;
                    int i4 = frameSize.y;
                    int displayOrientation = decoderWrapper.getDisplayOrientation();
                    boolean isSquareFrame = CodeScanner.this.mScannerView.isSquareFrame();
                    if (decoderWrapper.getCameraInfo().facing != 1) {
                        z = false;
                    }
                    decoder.decode(data, i, i2, i3, i4, displayOrientation, isSquareFrame, z, CodeScanner.this.mDecodeCallback);
                }
            }
        }
    }

    private final class StopPreviewTask implements Runnable {
        private StopPreviewTask() {
        }

        public void run() {
            CodeScanner.this.stopPreview();
        }
    }

    private final class SurfaceCallback implements Callback {
        private SurfaceCallback() {
        }

        public void surfaceCreated(SurfaceHolder holder) {
            CodeScanner.this.startPreviewInternalSafe();
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (holder.getSurface() == null) {
                CodeScanner.this.mPreviewActive = false;
                return;
            }
            CodeScanner.this.stopPreviewInternalSafe();
            CodeScanner.this.startPreviewInternalSafe();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            CodeScanner.this.stopPreviewInternalSafe();
        }
    }

    private final class DecoderStateListener implements StateListener {
        private DecoderStateListener() {
        }

        public void onStateChanged(int state) {
            if (state == 2) {
                CodeScanner.this.mStoppingPreview = true;
                CodeScanner.this.mMainThreadHandler.post(CodeScanner.this.mStopPreviewTask);
            }
        }
    }

    private final class ScannerLayoutListener implements LayoutListener {
        private ScannerLayoutListener() {
        }

        public void onLayout(int width, int height) {
            CodeScanner.this.initialize(width, height);
            CodeScanner.this.mScannerView.setLayoutListener(null);
        }
    }

    @MainThread
    public CodeScanner(@NonNull Context context, @NonNull CodeScannerView view) {
        this(context, view, -1);
    }

    @MainThread
    public CodeScanner(@NonNull Context context, @NonNull CodeScannerView view, int cameraId) {
        this.mInitializeLock = new ReentrantLock();
        this.mFormats = ALL_FORMATS;
        this.mAutoFocusEnabled = true;
        this.mContext = context;
        this.mScannerView = view;
        this.mSurfaceHolder = view.getPreviewView().getHolder();
        this.mMainThreadHandler = new Handler();
        this.mSurfaceCallback = new SurfaceCallback();
        this.mPreviewCallback = new PreviewCallback();
        this.mAutoFocusCallback = new AutoFocusCallback();
        this.mAutoFocusTask = new AutoFocusTask();
        this.mStopPreviewTask = new StopPreviewTask();
        this.mDecoderStateListener = new DecoderStateListener();
        this.mCameraId = cameraId;
        this.mScannerView.setCodeScanner(this);
    }

    public void setFormats(@NonNull List<BarcodeFormat> formats) {
        this.mInitializeLock.lock();
        try {
            if (this.mInitialized) {
                this.mDecoderWrapper.getDecoder().setFormats(formats);
            } else {
                this.mFormats = formats;
            }
            this.mInitializeLock.unlock();
        } catch (Throwable th) {
            this.mInitializeLock.unlock();
        }
    }

    public void setFormats(@NonNull BarcodeFormat... formats) {
        setFormats(Arrays.asList(formats));
    }

    public void setFormat(@NonNull BarcodeFormat format) {
        setFormats(Collections.singletonList(format));
    }

    public void setDecodeCallback(@Nullable DecodeCallback decodeCallback) {
        this.mDecodeCallback = decodeCallback;
    }

    public void setErrorCallback(@Nullable ErrorCallback errorCallback) {
        this.mErrorCallback = errorCallback;
    }

    public void setAutoFocusEnabled(boolean autoFocusEnabled) {
        this.mInitializeLock.lock();
        try {
            boolean changed = this.mAutoFocusEnabled != autoFocusEnabled;
            this.mAutoFocusEnabled = autoFocusEnabled;
            this.mScannerView.setAutoFocusEnabled(autoFocusEnabled);
            if (this.mInitialized && this.mPreviewActive && changed && this.mDecoderWrapper.isAutoFocusSupported()) {
                setAutoFocusEnabledInternal(autoFocusEnabled);
            }
            this.mInitializeLock.unlock();
        } catch (Throwable th) {
            this.mInitializeLock.unlock();
        }
    }

    public boolean isAutoFocusEnabled() {
        return this.mAutoFocusEnabled;
    }

    public void setFlashEnabled(boolean flashEnabled) {
        this.mInitializeLock.lock();
        try {
            boolean changed = this.mFlashEnabled != flashEnabled;
            this.mFlashEnabled = flashEnabled;
            this.mScannerView.setFlashEnabled(flashEnabled);
            if (this.mInitialized && this.mPreviewActive && changed && this.mDecoderWrapper.isFlashSupported()) {
                setFlashEnabledInternal(flashEnabled);
            }
            this.mInitializeLock.unlock();
        } catch (Throwable th) {
            this.mInitializeLock.unlock();
        }
    }

    public boolean isFlashEnabled() {
        return this.mFlashEnabled;
    }

    public boolean isPreviewActive() {
        return this.mPreviewActive;
    }

    @MainThread
    public void startPreview() {
        this.mInitializeLock.lock();
        try {
            if (this.mInitialized || this.mInitialization) {
                this.mInitializeLock.unlock();
                if (!this.mPreviewActive) {
                    this.mSurfaceHolder.addCallback(this.mSurfaceCallback);
                    startPreviewInternal(false);
                    return;
                }
                return;
            }
            this.mInitialization = true;
            initialize();
        } finally {
            this.mInitializeLock.unlock();
        }
    }

    @MainThread
    public void stopPreview() {
        if (this.mInitialized && this.mPreviewActive) {
            this.mSurfaceHolder.removeCallback(this.mSurfaceCallback);
            stopPreviewInternal(false);
        }
    }

    @MainThread
    public void releaseResources() {
        if (this.mInitialized) {
            if (this.mPreviewActive) {
                stopPreview();
            }
            releaseResourcesInternal();
        }
    }

    private void initialize() {
        if (Utils.isLaidOut(this.mScannerView)) {
            initialize(this.mScannerView.getWidth(), this.mScannerView.getHeight());
        } else {
            this.mScannerView.setLayoutListener(new ScannerLayoutListener());
        }
    }

    private void initialize(int width, int height) {
        new InitializationThread(width, height).start();
    }

    private void startPreviewInternal(boolean internal) {
        try {
            DecoderWrapper decoderWrapper = this.mDecoderWrapper;
            Camera camera = decoderWrapper.getCamera();
            camera.setPreviewCallback(this.mPreviewCallback);
            camera.setPreviewDisplay(this.mSurfaceHolder);
            if (!internal && decoderWrapper.isFlashSupported() && this.mFlashEnabled) {
                setFlashEnabledInternal(true);
            }
            camera.startPreview();
            this.mStoppingPreview = false;
            this.mPreviewActive = true;
            this.mFocusing = false;
            this.mFocusAttemptsCount = 0;
            scheduleAutoFocusTask();
        } catch (Exception e) {
        }
    }

    private void startPreviewInternalSafe() {
        if (this.mInitialized && !this.mPreviewActive) {
            startPreviewInternal(true);
        }
    }

    private void stopPreviewInternal(boolean internal) {
        try {
            DecoderWrapper decoderWrapper = this.mDecoderWrapper;
            Camera camera = decoderWrapper.getCamera();
            if (!internal && decoderWrapper.isFlashSupported() && this.mFlashEnabled) {
                Parameters parameters = camera.getParameters();
                if (parameters != null && Utils.setFlashMode(parameters, "off")) {
                    camera.setParameters(parameters);
                }
            }
            camera.setPreviewCallback(null);
            camera.stopPreview();
        } catch (Exception e) {
        }
        this.mStoppingPreview = false;
        this.mPreviewActive = false;
        this.mFocusing = false;
        this.mFocusAttemptsCount = 0;
    }

    private void stopPreviewInternalSafe() {
        if (this.mInitialized && this.mPreviewActive) {
            stopPreviewInternal(true);
        }
    }

    private void releaseResourcesInternal() {
        this.mInitialized = false;
        this.mInitialization = false;
        this.mStoppingPreview = false;
        this.mPreviewActive = false;
        this.mFocusing = false;
        DecoderWrapper decoderWrapper = this.mDecoderWrapper;
        if (decoderWrapper != null) {
            this.mDecoderWrapper = null;
            decoderWrapper.release();
        }
    }

    private void setFlashEnabledInternal(boolean flashEnabled) {
        try {
            Camera camera = this.mDecoderWrapper.getCamera();
            Parameters parameters = camera.getParameters();
            if (parameters != null) {
                boolean changed;
                if (flashEnabled) {
                    changed = Utils.setFlashMode(parameters, "torch");
                } else {
                    changed = Utils.setFlashMode(parameters, "off");
                }
                if (changed) {
                    camera.setParameters(parameters);
                }
            }
        } catch (Exception e) {
        }
    }

    private void setAutoFocusEnabledInternal(boolean autoFocusEnabled) {
        try {
            Camera camera = this.mDecoderWrapper.getCamera();
            Parameters parameters = camera.getParameters();
            if (parameters != null) {
                boolean changed;
                if (autoFocusEnabled) {
                    changed = Utils.setFocusMode(parameters, "auto");
                } else {
                    camera.cancelAutoFocus();
                    changed = Utils.setFocusMode(parameters, "fixed");
                }
                if (changed) {
                    camera.setParameters(parameters);
                }
                if (autoFocusEnabled) {
                    scheduleAutoFocusTask();
                }
            }
        } catch (Exception e) {
        }
    }

    private void autoFocusCamera() {
        if (this.mInitialized && this.mPreviewActive && this.mDecoderWrapper.isAutoFocusSupported() && this.mAutoFocusEnabled) {
            if (!this.mFocusing || this.mFocusAttemptsCount >= 2) {
                try {
                    this.mDecoderWrapper.getCamera().autoFocus(this.mAutoFocusCallback);
                    this.mFocusAttemptsCount = 0;
                    this.mFocusing = true;
                } catch (Exception e) {
                    this.mFocusing = false;
                }
            } else {
                this.mFocusAttemptsCount++;
            }
            scheduleAutoFocusTask();
        }
    }

    private void scheduleAutoFocusTask() {
        this.mMainThreadHandler.postDelayed(this.mAutoFocusTask, AUTO_FOCUS_INTERVAL);
    }

    boolean isAutoFocusSupportedOrUnknown() {
        DecoderWrapper wrapper = this.mDecoderWrapper;
        return wrapper == null || wrapper.isAutoFocusSupported();
    }

    boolean isFlashSupportedOrUnknown() {
        DecoderWrapper wrapper = this.mDecoderWrapper;
        return wrapper == null || wrapper.isFlashSupported();
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }
}
