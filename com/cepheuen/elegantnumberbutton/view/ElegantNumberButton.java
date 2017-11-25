package com.cepheuen.elegantnumberbutton.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cepheuen.elegantnumberbutton.C0315R;

public class ElegantNumberButton extends RelativeLayout {
    static final /* synthetic */ boolean $assertionsDisabled = (!ElegantNumberButton.class.desiredAssertionStatus());
    private AttributeSet attrs;
    private Context context;
    private int currentNumber;
    private int finalNumber;
    private int initialNumber;
    private int lastNumber;
    private OnClickListener mListener;
    private OnValueChangeListener mOnValueChangeListener;
    private int styleAttr;
    private TextView textView;
    private View view;

    class C03161 implements android.view.View.OnClickListener {
        C03161() {
        }

        public void onClick(View mView) {
            ElegantNumberButton.this.setNumber(String.valueOf(Integer.valueOf(ElegantNumberButton.this.textView.getText().toString()).intValue() - 1), true);
        }
    }

    class C03172 implements android.view.View.OnClickListener {
        C03172() {
        }

        public void onClick(View mView) {
            ElegantNumberButton.this.setNumber(String.valueOf(Integer.valueOf(ElegantNumberButton.this.textView.getText().toString()).intValue() + 1), true);
        }
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    public interface OnValueChangeListener {
        void onValueChange(ElegantNumberButton elegantNumberButton, int i, int i2);
    }

    public ElegantNumberButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {
        this.view = this;
        inflate(this.context, C0315R.layout.layout, this);
        Resources res = getResources();
        int defaultColor = res.getColor(C0315R.color.colorPrimary);
        int defaultTextColor = res.getColor(C0315R.color.colorText);
        Drawable defaultDrawable = res.getDrawable(C0315R.drawable.background);
        TypedArray a = this.context.obtainStyledAttributes(this.attrs, C0315R.styleable.ElegantNumberButton, this.styleAttr, 0);
        this.initialNumber = a.getInt(C0315R.styleable.ElegantNumberButton_initialNumber, 0);
        this.finalNumber = a.getInt(C0315R.styleable.ElegantNumberButton_finalNumber, Integer.MAX_VALUE);
        float textSize = a.getDimension(C0315R.styleable.ElegantNumberButton_textSize, 13.0f);
        int color = a.getColor(C0315R.styleable.ElegantNumberButton_backGroundColor, defaultColor);
        int textColor = a.getColor(C0315R.styleable.ElegantNumberButton_textColor, defaultTextColor);
        Drawable drawable = a.getDrawable(C0315R.styleable.ElegantNumberButton_backgroundDrawable);
        Button button1 = (Button) findViewById(C0315R.id.subtract_btn);
        Button button2 = (Button) findViewById(C0315R.id.add_btn);
        this.textView = (TextView) findViewById(C0315R.id.number_counter);
        LinearLayout mLayout = (LinearLayout) findViewById(C0315R.id.layout);
        button1.setTextColor(textColor);
        button2.setTextColor(textColor);
        this.textView.setTextColor(textColor);
        button1.setTextSize(textSize);
        button2.setTextSize(textSize);
        this.textView.setTextSize(textSize);
        if (drawable == null) {
            drawable = defaultDrawable;
        }
        if ($assertionsDisabled || drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC));
            if (VERSION.SDK_INT > 16) {
                mLayout.setBackground(drawable);
            } else {
                mLayout.setBackgroundDrawable(drawable);
            }
            this.textView.setText(String.valueOf(this.initialNumber));
            this.currentNumber = this.initialNumber;
            this.lastNumber = this.initialNumber;
            button1.setOnClickListener(new C03161());
            button2.setOnClickListener(new C03172());
            a.recycle();
            return;
        }
        throw new AssertionError();
    }

    private void callListener(View view) {
        if (this.mListener != null) {
            this.mListener.onClick(view);
        }
        if (this.mOnValueChangeListener != null && this.lastNumber != this.currentNumber) {
            this.mOnValueChangeListener.onValueChange(this, this.lastNumber, this.currentNumber);
        }
    }

    public String getNumber() {
        return String.valueOf(this.currentNumber);
    }

    public void setNumber(String number) {
        this.lastNumber = this.currentNumber;
        this.currentNumber = Integer.parseInt(number);
        if (this.currentNumber > this.finalNumber) {
            this.currentNumber = this.finalNumber;
        }
        if (this.currentNumber < this.initialNumber) {
            this.currentNumber = this.initialNumber;
        }
        this.textView.setText(String.valueOf(this.currentNumber));
    }

    public void setNumber(String number, boolean notifyListener) {
        setNumber(number);
        if (notifyListener) {
            callListener(this);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.mOnValueChangeListener = onValueChangeListener;
    }

    public void setRange(Integer startingNumber, Integer endingNumber) {
        this.initialNumber = startingNumber.intValue();
        this.finalNumber = endingNumber.intValue();
    }
}
