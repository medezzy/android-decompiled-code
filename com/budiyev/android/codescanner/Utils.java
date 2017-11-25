package com.budiyev.android.codescanner;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import com.google.zxing.client.android.camera.CameraConfigurationUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

final class Utils {
    private static final float LANDSCAPE_HEIGHT_RATIO = 0.625f;
    private static final float LANDSCAPE_WIDTH_RATIO = 1.4f;
    private static final float MAX_DISTORTION = 0.5f;
    private static final int MIN_PREVIEW_PIXELS = 442368;
    private static final float PORTRAIT_HEIGHT_RATIO = 0.75f;
    private static final float PORTRAIT_WIDTH_RATIO = 0.75f;
    private static final float SQUARE_RATIO = 0.75f;

    private static final class CameraSizeComparator implements Comparator<Size> {
        private CameraSizeComparator() {
        }

        public int compare(@NonNull Size a, @NonNull Size b) {
            int aPixels = a.height * a.width;
            int bPixels = b.height * b.width;
            if (bPixels < aPixels) {
                return -1;
            }
            if (bPixels > aPixels) {
                return 1;
            }
            return 0;
        }
    }

    public static final class SuppressErrorCallback implements ErrorCallback {
        public void onError(@NonNull Exception error) {
        }
    }

    private Utils() {
    }

    @NonNull
    public static Parameters optimizeParameters(@NonNull Parameters parameters) {
        CameraConfigurationUtils.setBestPreviewFPS(parameters);
        CameraConfigurationUtils.setBarcodeSceneMode(parameters);
        CameraConfigurationUtils.setBestExposure(parameters, false);
        CameraConfigurationUtils.setVideoStabilization(parameters);
        parameters.setPreviewFormat(17);
        return parameters;
    }

    @NonNull
    public static Point findSuitablePreviewSize(@NonNull Parameters parameters, int frameWidth, int frameHeight) {
        List<Size> sizes = parameters.getSupportedPreviewSizes();
        if (!(sizes == null || sizes.isEmpty())) {
            Collections.sort(sizes, new CameraSizeComparator());
            float frameRatio = ((float) frameWidth) / ((float) frameHeight);
            for (Size size : sizes) {
                int width = size.width;
                int height = size.height;
                if (width * height >= MIN_PREVIEW_PIXELS && Math.abs(frameRatio - (((float) width) / ((float) height))) <= MAX_DISTORTION) {
                    return new Point(width, height);
                }
            }
        }
        Size defaultSize = parameters.getPreviewSize();
        if (defaultSize != null) {
            return new Point(defaultSize.width, defaultSize.height);
        }
        throw new RuntimeException("Can't get camera preview size");
    }

