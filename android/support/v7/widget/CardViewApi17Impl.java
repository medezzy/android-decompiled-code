package android.support.v7.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.RequiresApi;

@RequiresApi(17)
class CardViewApi17Impl extends CardViewBaseImpl {

    class C04651 implements RoundRectHelper {
        C04651() {
        }

        public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {
            canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint);
        }
    }

    CardViewApi17Impl() {
    }

    public void initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper = new C04651();
    }
}
