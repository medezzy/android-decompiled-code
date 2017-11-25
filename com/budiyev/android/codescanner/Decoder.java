package com.budiyev.android.codescanner;

import android.support.annotation.NonNull;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

final class Decoder {
    private final BlockingQueue<DecodeTask> mDecodeQueue = new LinkedBlockingQueue();
    private final DecoderThread mDecoderThread;
    private final Map<DecodeHintType, Object> mHints;
    private volatile boolean mProcessing;
    private final MultiFormatReader mReader;
    private final StateListener mStateListener;

    private final class DecoderThread extends Thread {
        public DecoderThread() {
            super("Code scanner decode thread");
            if (getPriority() != 5) {
                setPriority(5);
            }
            if (isDaemon()) {
                setDaemon(false);
            }
        }

        public void run() {
            Result result;
            while (true) {
                DecodeCallback callback;
                try {
                    Decoder.this.mStateListener.onStateChanged(0);
                    result = null;
                    callback = null;
                    DecodeTask task = (DecodeTask) Decoder.this.mDecodeQueue.take();
                    Decoder.this.mProcessing = true;
                    Decoder.this.mStateListener.onStateChanged(1);
                    result = task.decode(Decoder.this.mReader);
                    callback = task.getCallback();
                    if (result != null) {
                        Decoder.this.mStateListener.onStateChanged(2);
                        if (callback != null) {
                            callback.onDecoded(result);
                        }
                    }
                    Decoder.this.mProcessing = false;
                } catch (ReaderException e) {
                    if (result != null) {
                        Decoder.this.mStateListener.onStateChanged(2);
                        if (callback != null) {
                            callback.onDecoded(result);
                        }
                    }
                    Decoder.this.mProcessing = false;
                } catch (InterruptedException e2) {
                    return;
                } catch (Throwable th) {
                    if (result != null) {
                        Decoder.this.mStateListener.onStateChanged(2);
                        if (callback != null) {
                            callback.onDecoded(result);
                        }
                    }
                    Decoder.this.mProcessing = false;
                }
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        public static final int DECODED = 2;
        public static final int DECODING = 1;
        public static final int IDLE = 0;
    }

    public interface StateListener {
        void onStateChanged(int i);
    }

    public Decoder(@NonNull StateListener stateListener, @NonNull List<BarcodeFormat> formats) {
        this.mStateListener = stateListener;
        this.mReader = new MultiFormatReader();
        this.mDecoderThread = new DecoderThread();
        this.mHints = new EnumMap(DecodeHintType.class);
        this.mHints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        this.mReader.setHints(this.mHints);
    }

    public void setFormats(@NonNull List<BarcodeFormat> formats) {
        this.mHints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        this.mReader.setHints(this.mHints);
    }

    public void decode(@NonNull byte[] data, int dataWidth, int dataHeight, int frameWidth, int frameHeight, int orientation, boolean squareFrame, boolean reverseHorizontal, @NonNull DecodeCallback decodeCallback) {
        this.mDecodeQueue.add(new DecodeTask(data, dataWidth, dataHeight, frameWidth, frameHeight, orientation, squareFrame, reverseHorizontal, decodeCallback));
    }

    public void start() {
        this.mDecoderThread.start();
    }

    public void shutdown() {
        this.mDecoderThread.interrupt();
        this.mDecodeQueue.clear();
    }

    public boolean isProcessing() {
        return this.mProcessing;
    }
}
