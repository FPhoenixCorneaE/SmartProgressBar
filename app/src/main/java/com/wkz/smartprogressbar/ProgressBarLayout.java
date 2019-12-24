package com.wkz.smartprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 进度条布局
 *
 * @author wkz
 * @date 2019-12-09 09:20
 */
public class ProgressBarLayout extends FrameLayout {

    private SmartProgressBar mSpbProgress;
    private RelativeLayout mRlTemperatureParent;
    private TextView mTvTemperature;
    private TextView mTvTemperatureUnit;
    private TextView mTvTime;
    private int mProgressBarBgColor;
    private int mProgressStartColor;
    private int mProgressCenterColor;
    private int mProgressEndColor;
    private boolean mClockwise;
    private float mRadius;
    private float mProgress;
    private int mTemperature;
    private float mTemperatureTextSize;
    private int mTemperatureTextColor;
    private boolean mTemperatureTextBold;
    private float mTemperatureUnitTextSize;
    private int mTemperatureUnitTextColor;
    private boolean mTemperatureUnitTextBold;
    private int mTime;
    private float mTimeHhmmssTextSize;
    private float mTimeMmssTextSize;
    private int mTimeTextColor;
    private boolean mTimeTextBold;
    private float mProgressBarWidth;
    private float mProgressBarHeight;
    private boolean mShowTemperatureText;
    private boolean mShowTimeText;
    private Typeface mTypeface;

    public ProgressBarLayout(@NonNull Context context) {
        this(context, null);
    }

