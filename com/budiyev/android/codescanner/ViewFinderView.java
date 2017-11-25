package com.budiyev.android.codescanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.view.View;

final class ViewFinderView extends View {
    private int mFrameCornerSize;
    private final Paint mFramePaint = new Paint(1);
    private final Path mFramePath;
    private Rect mFrameRect;
    private final Paint mMaskPaint = new Paint(1);
    private boolean mSquareFrame;

    public ViewFinderView(@NonNull Context context) {
        super(context);
        this.mFramePaint.setStyle(Style.STROKE);
        this.mFramePath = new Path();
    }

    protected void onDraw(Canvas canvas) {
        Rect frameRect = this.mFrameRect;
        if (frameRect != null) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            canvas.drawRect(0.0f, 0.0f, (float) width, (float) frameRect.top, this.mMaskPaint);
            canvas.drawRect(0.0f, (float) frameRect.top, (float) frameRect.left, (float) frameRect.bottom, this.mMaskPaint);
            canvas.drawRect((float) frameRect.right, (float) frameRect.top, (float) width, (float) frameRect.bottom, this.mMaskPaint);
            canvas.drawRect(0.0f, (float) frameRect.bottom, (float) width, (float) height, this.mMaskPaint);
            this.mFramePath.reset();
            this.mFramePath.moveTo((float) frameRect.left, (float) (frameRect.top + this.mFrameCornerSize));
            this.mFramePath.lineTo((float) frameRect.left, (float) frameRect.top);
            this.mFramePath.lineTo((float) (frameRect.left + this.mFrameCornerSize), (float) frameRect.top);
            this.mFramePath.moveTo((float) (frameRect.right - this.mFrameCornerSize), (float) frameRect.top);
            this.mFramePath.lineTo((float) frameRect.right, (float) frameRect.top);
            this.mFramePath.lineTo((float) frameRect.right, (float) (frameRect.top + this.mFrameCornerSize));
            this.mFramePath.moveTo((float) frameRect.right, (float) (frameRect.bottom - this.mFrameCornerSize));
            this.mFramePath.lineTo((float) frameRect.right, (float) frameRect.bottom);
            this.mFramePath.lineTo((float) (frameRect.right - this.mFrameCornerSize), (float) frameRect.bottom);
            this.mFramePath.moveTo((float) (frameRect.left + this.mFrameCornerSize), (float) frameRect.bottom);
            this.mFramePath.lineTo((float) frameRect.left, (float) frameRect.bottom);
            this.mFramePath.lineTo((float) frameRect.left, (float) (frameRect.bottom - this.mFrameCornerSize));
            canvas.drawPath(this.mFramePath, this.mFramePaint);
        }
    }

    protected void onSizeChanged(int width, int height, int oldW, int oldH) {
        this.mFrameRect = Utils.getFrameRect(this.mSquareFrame, width, height);
    }

    void setSquareFrame(boolean squareFrame) {
        this.mSquareFrame = squareFrame;
        if (this.mFrameRect != null) {
            this.mFrameRect = Utils.getFrameRect(this.mSquareFrame, getWidth(), getHeight());
        }
        if (Utils.isLaidOut(this)) {
            invalidate();
        }
    }

    boolean isSquareFrame() {
        return this.mSquareFrame;
    }

    void setMaskColor(@ColorInt int color) {
        this.mMaskPaint.setColor(color);
        if (Utils.isLaidOut(this)) {
            invalidate();
        }
    }

    void setFrameColor(@ColorInt int color) {
        this.mFramePaint.setColor(color);
        if (Utils.isLaidOut(this)) {
            invalidate();
        }
    }

    void setFrameWidth(@Px int width) {
        this.mFramePaint.setStrokeWidth((float) width);
        if (Utils.isLaidOut(this)) {
            invalidate();
        }
    }

    void setFrameCornersSize(@Px int size) {
        this.mFrameCornerSize = size;
        if (Utils.isLaidOut(this)) {
            invalidate();
        }
    }
}
