package com.wkz.smartprogressbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.wkz.smartprogressbar.SmartProgressBar.ShapeStyle
import java.text.SimpleDateFormat
import java.util.*

/**
 * 进度条布局
 *
 * @author wkz
 * @date 2019-12-09 09:20
 */
class ProgressBarLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    lateinit var progressBar: SmartProgressBar
        private set
    private var mRlTemperatureParent: RelativeLayout? = null
    private var mTvTemperature: TextView? = null
    private var mTvTemperatureUnit: TextView? = null
    private var mTvTime: TextView? = null
    private var mProgressBarBgColor = 0
    private var mProgressStartColor = 0
    private var mProgressCenterColor = 0
    private var mProgressEndColor = 0
    private var mClockwise = false
    private var mRadius = 0f
    private var mProgress = 0f
    private var mTemperature = 0
    private var mTemperatureTextSize = 0f
    private var mTemperatureTextColor = 0
    private var mTemperatureTextBold = false
    private var mTemperatureUnitTextSize = 0f
    private var mTemperatureUnitTextColor = 0
    private var mTemperatureUnitTextBold = false
    private var mTime = 0
    private var mTimeHhmmssTextSize = 0f
    private var mTimeMmssTextSize = 0f
    private var mTimeTextColor = 0
    private var mTimeTextBold = false
    private var mProgressBarWidth = 0f
    private var mProgressBarHeight = 0f
    private var mShowTemperatureText = false
    private var mShowTimeText = false
    private val mTypeface: Typeface
    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributes = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.ProgressBarLayout,
                    0,
                    0
            )
            try {
                mProgressBarWidth = attributes.getDimension(R.styleable.ProgressBarLayout_pbl_progress_bar_width, 580f)
                mProgressBarHeight = attributes.getDimension(R.styleable.ProgressBarLayout_pbl_progress_bar_height, 580f)
                mProgressBarBgColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_progress_bar_bg_color, 0)
                mProgressStartColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_progress_start_color, 0)
                mProgressCenterColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_progress_center_color, 0)
                mProgressEndColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_progress_end_color, 0)
                mClockwise = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_clockwise, true)
                mRadius = attributes.getDimension(R.styleable.ProgressBarLayout_pbl_radius, 0f)
                mProgress = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_progress, 0f)
                mShowTemperatureText = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_show_temperature_text, false)
                mTemperature = attributes.getInteger(R.styleable.ProgressBarLayout_pbl_temperature_text, 0)
                mTemperatureTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_temperature_text_size, 60f)
                mTemperatureTextColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_temperature_text_color, 0)
                mTemperatureTextBold = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_temperature_text_bold, false)
                mTemperatureUnitTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_temperature_unit_text_size, 30f)
                mTemperatureUnitTextColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_temperature_unit_text_color, 0)
                mTemperatureUnitTextBold = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_temperature_unit_text_bold, false)
                mShowTimeText = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_show_time_text, false)
                mTime = attributes.getInteger(R.styleable.ProgressBarLayout_pbl_time_text, 0)
                mTimeHhmmssTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_time_hhmmss_text_size, 50f)
                mTimeMmssTextSize = attributes.getFloat(R.styleable.ProgressBarLayout_pbl_time_mmss_text_size, 60f)
                mTimeTextColor = attributes.getColor(R.styleable.ProgressBarLayout_pbl_time_text_color, 0)
                mTimeTextBold = attributes.getBoolean(R.styleable.ProgressBarLayout_pbl_time_text_bold, false)
            } finally {
                attributes.recycle()
            }
        }
    }

    private fun initLayout(context: Context) {
        addProgressBar(context)
        if (mShowTemperatureText) {
            addTemperature(context)
        }
        if (mShowTimeText) {
            addTime(context)
        }
    }

    private fun addProgressBar(context: Context) {
        val progressLp = LayoutParams(mProgressBarWidth.toInt(), mProgressBarHeight.toInt())
        progressLp.gravity = Gravity.CENTER
        progressBar = SmartProgressBar(context)
        progressBar.setShapeStyle(ShapeStyle.RING)
        progressBar.setIsAnimated(true)
        setProgressBarBgColor(mProgressBarBgColor)
        setProgressStartColor(mProgressStartColor)
        setProgressCenterColor(mProgressCenterColor)
        setProgressEndColor(mProgressEndColor)
        setClockwise(mClockwise)
        setRadius(mRadius)
        setProgress(mProgress)
        addView(progressBar, progressLp)
    }

    @SuppressLint("ResourceType")
    private fun addTemperature(context: Context) {
        mRlTemperatureParent = RelativeLayout(context)
        mTvTemperature = TextView(context)
        mTvTemperature!!.id = 520
        mTvTemperature!!.paint.isFakeBoldText = mTemperatureTextBold
        mTvTemperature!!.textSize = mTemperatureTextSize
        mTvTemperature!!.setTextColor(mTemperatureTextColor)
        mTvTemperature!!.typeface = mTypeface
        val temperatureUnitLp = RelativeLayout.LayoutParams(-2, -2)
        temperatureUnitLp.topMargin = 20
        temperatureUnitLp.addRule(RelativeLayout.RIGHT_OF, mTvTemperature!!.id)
        mTvTemperatureUnit = TextView(context)
        mTvTemperatureUnit!!.paint.isFakeBoldText = mTemperatureUnitTextBold
        mTvTemperatureUnit!!.textSize = mTemperatureUnitTextSize
        mTvTemperatureUnit!!.setTextColor(mTemperatureUnitTextColor)
        mTvTemperatureUnit!!.typeface = mTypeface
        mRlTemperatureParent!!.addView(mTvTemperature)
        mRlTemperatureParent!!.addView(mTvTemperatureUnit, temperatureUnitLp)
        val parentLp = LayoutParams(-2, -2)
        parentLp.gravity = Gravity.CENTER
        addView(mRlTemperatureParent, parentLp)
        // 设置温度文本
        setTemperatureText(mTemperature.toFloat())
    }

    private fun addTime(context: Context) {
        val timeLp = LayoutParams(-2, -2)
        timeLp.gravity = Gravity.CENTER
        mTvTime = TextView(context)
        addView(mTvTime, timeLp)
        mTvTime!!.paint.isFakeBoldText = mTimeTextBold
        mTvTime!!.setTextColor(mTimeTextColor)
        mTvTime!!.typeface = mTypeface
        // 设置时间文本
        setTimeText(mTime.toFloat())
    }

    fun setProgressBarBgColor(progressBarBgColor: Int) {
        progressBar.setProgressBarBgColor(progressBarBgColor)
    }

    fun setProgressStartColor(progressStartColor: Int) {
        progressBar.setProgressStartColor(progressStartColor)
    }

    fun setProgressCenterColor(progressCenterColor: Int) {
        progressBar.setProgressCenterColor(progressCenterColor)
    }

    fun setProgressEndColor(progressEndColor: Int) {
        progressBar.setProgressEndColor(progressEndColor)
    }

    fun setClockwise(clockwise: Boolean) {
        progressBar.setClockwise(clockwise)
    }

    fun setRadius(radius: Float) {
        progressBar.setRadius(radius)
    }

    fun setProgress(progress: Float) {
        progressBar.progress = progress
    }

    fun setDuration(duration: Long) {
        progressBar.setDuration(duration)
    }

    var max: Float
        get() = progressBar.max
        set(max) {
            progressBar.max = max
        }

    /**
     * 设置温度
     *
     * @param tempValue 温度
     */
    fun setTemperatureText(tempValue: Float) {
        setTemperatureText(tempValue, false)
    }

    /**
     * 设置温度
     *
     * @param tempValue       温度
     * @param isFullReduction 是否满减,进度从100%-->0%
     */
    fun setTemperatureText(tempValue: Float, isFullReduction: Boolean) {
        var tempValue = tempValue
        if (mTvTime != null) {
            mTvTime!!.visibility = View.GONE
        }
        if (mRlTemperatureParent != null) {
            mRlTemperatureParent!!.visibility = View.VISIBLE
            if (tempValue < 0) {
                tempValue = 0f
            }
            mTvTemperature?.text = tempValue.toInt().toString()
            mTvTemperatureUnit!!.text = "℃"
            if (isFullReduction) {
                setProgress(max - tempValue)
            } else {
                setProgress(tempValue)
            }
        } else {
            addTemperature(context)
        }
    }

    /**
     * 设置时间,单位"秒"
     *
     * @param timeValue 时间
     */
    fun setTimeText(timeValue: Float) {
        setTimeText(timeValue, true)
    }

    /**
     * 设置时间,单位"秒"
     *
     * @param timeValue       时间
     * @param isFullReduction 是否满减,进度从100%-->0%
     */
    fun setTimeText(timeValue: Float, isFullReduction: Boolean) {
        var timeValue = timeValue
        if (mRlTemperatureParent != null) {
            mRlTemperatureParent!!.visibility = View.GONE
        }
        if (mTvTime != null) {
            mTvTime!!.visibility = View.VISIBLE
            if (timeValue < 0) {
                timeValue = 0f
            }
            try {
                if (timeValue >= 10 * 60 * 60) { // 10小时以上
                    mTvTime!!.text = formatTime("HH:mm:ss", (timeValue * 1000).toLong())
                    mTvTime!!.textSize = mTimeHhmmssTextSize
                } else if (timeValue >= 1 * 60 * 60) { // 1小时以上
                    mTvTime!!.text = formatTime("H:mm:ss", (timeValue * 1000).toLong())
                    mTvTime!!.textSize = mTimeHhmmssTextSize
                } else if (timeValue >= 10 * 60) { // 10分钟以上
                    mTvTime!!.text = formatTime("mm:ss", (timeValue * 1000).toLong())
                    mTvTime!!.textSize = mTimeMmssTextSize
                } else { // 10分钟以内
                    mTvTime!!.text = formatTime("m:ss", (timeValue * 1000).toLong())
                    mTvTime!!.textSize = mTimeMmssTextSize
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            if (isFullReduction) {
                setProgress(timeValue)
            } else {
                setProgress(max - timeValue)
            }
        } else {
            addTime(context)
        }
    }

    /**
     * @param pattern         时间格式
     * @param milisecondValue 毫秒值
     */
    fun formatTime(pattern: String?, milisecondValue: Long): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        //设置时区，否则会有时差
        dateFormat.timeZone = TimeZone.getTimeZone("UT+08:00")
        return dateFormat.format(milisecondValue)
    }

    init {
        mTypeface = Typeface.createFromAsset(getContext().assets, "fonts/dincond_boldalternate.ttf")
        initAttributes(context, attrs)
        initLayout(context)
    }
}