    public ProgressBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/dincond_boldalternate.ttf");
        initAttributes(context, attrs);
        initLayout(context);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ProgressBarLayout,
                    0,
                    0
            );
            try {
                mProgressBarWidth = attributes.getDimension(R.styleable.ProgressBarLayout_pbl_progress_bar_width, 580);
                mProgressBarHeight = attributes.getDimension(R.styleable.ProgressBarLayout_pbl_progress_bar_height, 580);
                mProgressBarBgColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_progress_bar_bg_color, 0);
                mProgressStartColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_progress_start_color, 0);
                mProgressCenterColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_progress_center_color, 0);
                mProgressEndColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_progress_end_color, 0);
                mClockwise = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_clockwise, true);
                mRadius = attributes.getDimension(R.styleable.ProgressBarLayout_pbl_radius, 0);
                mProgress = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_progress, 0F);
                mShowTemperatureText = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_show_temperature_text, false);
                mTemperature = attributes.getInteger(R.styleable.ProgressBarLayout_pbl_temperature_text, 0);
                mTemperatureTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_temperature_text_size, 60);
                mTemperatureTextColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_temperature_text_color, 0);
                mTemperatureTextBold = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_temperature_text_bold, false);
                mTemperatureUnitTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_temperature_unit_text_size, 30);
                mTemperatureUnitTextColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_temperature_unit_text_color, 0);
                mTemperatureUnitTextBold = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_temperature_unit_text_bold, false);
                mShowTimeText = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_show_time_text, false);
                mTime = attributes.getInteger(R.styleable.ProgressBarLayout_pbl_time_text, 0);
                mTimeHhmmssTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_time_hhmmss_text_size, 50);
                mTimeMmssTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_time_mmss_text_size, 60);
                mTimeTextColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_time_text_color, 0);
                mTimeTextBold = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_time_text_bold, false);
            } finally {
                attributes.recycle();
            }
        }
    }

    private void initLayout(Context context) {
        addProgressBar(context);
        if (mShowTemperatureText) {
            addTemperature(context);
        }
        if (mShowTimeText) {
            addTime(context);
        }
    }

    private void addProgressBar(Context context) {
        LayoutParams progressLp = new LayoutParams((int) mProgressBarWidth, (int) mProgressBarHeight);
        progressLp.gravity = Gravity.CENTER;
        mSpbProgress = new SmartProgressBar(context);
        mSpbProgress.setShapeStyle(SmartProgressBar.ShapeStyle.RING);
        mSpbProgress.setIsAnimated(true);
        setProgressBarBgColor(mProgressBarBgColor);
        setProgressStartColor(mProgressStartColor);
        setProgressCenterColor(mProgressCenterColor);
        setProgressEndColor(mProgressEndColor);
        setClockwise(mClockwise);
        setRadius(mRadius);
        setProgress(mProgress);
        addView(mSpbProgress, progressLp);
    }

    @SuppressLint("ResourceType")
    private void addTemperature(Context context) {
        mRlTemperatureParent = new RelativeLayout(context);
        mTvTemperature = new TextView(context);
        mTvTemperature.setId(520);
        mTvTemperature.getPaint().setFakeBoldText(mTemperatureTextBold);
        mTvTemperature.setTextSize(mTemperatureTextSize);
        mTvTemperature.setTextColor(mTemperatureTextColor);
        mTvTemperature.setTypeface(mTypeface);

        RelativeLayout.LayoutParams temperatureUnitLp = new RelativeLayout.LayoutParams(-2, -2);
        temperatureUnitLp.topMargin = 20;
        temperatureUnitLp.addRule(RelativeLayout.RIGHT_OF, mTvTemperature.getId());
        mTvTemperatureUnit = new TextView(context);
        mTvTemperatureUnit.getPaint().setFakeBoldText(mTemperatureUnitTextBold);
        mTvTemperatureUnit.setTextSize(mTemperatureUnitTextSize);
        mTvTemperatureUnit.setTextColor(mTemperatureUnitTextColor);
        mTvTemperatureUnit.setTypeface(mTypeface);

        mRlTemperatureParent.addView(mTvTemperature);
        mRlTemperatureParent.addView(mTvTemperatureUnit, temperatureUnitLp);

        LayoutParams parentLp = new LayoutParams(-2, -2);
        parentLp.gravity = Gravity.CENTER;
        addView(mRlTemperatureParent, parentLp);

        // 设置温度文本
        setTemperatureText(mTemperature);
    }

    private void addTime(Context context) {
        LayoutParams timeLp = new LayoutParams(-2, -2);
        timeLp.gravity = Gravity.CENTER;
        mTvTime = new TextView(context);
        addView(mTvTime, timeLp);
        mTvTime.getPaint().setFakeBoldText(mTimeTextBold);
        mTvTime.setTextColor(mTimeTextColor);
        mTvTime.setTypeface(mTypeface);

        // 设置时间文本
        setTimeText(mTime);
    }

    public void setProgressBarBgColor(int progressBarBgColor) {
        this.mSpbProgress.setProgressBarBgColor(progressBarBgColor);
    }

    public void setProgressStartColor(int progressStartColor) {
        this.mSpbProgress.setProgressStartColor(progressStartColor);
    }

    public void setProgressCenterColor(int progressCenterColor) {
        this.mSpbProgress.setProgressCenterColor(progressCenterColor);
    }

    public void setProgressEndColor(int progressEndColor) {
        this.mSpbProgress.setProgressEndColor(progressEndColor);
    }

    public void setClockwise(boolean clockwise) {
        this.mSpbProgress.setClockwise(clockwise);
    }

    public void setRadius(float radius) {
        this.mSpbProgress.setRadius(radius);
    }

    public void setProgress(float progress) {
        this.mSpbProgress.setProgress(progress);
    }

    public void setMax(float max) {
        this.mSpbProgress.setMax(max);
    }

    public void setDuration(long duration) {
        this.mSpbProgress.setDuration(duration);
    }

    public float getMax() {
        return this.mSpbProgress.getMax();
    }

    public SmartProgressBar getProgressBar() {
        return this.mSpbProgress;
    }

    /**
     * 设置温度
     *
     * @param tempValue 温度
     */
    public void setTemperatureText(float tempValue) {
        setTemperatureText(tempValue, false);
    }

    /**
     * 设置温度
     *
     * @param tempValue       温度
     * @param isFullReduction 是否满减,进度从100%-->0%
     */
    public void setTemperatureText(float tempValue, boolean isFullReduction) {
        if (mTvTime != null) {
            this.mTvTime.setVisibility(GONE);
        }
        if (mRlTemperatureParent != null) {
            this.mRlTemperatureParent.setVisibility(VISIBLE);
            if (tempValue < 0) {
                tempValue = 0;
            }
            this.mTvTemperature.setText(String.valueOf((int) tempValue));
            this.mTvTemperatureUnit.setText("℃");

            if (isFullReduction) {
                setProgress(getMax() - tempValue);
            } else {
                setProgress(tempValue);
            }
        } else {
            addTemperature(getContext());
        }
    }

    /**
     * 设置时间,单位"秒"
     *
     * @param timeValue 时间
     */
    public void setTimeText(float timeValue) {
        setTimeText(timeValue, true);
    }

    /**
     * 设置时间,单位"秒"
     *
     * @param timeValue       时间
     * @param isFullReduction 是否满减,进度从100%-->0%
     */
    public void setTimeText(float timeValue, boolean isFullReduction) {
        if (mRlTemperatureParent != null) {
            this.mRlTemperatureParent.setVisibility(GONE);
        }

        if (mTvTime != null) {
            this.mTvTime.setVisibility(VISIBLE);

            if (timeValue < 0) {
                timeValue = 0;
            }

            try {
                if (timeValue >= 10 * 60 * 60) {
                    // 10小时以上
                    this.mTvTime.setText(formatTime("HH:mm:ss", (long) (timeValue * 1000)));
                    this.mTvTime.setTextSize(mTimeHhmmssTextSize);
                } else if (timeValue >= 1 * 60 * 60) {
                    // 1小时以上
                    this.mTvTime.setText(formatTime("H:mm:ss", (long) (timeValue * 1000)));
                    this.mTvTime.setTextSize(mTimeHhmmssTextSize);
                } else if (timeValue >= 10 * 60) {
                    // 10分钟以上
                    this.mTvTime.setText(formatTime("mm:ss", (long) (timeValue * 1000)));
                    this.mTvTime.setTextSize(mTimeMmssTextSize);
                } else {
                    // 10分钟以内
                    this.mTvTime.setText(formatTime("m:ss", (long) (timeValue * 1000)));
                    this.mTvTime.setTextSize(mTimeMmssTextSize);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (isFullReduction) {
                setProgress((float) timeValue);
            } else {
                setProgress(getMax() - timeValue);
            }
        } else {
            addTime(getContext());
        }
    }

    /**
     * @param pattern         时间格式
     * @param milisecondValue 毫秒值
     */
    public String formatTime(String pattern, long milisecondValue) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        //设置时区，否则会有时差
        dateFormat.setTimeZone(TimeZone.getTimeZone("UT+08:00"));
        return dateFormat.format(milisecondValue);
    }
}