    public static boolean setFocusMode(@NonNull Parameters parameters, @NonNull String focusMode) {
        if (focusMode.equals(parameters.getFocusMode())) {
            return false;
        }
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes == null || !focusModes.contains(focusMode)) {
            return false;
        }
        parameters.setFocusMode(focusMode);
        return true;
    }

    public static boolean setFlashMode(@NonNull Parameters parameters, @NonNull String flashMode) {
        if (flashMode.equals(parameters.getFlashMode())) {
            return false;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null || !flashModes.contains(flashMode)) {
            return false;
        }
        parameters.setFlashMode(flashMode);
        return true;
    }

    public static int getDisplayOrientation(@NonNull Context context, @NonNull CameraInfo cameraInfo) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            throw new RuntimeException("Can't access window manager");
        }
        int degrees;
        int i;
        int rotation = windowManager.getDefaultDisplay().getRotation();
        switch (rotation) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
                break;
            default:
                if (rotation % 90 == 0) {
                    degrees = (rotation + 360) % 360;
                    break;
                }
                throw new RuntimeException("Bad rotation: " + rotation);
        }
        if (cameraInfo.facing == 1) {
            i = 180;
        } else {
            i = 360;
        }
        return ((i + cameraInfo.orientation) - degrees) % 360;
    }

    public static boolean isPortrait(int orientation) {
        return orientation == 90 || orientation == 270;
    }

    public static boolean isLaidOut(@NonNull View view) {
        if (VERSION.SDK_INT >= 19) {
            return view.isLaidOut();
        }
        return view.getWidth() > 0 && view.getHeight() > 0;
    }

    @NonNull
    public static Point getFrameSize(int imageWidth, int imageHeight, int frameWidth, int frameHeight) {
        if (imageWidth == frameWidth && imageHeight == frameHeight) {
            return new Point(frameWidth, frameHeight);
        }
        int imageDivisor = greatestCommonDivisor(imageWidth, imageHeight);
        int imageRatioWidth = imageWidth / imageDivisor;
        int imageRatioHeight = imageHeight / imageDivisor;
        int resultWidth = (imageRatioWidth * frameHeight) / imageRatioHeight;
        if (resultWidth < frameWidth) {
            return new Point(frameWidth, (imageRatioHeight * frameWidth) / imageRatioWidth);
        }
        return new Point(resultWidth, frameHeight);
    }

    @NonNull
    public static Rect getFrameRect(boolean squareFrame, int width, int height) {
        int frameWidth;
        int frameHeight;
        if (squareFrame) {
            frameWidth = Math.round(((float) Math.min(width, height)) * 0.75f);
            frameHeight = frameWidth;
        } else if (width >= height) {
            frameHeight = Math.round(((float) height) * LANDSCAPE_HEIGHT_RATIO);
            frameWidth = Math.round(LANDSCAPE_WIDTH_RATIO * ((float) frameHeight));
        } else {
            frameWidth = Math.round(((float) width) * 0.75f);
            frameHeight = Math.round(((float) frameWidth) * 0.75f);
        }
        int left = (width - frameWidth) / 2;
        int top = (height - frameHeight) / 2;
        return new Rect(left, top, left + frameWidth, top + frameHeight);
    }

    @NonNull
    public static Rect getImageFrameRect(boolean squareFrame, int imageWidth, int imageHeight, int frameWidth, int frameHeight) {
        Point frameSize = getFrameSize(imageWidth, imageHeight, frameWidth, frameHeight);
        int wDiff = (frameSize.x - frameWidth) / 2;
        int hDiff = (frameSize.y - frameHeight) / 2;
        Rect frameRect = getFrameRect(squareFrame, frameWidth, frameHeight);
        frameRect.left += wDiff;
        frameRect.top += hDiff;
        frameRect.right += wDiff;
        frameRect.bottom += hDiff;
        float wRatio = ((float) imageWidth) / ((float) frameSize.x);
        float hRatio = ((float) imageHeight) / ((float) frameSize.y);
        frameRect.left = Math.round(((float) frameRect.left) * wRatio);
        frameRect.top = Math.round(((float) frameRect.top) * hRatio);
        frameRect.right = Math.round(((float) frameRect.right) * wRatio);
        frameRect.bottom = Math.round(((float) frameRect.bottom) * hRatio);
        return frameRect;
    }

    public static byte[] rotateNV21(byte[] yuv, int width, int height, int rotation) {
        if (rotation == 0 || rotation == 360) {
            return yuv;
        }
        if (rotation % 90 != 0 || rotation < 0 || rotation > 270) {
            throw new IllegalArgumentException("0 <= rotation < 360, rotation % 90 == 0");
        }
        byte[] output = new byte[yuv.length];
        int frameSize = width * height;
        boolean swap = rotation % 180 != 0;
        boolean flipX = rotation % 270 != 0;
        boolean flipY = rotation >= 180;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int wOut;
                int hOut;
                int iSwapped;
                int jSwapped;
                int iOut;
                int jOut;
                int yIn = (j * width) + i;
                int uIn = (((j >> 1) * width) + frameSize) + (i & -2);
                int vIn = uIn + 1;
                if (swap) {
                    wOut = height;
                } else {
                    wOut = width;
                }
                if (swap) {
                    hOut = width;
                } else {
                    hOut = height;
                }
                if (swap) {
                    iSwapped = j;
                } else {
                    iSwapped = i;
                }
                if (swap) {
                    jSwapped = i;
                } else {
                    jSwapped = j;
                }
                if (flipX) {
                    iOut = (wOut - iSwapped) - 1;
                } else {
                    iOut = iSwapped;
                }
                if (flipY) {
                    jOut = (hOut - jSwapped) - 1;
                } else {
                    jOut = jSwapped;
                }
                int uOut = (((jOut >> 1) * wOut) + frameSize) + (iOut & -2);
                int vOut = uOut + 1;
                output[(jOut * wOut) + iOut] = (byte) (yuv[yIn] & 255);
                output[uOut] = (byte) (yuv[uIn] & 255);
                output[vOut] = (byte) (yuv[vIn] & 255);
            }
        }
        return output;
    }

    private static int greatestCommonDivisor(int a, int b) {
        while (a > 0 && b > 0) {
            if (a > b) {
                a %= b;
            } else {
                b %= a;
            }
        }
        return a + b;
    }
}
