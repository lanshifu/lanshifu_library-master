package library.lanshifu.com.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import library.lanshifu.com.myapplication.R;

/**
 * Created by lanshifu on 2017/12/25.
 */

public class CustomRangeSeekBar extends View {
    private final Paint mPaint = new Paint();
    private Bitmap mThumbImage;
    private Bitmap mProgressBarBg;
    private Bitmap mProgressBarSelBg;
    private float mThumbWidth;
    private float mThumbHalfWidth;
    private float mThumbHalfHeight;
    private float mProgressBarHeight;
    private float mWidthPadding;
    private float mAbsoluteMinValue;
    private float mAbsoluteMaxValue;
    private double mPercentSelectedMinValue = 0.0D;
    private double mPercentSelectedMaxValue = 1.0D;
    private CustomRangeSeekBar.Thumb mPressedThumb = null;
    private CustomRangeSeekBar.ThumbListener mThumbListener;
    private RectF mProgressBarRect;
    private RectF mProgressBarSelRect;
    private boolean mIsEnable = true;
    private float mBetweenAbsoluteValue;
    private final int MIN_WIDTH = 200;
    public static final int HINT_FORMAT_NUMBER = 0;
    public static final int HINT_FORMAT_TIME = 1;
    private int mProgressTextFormat;
    private int mWordHeight;
    private float mWordSize;
    private float mStartMinPercent;
    private float mStartMaxPercent;

    public CustomRangeSeekBar(Context context) {
        super(context);
    }

