package com.wkz.smartprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
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
    private int mProgress;
    private String mTemperatureText;
    private float mTemperatureTextSize;
    private int mTemperatureTextColor;
    private float mTemperatureUnitTextSize;
    private int mTemperatureUnitTextColor;
    private String mTimeText;
    private float mTimeHhmmssTextSize;
    private float mTimeMmssTextSize;
    private int mTimeTextColor;
    private float mProgressBarWidth;
    private float mProgressBarHeight;
    private boolean mShowTemperatureText;
    private boolean mShowTimeText;

    public ProgressBarLayout(@NonNull Context context) {
        this(context, null);
    }

    public ProgressBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
                mProgress = attributes.getInteger(R.styleable.ProgressBarLayout_pbl_progress, 0);
                mShowTemperatureText = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_show_temperature_text, false);
                mTemperatureText = attributes.getString(R.styleable.ProgressBarLayout_pbl_temperature_text);
                mTemperatureTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_temperature_text_size, 60);
                mTemperatureUnitTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_temperature_unit_text_size, 30);
                mTemperatureTextColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_temperature_text_color, 0);
                mTemperatureUnitTextColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_temperature_unit_text_color, 0);
                mShowTimeText = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_show_time_text, false);
                mTimeText = attributes.getString(R.styleable.ProgressBarLayout_pbl_time_text);
                mTimeHhmmssTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_time_hhmmss_text_size, 50);
                mTimeMmssTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_time_mmss_text_size, 60);
                mTimeTextColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_time_text_color, 0);
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
        mSpbProgress.setIsAnimated(false);
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
        LayoutParams parentLp = new LayoutParams(-2, -2);
        parentLp.gravity = Gravity.CENTER;
        mRlTemperatureParent = new RelativeLayout(context);
        mTvTemperature = new TextView(context);
        mTvTemperature.setId(520);
        RelativeLayout.LayoutParams temperatureUnitLp = new RelativeLayout.LayoutParams(-2, -2);
        temperatureUnitLp.topMargin = 20;
        temperatureUnitLp.addRule(RelativeLayout.RIGHT_OF, mTvTemperature.getId());
        mTvTemperatureUnit = new TextView(context);
        mRlTemperatureParent.addView(mTvTemperature);
        mRlTemperatureParent.addView(mTvTemperatureUnit, temperatureUnitLp);
        addView(mRlTemperatureParent, parentLp);
        mTvTemperature.setTextSize(mTemperatureTextSize);
        mTvTemperature.setTextColor(mTemperatureTextColor);
        mTvTemperatureUnit.setTextSize(mTemperatureUnitTextSize);
        mTvTemperatureUnit.setTextColor(mTemperatureUnitTextColor);
        setTemperatureText(mTemperatureText);
    }

    private void addTime(Context context) {
        LayoutParams timeLp = new LayoutParams(-2, -2);
        timeLp.gravity = Gravity.CENTER;
        mTvTime = new TextView(context);
        addView(mTvTime, timeLp);
        mTvTime.setTextColor(mTimeTextColor);
        setTimeText(mTimeText);
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

    public void setProgress(int progress) {
        this.mSpbProgress.setProgress(progress);
    }

    /**
     * 设置温度
     *
     * @param temperatureText
     */
    public void setTemperatureText(String temperatureText) {
        this.mRlTemperatureParent.setVisibility(VISIBLE);
        if (mTvTime != null) {
            this.mTvTime.setVisibility(GONE);
        }
        if (TextUtils.isEmpty(temperatureText)) {
            temperatureText = "0";
        }
        this.mTvTemperature.setText(temperatureText);
        this.mTvTemperatureUnit.setText("℃");
    }

    /**
     * 设置时间,单位"秒"
     *
     * @param timeText
     */
    public void setTimeText(String timeText) {
        if (mRlTemperatureParent != null) {
            this.mRlTemperatureParent.setVisibility(GONE);
        }
        this.mTvTime.setVisibility(VISIBLE);

        if (TextUtils.isEmpty(timeText)) {
            timeText = "0";
        }

        try {
            long time = Long.parseLong(timeText);
            if (time >= 36000) {
                this.mTvTime.setText(formatTime("HH:mm:ss", time * 1000));
                this.mTvTime.setTextSize(mTimeHhmmssTextSize);
            } else if (time >= 3600) {
                this.mTvTime.setText(formatTime("H:mm:ss", time * 1000));
                this.mTvTime.setTextSize(mTimeHhmmssTextSize);
            } else if (time >= 600) {
                this.mTvTime.setText(formatTime("mm:ss", time * 1000));
                this.mTvTime.setTextSize(mTimeMmssTextSize);
            } else {
                this.mTvTime.setText(formatTime("m:ss", time * 1000));
                this.mTvTime.setTextSize(mTimeMmssTextSize);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
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
