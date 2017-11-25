package com.budiyev.android.codescanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public final class CodeScannerView extends ViewGroup {
    private static final float BUTTON_SIZE_DP = 56.0f;
    private static final int DEFAULT_AUTO_FOCUS_BUTTON_COLOR = -1;
    private static final int DEFAULT_AUTO_FOCUS_BUTTON_VISIBILITY = 0;
    private static final boolean DEFAULT_AUTO_FOCUS_BUTTON_VISIBLE = true;
    private static final int DEFAULT_FLASH_BUTTON_COLOR = -1;
    private static final int DEFAULT_FLASH_BUTTON_VISIBILITY = 0;
    private static final boolean DEFAULT_FLASH_BUTTON_VISIBLE = true;
    private static final int DEFAULT_FRAME_COLOR = -1;
    private static final float DEFAULT_FRAME_CORNER_SIZE_DP = 50.0f;
    private static final float DEFAULT_FRAME_WIDTH_DP = 2.0f;
    private static final int DEFAULT_MASK_COLOR = 1996488704;
    private static final boolean DEFAULT_SQUARE_FRAME = false;
    private ImageView mAutoFocusButton;
    private int mButtonSize;
    private CodeScanner mCodeScanner;
    private ImageView mFlashButton;
    private Point mFrameSize;
    private LayoutListener mLayoutListener;
    private SurfaceView mPreviewView;
    private ViewFinderView mViewFinderView;

    private final class AutoFocusClickListener implements OnClickListener {
        private AutoFocusClickListener() {
        }

        public void onClick(View view) {
            CodeScanner scanner = CodeScannerView.this.mCodeScanner;
            if (scanner != null && scanner.isAutoFocusSupportedOrUnknown()) {
                boolean enabled = !scanner.isAutoFocusEnabled();
                scanner.setAutoFocusEnabled(enabled);
                CodeScannerView.this.setAutoFocusEnabled(enabled);
            }
        }
    }

    private final class FlashClickListener implements OnClickListener {
        private FlashClickListener() {
        }

        public void onClick(View view) {
            CodeScanner scanner = CodeScannerView.this.mCodeScanner;
            if (scanner != null && scanner.isFlashSupportedOrUnknown()) {
                boolean enabled = !scanner.isFlashEnabled();
                scanner.setFlashEnabled(enabled);
                CodeScannerView.this.setFlashEnabled(enabled);
            }
        }
    }

    interface LayoutListener {
        void onLayout(int i, int i2);
    }

    public CodeScannerView(@NonNull Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    public CodeScannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
    }

    public CodeScannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(21)
    public CodeScannerView(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        int i = 0;
        this.mPreviewView = new SurfaceView(context);
        this.mPreviewView.setLayoutParams(new LayoutParams(-1, -1));
        this.mViewFinderView = new ViewFinderView(context);
        this.mViewFinderView.setLayoutParams(new LayoutParams(-1, -1));
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.mButtonSize = Math.round(displayMetrics.density * BUTTON_SIZE_DP);
        this.mAutoFocusButton = new ImageView(context);
        this.mAutoFocusButton.setLayoutParams(new LayoutParams(this.mButtonSize, this.mButtonSize));
        this.mAutoFocusButton.setScaleType(ScaleType.CENTER);
        this.mAutoFocusButton.setImageResource(C0313R.drawable.ic_code_scanner_auto_focus_on);
        this.mAutoFocusButton.setOnClickListener(new AutoFocusClickListener());
        this.mFlashButton = new ImageView(context);
        this.mFlashButton.setLayoutParams(new LayoutParams(this.mButtonSize, this.mButtonSize));
        this.mFlashButton.setScaleType(ScaleType.CENTER);
        this.mFlashButton.setImageResource(C0313R.drawable.ic_code_scanner_flash_on);
        this.mFlashButton.setOnClickListener(new FlashClickListener());
        if (attrs == null) {
            this.mViewFinderView.setSquareFrame(false);
            this.mViewFinderView.setMaskColor(DEFAULT_MASK_COLOR);
            this.mViewFinderView.setFrameColor(-1);
            this.mViewFinderView.setFrameWidth(Math.round(displayMetrics.density * DEFAULT_FRAME_WIDTH_DP));
            this.mViewFinderView.setFrameCornersSize(Math.round(displayMetrics.density * DEFAULT_FRAME_CORNER_SIZE_DP));
            this.mAutoFocusButton.setColorFilter(-1);
            this.mFlashButton.setColorFilter(-1);
            this.mAutoFocusButton.setVisibility(0);
            this.mFlashButton.setVisibility(0);
        } else {
            TypedArray attributes = null;
            try {
                attributes = context.getTheme().obtainStyledAttributes(attrs, C0313R.styleable.CodeScannerView, defStyleAttr, defStyleRes);
                this.mViewFinderView.setSquareFrame(attributes.getBoolean(C0313R.styleable.CodeScannerView_squareFrame, false));
                this.mViewFinderView.setMaskColor(attributes.getColor(C0313R.styleable.CodeScannerView_maskColor, DEFAULT_MASK_COLOR));
                this.mViewFinderView.setFrameColor(attributes.getColor(C0313R.styleable.CodeScannerView_frameColor, -1));
                this.mViewFinderView.setFrameWidth(attributes.getDimensionPixelSize(C0313R.styleable.CodeScannerView_frameWidth, Math.round(displayMetrics.density * DEFAULT_FRAME_WIDTH_DP)));
                this.mViewFinderView.setFrameCornersSize(attributes.getDimensionPixelSize(C0313R.styleable.CodeScannerView_frameCornersSize, Math.round(displayMetrics.density * DEFAULT_FRAME_CORNER_SIZE_DP)));
                this.mAutoFocusButton.setColorFilter(attributes.getColor(C0313R.styleable.CodeScannerView_autoFocusButtonColor, -1));
                this.mFlashButton.setColorFilter(attributes.getColor(C0313R.styleable.CodeScannerView_flashButtonColor, -1));
                this.mAutoFocusButton.setVisibility(attributes.getBoolean(C0313R.styleable.CodeScannerView_autoFocusButtonVisible, true) ? 0 : 4);
                ImageView imageView = this.mFlashButton;
                if (!attributes.getBoolean(C0313R.styleable.CodeScannerView_flashButtonVisible, true)) {
                    i = 4;
                }
                imageView.setVisibility(i);
                if (attributes != null) {
                    attributes.recycle();
                }
            } catch (Throwable th) {
                if (attributes != null) {
                    attributes.recycle();
                }
            }
        }
        addView(this.mPreviewView);
        addView(this.mViewFinderView);
        addView(this.mAutoFocusButton);
        addView(this.mFlashButton);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        Point frameSize = this.mFrameSize;
        if (frameSize == null) {
            this.mPreviewView.layout(left, top, right, bottom);
        } else {
            int d;
            int frameLeft = left;
            int frameTop = top;
            int frameRight = right;
            int frameBottom = bottom;
            if (frameSize.x > width) {
                d = (frameSize.x - width) / 2;
                frameLeft -= d;
                frameRight += d;
            }
            if (frameSize.y > height) {
                d = (frameSize.y - height) / 2;
                frameTop -= d;
                frameBottom += d;
            }
            this.mPreviewView.layout(frameLeft, frameTop, frameRight, frameBottom);
        }
        this.mViewFinderView.layout(left, top, right, bottom);
        int buttonSize = this.mButtonSize;
        this.mAutoFocusButton.layout(left, top, left + buttonSize, top + buttonSize);
        this.mFlashButton.layout(right - buttonSize, top, right, top + buttonSize);
        LayoutListener listener = this.mLayoutListener;
        if (listener != null) {
            listener.onLayout(width, height);
        }
    }

    public void setSquareFrame(boolean squareFrame) {
        this.mViewFinderView.setSquareFrame(squareFrame);
    }

    public void setMaskColor(@ColorInt int color) {
        this.mViewFinderView.setMaskColor(color);
    }

    public void setFrameColor(@ColorInt int color) {
        this.mViewFinderView.setFrameColor(color);
    }

    public void setFrameWidth(@Px int width) {
        this.mViewFinderView.setFrameWidth(width);
    }

    public void setFrameCornersSize(@Px int size) {
        this.mViewFinderView.setFrameCornersSize(size);
    }

    public void setAutoFocusButtonVisible(boolean visible) {
        if (visible) {
            this.mAutoFocusButton.setVisibility(0);
        } else {
            this.mAutoFocusButton.setVisibility(4);
        }
    }

    public void setFlashButtonVisible(boolean visible) {
        if (visible) {
            this.mFlashButton.setVisibility(0);
        } else {
            this.mFlashButton.setVisibility(4);
        }
    }

    boolean isSquareFrame() {
        return this.mViewFinderView.isSquareFrame();
    }

    SurfaceView getPreviewView() {
        return this.mPreviewView;
    }

    ViewFinderView getViewFinderView() {
        return this.mViewFinderView;
    }

    void setFrameSize(@Nullable Point frameSize) {
        this.mFrameSize = frameSize;
        requestLayout();
    }

    void setLayoutListener(@Nullable LayoutListener layoutListener) {
        this.mLayoutListener = layoutListener;
    }

    void setCodeScanner(@Nullable CodeScanner codeScanner) {
        this.mCodeScanner = codeScanner;
        if (codeScanner != null) {
            setAutoFocusEnabled(codeScanner.isAutoFocusEnabled());
            setFlashEnabled(codeScanner.isFlashEnabled());
        }
    }

    void setAutoFocusEnabled(boolean enabled) {
        if (enabled) {
            this.mAutoFocusButton.setImageResource(C0313R.drawable.ic_code_scanner_auto_focus_on);
        } else {
            this.mAutoFocusButton.setImageResource(C0313R.drawable.ic_code_scanner_auto_focus_off);
        }
    }

    void setFlashEnabled(boolean enabled) {
        if (enabled) {
            this.mFlashButton.setImageResource(C0313R.drawable.ic_code_scanner_flash_on);
        } else {
            this.mFlashButton.setImageResource(C0313R.drawable.ic_code_scanner_flash_off);
        }
    }
}