    public CustomRangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.CustomRangeSeekBar, 0, 0);
        this.mAbsoluteMinValue = a.getFloat(R.styleable.CustomRangeSeekBar_absoluteMin, 0.0F);
        this.mAbsoluteMaxValue = a.getFloat(R.styleable.CustomRangeSeekBar_absolutemMax, 100.0F);
        this.mStartMinPercent = a.getFloat(R.styleable.CustomRangeSeekBar_startMinPercent, 0.0F);
        this.mStartMaxPercent = a.getFloat(R.styleable.CustomRangeSeekBar_startMaxPercent, 1.0F);
        this.mThumbImage = BitmapFactory.decodeResource(this.getResources(), a.getResourceId(R.styleable.CustomRangeSeekBar_thumbImage, R.mipmap.btn_seekbar_normal));
        this.mProgressBarBg = BitmapFactory.decodeResource(this.getResources(), a.getResourceId(R.styleable.CustomRangeSeekBar_progressBarBg, R.mipmap.seekbar_bg));
        this.mProgressBarSelBg = BitmapFactory.decodeResource(this.getResources(), a.getResourceId(R.styleable.CustomRangeSeekBar_progressBarSelBg, R.mipmap.seekbar_sel_bg));
        this.mBetweenAbsoluteValue = a.getFloat(R.styleable.CustomRangeSeekBar_betweenAbsoluteValue, 0.0F);
        this.mProgressTextFormat = a.getInt(R.styleable.CustomRangeSeekBar_progressTextFormat, 0);
        this.mWordSize = a.getDimension(R.styleable.CustomRangeSeekBar_progressTextSize, (float)dp2px(context, 16.0F));
        this.mPaint.setTextSize(this.mWordSize);
        this.mThumbWidth = (float)this.mThumbImage.getWidth();
        this.mThumbHalfWidth = 0.5F * this.mThumbWidth;
        this.mThumbHalfHeight = 0.5F * (float)this.mThumbImage.getHeight();
        this.mProgressBarHeight = 0.3F * this.mThumbHalfHeight;
        this.mWidthPadding = this.mThumbHalfHeight;
        FontMetrics metrics = this.mPaint.getFontMetrics();
        this.mWordHeight = (int)(metrics.descent - metrics.ascent);
        this.restorePercentSelectedMinValue();
        this.restorePercentSelectedMaxValue();
        a.recycle();
    }

    public void restorePercentSelectedMinValue() {
        this.setPercentSelectedMinValue((double)this.mStartMinPercent);
    }

    public void restorePercentSelectedMaxValue() {
        this.setPercentSelectedMaxValue((double)this.mStartMaxPercent);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mProgressBarRect = new RectF(this.mWidthPadding, (float)this.mWordHeight + 0.5F * ((float)(h - this.mWordHeight) - this.mProgressBarHeight), (float)w - this.mWidthPadding, (float)this.mWordHeight + 0.5F * ((float)(h - this.mWordHeight) + this.mProgressBarHeight));
        this.mProgressBarSelRect = new RectF(this.mProgressBarRect);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mIsEnable = enabled;
    }

    public void setAbsoluteMaxValue(double maxvalue) {
        this.mAbsoluteMaxValue = (new Float(maxvalue)).floatValue();
    }

    public float getSelectedAbsoluteMinValue() {
        return this.percentToAbsoluteValue(this.mPercentSelectedMinValue);
    }

    public boolean setSelectedAbsoluteMinValue(float value) {
        boolean status = true;
        if(0.0F == this.mAbsoluteMaxValue - this.mAbsoluteMinValue) {
            this.setPercentSelectedMinValue(0.0D);
        } else {
            float maxValue = this.percentToAbsoluteValue(this.mPercentSelectedMaxValue);
            if(this.mBetweenAbsoluteValue > 0.0F && maxValue - value <= this.mBetweenAbsoluteValue) {
                value = (new Float(maxValue - this.mBetweenAbsoluteValue)).floatValue();
                status = false;
            }

            if(maxValue - value <= 0.0F) {
                status = false;
                value = maxValue;
            }

            this.setPercentSelectedMinValue(this.absoluteValueToPercent(value));
        }

        return status;
    }

    public float getAbsoluteMaxValue() {
        return this.mAbsoluteMaxValue;
    }

    public float getSelectedAbsoluteMaxValue() {
        return this.percentToAbsoluteValue(this.mPercentSelectedMaxValue);
    }

    public boolean setSelectedAbsoluteMaxValue(float value) {
        boolean status = true;
        if(0.0F == this.mAbsoluteMaxValue - this.mAbsoluteMinValue) {
            this.setPercentSelectedMaxValue(1.0D);
        } else {
            float minValue = this.percentToAbsoluteValue(this.mPercentSelectedMinValue);
            if(this.mBetweenAbsoluteValue > 0.0F && value - minValue <= this.mBetweenAbsoluteValue) {
                value = (new Float(minValue + this.mBetweenAbsoluteValue)).floatValue();
                status = false;
            }

            if(value - minValue <= 0.0F) {
                status = false;
                value = minValue;
            }

            this.setPercentSelectedMaxValue(this.absoluteValueToPercent(value));
        }

        return status;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(!this.mIsEnable) {
            return true;
        } else {
            switch(event.getAction()) {
                case 0:
                    this.mPressedThumb = this.evalPressedThumb(event.getX());
                    if(CustomRangeSeekBar.Thumb.MIN.equals(this.mPressedThumb) && this.mThumbListener != null) {
                        this.mThumbListener.onClickMinThumb(Float.valueOf(this.getSelectedAbsoluteMaxValue()), Float.valueOf(this.getSelectedAbsoluteMinValue()));
                    }

                    if(CustomRangeSeekBar.Thumb.MAX.equals(this.mPressedThumb) && this.mThumbListener != null) {
                        this.mThumbListener.onClickMaxThumb();
                    }

                    this.invalidate();
                    if(this.getParent() != null) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case 1:
                    if(CustomRangeSeekBar.Thumb.MIN.equals(this.mPressedThumb) && this.mThumbListener != null) {
                        this.mThumbListener.onUpMinThumb(Float.valueOf(this.getSelectedAbsoluteMaxValue()), Float.valueOf(this.getSelectedAbsoluteMinValue()));
                    }

                    if(CustomRangeSeekBar.Thumb.MAX.equals(this.mPressedThumb) && this.mThumbListener != null) {
                        this.mThumbListener.onUpMaxThumb();
                    }

                    if(this.getParent() != null) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case 2:
                    if(this.mPressedThumb != null) {
                        float eventX = event.getX();
                        float maxValue = this.percentToAbsoluteValue(this.mPercentSelectedMaxValue);
                        float minValue = this.percentToAbsoluteValue(this.mPercentSelectedMinValue);
                        float eventValue = this.percentToAbsoluteValue(this.screenToPercent(eventX));
                        if(CustomRangeSeekBar.Thumb.MIN.equals(this.mPressedThumb)) {
                            minValue = eventValue;
                            if(this.mBetweenAbsoluteValue > 0.0F && maxValue - eventValue <= this.mBetweenAbsoluteValue) {
                                minValue = (new Float(maxValue - this.mBetweenAbsoluteValue)).floatValue();
                            }

                            this.setPercentSelectedMinValue(this.absoluteValueToPercent(minValue));
                            if(this.mThumbListener != null) {
                                this.mThumbListener.onMinMove(Float.valueOf(this.getSelectedAbsoluteMaxValue()), Float.valueOf(this.getSelectedAbsoluteMinValue()));
                            }
                        } else if(CustomRangeSeekBar.Thumb.MAX.equals(this.mPressedThumb)) {
                            maxValue = eventValue;
                            if(this.mBetweenAbsoluteValue > 0.0F && eventValue - minValue <= this.mBetweenAbsoluteValue) {
                                maxValue = (new Float(minValue + this.mBetweenAbsoluteValue)).floatValue();
                            }

                            this.setPercentSelectedMaxValue(this.absoluteValueToPercent(maxValue));
                            if(this.mThumbListener != null) {
                                this.mThumbListener.onMaxMove(Float.valueOf(this.getSelectedAbsoluteMaxValue()), Float.valueOf(this.getSelectedAbsoluteMinValue()));
                            }
                        }
                    }

                    if(this.getParent() != null) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case 3:
                    if(CustomRangeSeekBar.Thumb.MIN.equals(this.mPressedThumb) && this.mThumbListener != null) {
                        this.mThumbListener.onUpMinThumb(Float.valueOf(this.getSelectedAbsoluteMaxValue()), Float.valueOf(this.getSelectedAbsoluteMinValue()));
                    }

                    if(CustomRangeSeekBar.Thumb.MAX.equals(this.mPressedThumb) && this.mThumbListener != null) {
                        this.mThumbListener.onUpMaxThumb();
                    }

                    this.mPressedThumb = null;
                    if(this.getParent() != null) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                    }
            }

            return true;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 200;
        if(0 != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        int height = this.mThumbImage.getHeight() + this.mWordHeight;
        if(0 != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }

        this.setMeasuredDimension(width, height);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setStyle(Style.FILL);
        canvas.drawBitmap(this.mProgressBarBg, (Rect)null, this.mProgressBarRect, this.mPaint);
        this.mProgressBarSelRect.left = this.percentToScreen(this.mPercentSelectedMinValue);
        this.mProgressBarSelRect.right = this.percentToScreen(this.mPercentSelectedMaxValue);
        canvas.drawBitmap(this.mProgressBarSelBg, (Rect)null, this.mProgressBarSelRect, this.mPaint);
        this.drawThumb(this.percentToScreen(this.mPercentSelectedMinValue), CustomRangeSeekBar.Thumb.MIN.equals(this.mPressedThumb), canvas);
        this.drawThumb(this.percentToScreen(this.mPercentSelectedMaxValue), CustomRangeSeekBar.Thumb.MAX.equals(this.mPressedThumb), canvas);
        this.mPaint.setColor(Color.rgb(255, 165, 0));
        this.drawThumbMinText(this.percentToScreen(this.mPercentSelectedMinValue), Float.valueOf(this.getSelectedAbsoluteMinValue()), canvas);
        this.drawThumbMaxText(this.percentToScreen(this.mPercentSelectedMaxValue), Float.valueOf(this.getSelectedAbsoluteMaxValue()), canvas);
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        bundle.putDouble("MIN", this.mPercentSelectedMinValue);
        bundle.putDouble("MAX", this.mPercentSelectedMaxValue);
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable parcel) {
        Bundle bundle = (Bundle)parcel;
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
        this.mPercentSelectedMinValue = bundle.getDouble("MIN");
        this.mPercentSelectedMaxValue = bundle.getDouble("MAX");
    }

    private void drawThumb(float screenCoord, boolean pressed, Canvas canvas) {
        canvas.drawBitmap(this.mThumbImage, screenCoord - this.mThumbHalfWidth, (float)this.mWordHeight + 0.5F * (float)(this.getHeight() - this.mWordHeight) - this.mThumbHalfHeight, this.mPaint);
    }

    private void drawThumbMinText(float screenCoord, Number value, Canvas canvas) {
        String progress = this.getProgressStr(value.intValue());
        float progressWidth = this.mPaint.measureText(progress);
        canvas.drawText(progress, screenCoord - progressWidth / 2.0F, this.mWordSize, this.mPaint);
    }

    private void drawThumbMaxText(float screenCoord, Number value, Canvas canvas) {
        String progress = this.getProgressStr(value.intValue());
        float progressWidth = this.mPaint.measureText(progress);
        canvas.drawText(progress, screenCoord - progressWidth / 2.0F, this.mWordSize, this.mPaint);
    }

    private CustomRangeSeekBar.Thumb evalPressedThumb(float touchX) {
        CustomRangeSeekBar.Thumb result = null;
        boolean minThumbPressed = this.isInThumbRange(touchX, this.mPercentSelectedMinValue);
        boolean maxThumbPressed = this.isInThumbRange(touchX, this.mPercentSelectedMaxValue);
        if(minThumbPressed && maxThumbPressed) {
            result = touchX / (float)this.getWidth() > 0.5F?CustomRangeSeekBar.Thumb.MIN:CustomRangeSeekBar.Thumb.MAX;
        } else if(minThumbPressed) {
            result = CustomRangeSeekBar.Thumb.MIN;
        } else if(maxThumbPressed) {
            result = CustomRangeSeekBar.Thumb.MAX;
        }

        return result;
    }

    private boolean isInThumbRange(float touchX, double percentThumbValue) {
        return Math.abs(touchX - this.percentToScreen(percentThumbValue)) <= this.mThumbHalfWidth;
    }

    public void setPercentSelectedMinValue(double value) {
        this.mPercentSelectedMinValue = Math.max(0.0D, Math.min(1.0D, Math.min(value, this.mPercentSelectedMaxValue)));
        this.invalidate();
    }

    public void setPercentSelectedMaxValue(double value) {
        this.mPercentSelectedMaxValue = Math.max(0.0D, Math.min(1.0D, Math.max(value, this.mPercentSelectedMinValue)));
        this.invalidate();
    }

    private float percentToAbsoluteValue(double normalized) {
        return (float)((double)this.mAbsoluteMinValue + normalized * (double)(this.mAbsoluteMaxValue - this.mAbsoluteMinValue));
    }

    private double absoluteValueToPercent(float value) {
        return 0.0F == this.mAbsoluteMaxValue - this.mAbsoluteMinValue?0.0D:(double)((value - this.mAbsoluteMinValue) / (this.mAbsoluteMaxValue - this.mAbsoluteMinValue));
    }

    private float percentToScreen(double percentValue) {
        return (float)((double)this.mWidthPadding + percentValue * (double)((float)this.getWidth() - 2.0F * this.mWidthPadding));
    }

    private double screenToPercent(float screenCoord) {
        int width = this.getWidth();
        if((float)width <= 2.0F * this.mWidthPadding) {
            return 0.0D;
        } else {
            double result = (double)((screenCoord - this.mWidthPadding) / ((float)width - 2.0F * this.mWidthPadding));
            return Math.min(1.0D, Math.max(0.0D, result));
        }
    }

    public void setThumbListener(CustomRangeSeekBar.ThumbListener mThumbListener) {
        this.mThumbListener = mThumbListener;
    }

    private String getProgressStr(int progress) {
        String progressStr;
        if(this.mProgressTextFormat == 1) {
            progressStr = formatSecondTime(progress);
        } else {
            progressStr = String.valueOf(progress);
        }

        return progressStr;
    }

    private static String formatSecondTime(int millisecond) {
        if(millisecond == 0) {
            return "00:00";
        } else {
            int second = millisecond / 1000;
            int m = second / 60;
            int s = second % 60;
            if(m >= 60) {
                int hour = m / 60;
                int minute = m % 60;
                return hour + ":" + (minute > 9?Integer.valueOf(minute):"0" + minute) + ":" + (s > 9?Integer.valueOf(s):"0" + s);
            } else {
                return (m > 9?Integer.valueOf(m):"0" + m) + ":" + (s > 9?Integer.valueOf(s):"0" + s);
            }
        }
    }

    public static int dp2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5F);
    }

    public interface ThumbListener {
        void onClickMinThumb(Number var1, Number var2);

        void onClickMaxThumb();

        void onUpMinThumb(Number var1, Number var2);

        void onUpMaxThumb();

        void onMinMove(Number var1, Number var2);

        void onMaxMove(Number var1, Number var2);
    }

    private static enum Thumb {
        MIN,
        MAX;

        private Thumb() {
        }
    }
}

