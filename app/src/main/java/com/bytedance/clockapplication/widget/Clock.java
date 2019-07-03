package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import java.util.Calendar;
import java.util.Locale;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private int mWidth, mCenterX, mCenterY, mRadius;

    /**
     * properties
     */
    private int centerInnerColor = DEFAULT_SECONDARY_COLOR;
    private int centerOuterColor = DEFAULT_PRIMARY_COLOR;

    private int secondsNeedleColor;
    private int hoursNeedleColor;
    private int minutesNeedleColor;

    //******************************
    private Paint mHourPaint = new Paint();
    private Paint mMinutePaint = new Paint();
    private Paint mSecondPaint = new Paint();
    private Paint mInner = new Paint();
    private Paint mOuter = new Paint();
    private Calendar mcalendar;
    //*******************************

    private float secondPointerLength = 350f;
    private float minutePointerLength = 240f;
    private float hourPointerLength = 180f;

    private int degreesColor;

    private int hoursValuesColor;

    private int numbersColor;

    private boolean mShowAnalog = true;

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }
        mhandler.sendEmptyMessage(0x23);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private float[] calculatePoint(float angle,float length){
        float[] points = new float[4];
        if(angle <= 90f){
            points[0] = -(float) Math.sin(angle*Math.PI/180) * 40f;
            points[1] = (float) Math.cos(angle*Math.PI/180) * 40f;
            points[2] = (float) Math.sin(angle*Math.PI/180) * length;
            points[3] = -(float) Math.cos(angle*Math.PI/180) * length;
        }else if(angle <= 180f){
            points[0] = -(float) Math.cos((angle-90)*Math.PI/180) * 40f;
            points[1] = -(float) Math.sin((angle-90)*Math.PI/180) * 40f;
            points[2] = (float) Math.cos((angle-90)*Math.PI/180) * length;
            points[3] = (float) Math.sin((angle-90)*Math.PI/180) * length;
        }else if(angle <= 270f){
            points[0] = (float) Math.sin((angle-180)*Math.PI/180) * 40f;
            points[1] = -(float) Math.cos((angle-180)*Math.PI/180) * 40f;
            points[2] = -(float) Math.sin((angle-180)*Math.PI/180) * length;
            points[3] = (float) Math.cos((angle-180)*Math.PI/180) * length;
        }else if(angle <= 360f){
            points[0] = (float) Math.cos((angle-270)*Math.PI/180) * 40f;
            points[1] = (float) Math.sin((angle-270)*Math.PI/180) * 40f;
            points[2] = -(float) Math.cos((angle-270)*Math.PI/180) * length;
            points[3] = -(float) Math.sin((angle-270)*Math.PI/180) * length;
        }
        return points;
    }

    private void init(Context context, AttributeSet attrs) {

        this.centerInnerColor = Color.LTGRAY;
        this.centerOuterColor = DEFAULT_PRIMARY_COLOR;

        this.secondsNeedleColor = DEFAULT_SECONDARY_COLOR;
        this.hoursNeedleColor = DEFAULT_PRIMARY_COLOR;
        this.minutesNeedleColor = DEFAULT_PRIMARY_COLOR;

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        this.hoursValuesColor = DEFAULT_PRIMARY_COLOR;

        numbersColor = Color.WHITE;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;

        if (mShowAnalog) {
            drawDegrees(canvas);
            drawHoursValues(canvas);
            drawNeedles(canvas);
            drawCenter(canvas);
        } else {
            drawNumbers(canvas);
        }

    }

    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                paint.setAlpha(CUSTOM_ALPHA);
            else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * @param canvas
     */
    private void drawNumbers(Canvas canvas) {

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.2f);
        textPaint.setColor(numbersColor);
        textPaint.setColor(numbersColor);
        textPaint.setAntiAlias(true);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        String time = String.format("%s:%s:%s%s",
                String.format(Locale.getDefault(), "%02d", hour),
                String.format(Locale.getDefault(), "%02d", minute),
                String.format(Locale.getDefault(), "%02d", second),
                amPm == AM ? "AM" : "PM");

        SpannableStringBuilder spannableString = new SpannableStringBuilder(time);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), spannableString.toString().length() - 2, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // se superscript percent

        StaticLayout layout = new StaticLayout(spannableString, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);
        canvas.translate(mCenterX - layout.getWidth() / 2f, mCenterY - layout.getHeight() / 2f);
        layout.draw(canvas);
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        Paint hvPoint = new Paint();
        hvPoint.setColor(DEFAULT_PRIMARY_COLOR);
        hvPoint.setTextSize(mWidth * 0.08f);
        hvPoint.setTextAlign(Paint.Align.CENTER);
        canvas.translate(mCenterX,mCenterY);
        hvPoint.setStyle(Paint.Style.FILL);

        canvas.save();
        for (int i = 0; i < 12; i++) {
            String degree = (i + 1) + "";
            float[] temp = calculatePoint((i + 1) * 30, mRadius - hvPoint.getTextSize() / 2 - 40f - 10);
            canvas.drawText(degree, temp[2], temp[3] + hvPoint.getTextSize() / 2, hvPoint);
        }
    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        // Default Color:
        // - secondsNeedleColor
        // - hoursNeedleColor
        // - minutesNeedleColor
        mHourPaint.setColor(hoursNeedleColor);
        mHourPaint.setStyle(Paint.Style.FILL);
        mHourPaint.setStrokeWidth(10f);

        mMinutePaint.setColor(minutesNeedleColor);
        mMinutePaint.setStyle(Paint.Style.FILL);
        mMinutePaint.setStrokeWidth(10f);

        mSecondPaint.setColor(secondsNeedleColor);
        mSecondPaint.setStyle(Paint.Style.FILL);
        mSecondPaint.setStrokeWidth(10f);

        Calendar calendar = Calendar.getInstance();
        float[] hourPoint = calculatePoint(calendar.get(Calendar.HOUR_OF_DAY) % 12 / 12f * 360, hourPointerLength);
        float[] minutePoint = calculatePoint(calendar.get(Calendar.MINUTE) / 60f * 360, minutePointerLength);
        float[] secondPoint = calculatePoint(calendar.get(Calendar.SECOND) / 60f * 360, secondPointerLength);
        canvas.drawLine(hourPoint[0], hourPoint[1], hourPoint[2],hourPoint[3], mHourPaint);
        canvas.drawLine(minutePoint[0], minutePoint[1], minutePoint[2], minutePoint[3], mMinutePaint);
        canvas.drawLine(secondPoint[0], secondPoint[1], secondPoint[2], secondPoint[3], mSecondPaint);
    }

    /**
     * Draw Center Dot
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        // Default Color:
        // - centerInnerColor
        // - centerOuterColor
        mInner.setColor(centerInnerColor);
        mInner.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, 20, mInner);

        mOuter.setColor(centerOuterColor);
        mOuter.setStyle(Paint.Style.STROKE);
        mOuter.setStrokeWidth(10f);
        canvas.drawCircle(0, 0, 20, mOuter);

    }

    public void setShowAnalog(boolean showAnalog) {
        mShowAnalog = showAnalog;
        invalidate();
    }

    public boolean isShowAnalog() {
        return mShowAnalog;
    }

    private Handler mhandler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x23:
                    mcalendar=Calendar.getInstance();
                    invalidate();
                    mhandler.sendEmptyMessageDelayed(0x23, 1000);
                    break;

                default:
                    break;
            }
        };
    };
}