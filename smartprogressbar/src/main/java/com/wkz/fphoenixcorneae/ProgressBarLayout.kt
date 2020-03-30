package com.wkz.fphoenixcorneae

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.wkz.fphoenixcorneae.SmartProgressBar.ShapeStyle
import java.text.SimpleDateFormat
import java.util.*

/**
 * 进度条布局
 *
 * @author wkz
 * @date 2019-12-09 09:20
 */
class ProgressBarLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    lateinit var mSmartProgressBar: SmartProgressBar
    private var mRlTemperatureParent: RelativeLayout? = null
    private var mTvTemperature: TextView? = null
    private var mTvTemperatureUnit: TextView? = null
    private var mTvTime: TextView? = null
    private var mProgressBarBgColor = 0
    private var mProgressBarBgGradient = false
    private var mProgressBarBgAlpha = 0f
    private var mProgressStartColor = 0
    private var mProgressCenterColor = 0
    private var mProgressEndColor = 0
    private var mProgressColorsResId = 0
    private var mProgressPositionsResId = 0
    private var mClockwise = false
    private var mRadius = 0f
    private var mProgressMax = 100f
    private var mProgress = 0f
    private var mTemperature = 0
    private var mTemperatureTextSize = 0f
    private var mTemperatureTextColor = 0
    private var mTemperatureTextBold = false
    private var mTemperatureTextFontFamily = 0
    private var mTemperatureUnitTextSize = 0f
    private var mTemperatureUnitTextColor = 0
    private var mTemperatureUnitTextBold = false
    private var mTemperatureUnitResId = 0
    private var mTemperatureUnitHeight = 0f
    private var mTemperatureUnitLeftMargin = 0f
    private var mTemperatureUnitTopMargin = 0f
    private var mTime = 0
    private var mTimeHhmmssTextSize = 0f
    private var mTimeMmssTextSize = 0f
    private var mTimeTextColor = 0
    private var mTimeTextBold = false
    private var mTimeTextFontFamily = 0
    private var mProgressBarWidth = 0f
    private var mProgressBarHeight = 0f
    private var mShowTemperatureText = false
    private var mShowTimeText = false
    private var mShowShadow = true

    private fun initAttributes(
            context: Context,
            attrs: AttributeSet?
    ) {
        if (attrs != null) {
            val attributes =
                    context.theme.obtainStyledAttributes(
                            attrs,
                            R.styleable.ProgressBarLayout,
                            0,
                            0
                    )
            try {
                mProgressBarWidth = attributes.getDimension(
                        R.styleable.ProgressBarLayout_pbl_progress_bar_width,
                        580f
                )
                mProgressBarHeight = attributes.getDimension(
                        R.styleable.ProgressBarLayout_pbl_progress_bar_height,
                        580f
                )
                mProgressBarBgColor = attributes.getColor(
                        R.styleable.ProgressBarLayout_pbl_progress_bar_bg_color,
                        0
                )
                mProgressBarBgGradient = attributes.getBoolean(
                        R.styleable.ProgressBarLayout_pbl_progress_bar_bg_gradient,
                        false
                )
                mProgressBarBgAlpha = attributes.getFloat(
                        R.styleable.ProgressBarLayout_pbl_progress_bar_bg_alpha,
                        1f
                )
                mProgressStartColor = attributes.getColor(
                        R.styleable.ProgressBarLayout_pbl_progress_start_color,
                        0
                )
                mProgressCenterColor = attributes.getColor(
                        R.styleable.ProgressBarLayout_pbl_progress_center_color,
                        0
                )
                mProgressEndColor = attributes.getColor(
                        R.styleable.ProgressBarLayout_pbl_progress_end_color,
                        0
                )
                mProgressColorsResId = attributes.getResourceId(
                        R.styleable.ProgressBarLayout_pbl_progress_colors,
                        0
                )
                mProgressPositionsResId = attributes.getResourceId(
                        R.styleable.ProgressBarLayout_pbl_progress_positions,
                        0
                )
                mClockwise = attributes.getBoolean(
                        R.styleable.ProgressBarLayout_pbl_clockwise,
                        true
                )
                mRadius = attributes.getDimension(
                        R.styleable.ProgressBarLayout_pbl_radius,
                        0f
                )
                mProgressMax = attributes.getFloat(
                        R.styleable.ProgressBarLayout_pbl_progress_max,
                        100f
                )
                mProgress = attributes.getFloat(
                        R.styleable.ProgressBarLayout_pbl_progress,
                        0f
                )
                mShowTemperatureText = attributes.getBoolean(
                        R.styleable.ProgressBarLayout_pbl_show_temperature_text,
                        false
                )
                mTemperature = attributes.getInteger(
                        R.styleable.ProgressBarLayout_pbl_temperature_text,
                        0
                )
                mTemperatureTextSize = attributes.getFloat(
                        R.styleable.ProgressBarLayout_pbl_temperature_text_size,
                        60f
                )
                mTemperatureTextColor = attributes.getColor(
                        R.styleable.ProgressBarLayout_pbl_temperature_text_color,
                        0
                )
                mTemperatureTextBold = attributes.getBoolean(
                        R.styleable.ProgressBarLayout_pbl_temperature_text_bold,
                        false
                )
                mTemperatureTextFontFamily = attributes.getResourceId(
                        R.styleable.ProgressBarLayout_pbl_temperature_text_fontfamily,
                        0
                )
                mTemperatureUnitTextSize = attributes.getFloat(
                        R.styleable.ProgressBarLayout_pbl_temperature_unit_text_size,
                        30f
                )
                mTemperatureUnitTextColor = attributes.getColor(
                        R.styleable.ProgressBarLayout_pbl_temperature_unit_text_color,
                        0
                )
                mTemperatureUnitTextBold = attributes.getBoolean(
                        R.styleable.ProgressBarLayout_pbl_temperature_unit_text_bold,
                        false
                )
                mTemperatureUnitResId = attributes.getResourceId(
                        R.styleable.ProgressBarLayout_pbl_temperature_unit_res_id,
                        0
                )
                mTemperatureUnitHeight = attributes.getDimension(
                        R.styleable.ProgressBarLayout_pbl_temperature_unit_height,
                        -2f
                )
                mTemperatureUnitLeftMargin = attributes.getDimension(
                        R.styleable.ProgressBarLayout_pbl_temperature_unit_left_margin,
                        0f
                )
                mTemperatureUnitTopMargin = attributes.getDimension(
                        R.styleable.ProgressBarLayout_pbl_temperature_unit_top_margin,
                        0f
                )
                mShowTimeText = attributes.getBoolean(
                        R.styleable.ProgressBarLayout_pbl_show_time_text,
                        false
                )
                mShowShadow = attributes.getBoolean(
                        R.styleable.ProgressBarLayout_pbl_show_shadow,
                        true
                )
                mTime = attributes.getInteger(
                        R.styleable.ProgressBarLayout_pbl_time_text,
                        0
                )
                mTimeHhmmssTextSize = attributes.getFloat(
                        R.styleable.ProgressBarLayout_pbl_time_hhmmss_text_size,
                        50f
                )
                mTimeMmssTextSize = attributes.getFloat(
                        R.styleable.ProgressBarLayout_pbl_time_mmss_text_size,
                        60f
                )
                mTimeTextColor = attributes.getColor(
                        R.styleable.ProgressBarLayout_pbl_time_text_color,
                        0
                )
                mTimeTextBold = attributes.getBoolean(
                        R.styleable.ProgressBarLayout_pbl_time_text_bold,
                        false
                )
                mTimeTextFontFamily = attributes.getResourceId(
                        R.styleable.ProgressBarLayout_pbl_temperature_text_fontfamily,
                        0
                )
            } finally {
                attributes.recycle()
            }
        }
    }

    private fun initLayout(context: Context) {
        addProgressBar(context)
        if (mShowTemperatureText) {
            addTemperature(context, 0f, mProgressMax)
        }
        if (mShowTimeText) {
            addTime(context)
        }
    }

    private fun addProgressBar(context: Context) {
        val progressLp = LayoutParams(mProgressBarWidth.toInt(), mProgressBarHeight.toInt())
        progressLp.gravity = Gravity.CENTER
        mSmartProgressBar = SmartProgressBar(context)
        mSmartProgressBar.setShapeStyle(ShapeStyle.RING)
        mSmartProgressBar.setIsAnimated(false)
        mSmartProgressBar.setProgressBarBgGradient(mProgressBarBgGradient)
        mSmartProgressBar.setProgressBarBgAlpha(mProgressBarBgAlpha)
        mSmartProgressBar.setProgressColorsResId(mProgressColorsResId)
        mSmartProgressBar.setProgressPositionsResId(mProgressPositionsResId)
        setProgressBarBgColor(mProgressBarBgColor)
        setProgressStartColor(mProgressStartColor)
        setProgressCenterColor(mProgressCenterColor)
        setProgressEndColor(mProgressEndColor)
        setClockwise(mClockwise)
        setRadius(mRadius)
        mSmartProgressBar.mShowShadow = mShowShadow
        addView(mSmartProgressBar, progressLp)
    }

    @SuppressLint("ResourceType")
    private fun addTemperature(context: Context, beginTemperature: Float, endTemperature: Float) {
        mRlTemperatureParent = RelativeLayout(context)
        mRlTemperatureParent!!.id = View.generateViewId()
        val temperatureLp =
                RelativeLayout.LayoutParams(-2, -2)
        temperatureLp.addRule(
                RelativeLayout.CENTER_IN_PARENT,
                mRlTemperatureParent!!.id
        )
        mTvTemperature = TextView(context)
        mTvTemperature!!.id = View.generateViewId()
        mTvTemperature!!.paint.isFakeBoldText = mTemperatureTextBold
        mTvTemperature!!.textSize = mTemperatureTextSize
        mTvTemperature!!.setTextColor(mTemperatureTextColor)
        if (!isInEditMode && mTemperatureTextFontFamily != 0) {
            mTvTemperature!!.typeface = ResourcesCompat.getFont(
                    getContext(),
                    mTemperatureTextFontFamily
            )
        }
        val temperatureUnitLp =
                RelativeLayout.LayoutParams(-2, mTemperatureUnitHeight.toInt())
        temperatureUnitLp.addRule(RelativeLayout.RIGHT_OF, mTvTemperature!!.id)
        temperatureUnitLp.topMargin = mTemperatureUnitTopMargin.toInt()
        temperatureUnitLp.leftMargin = mTemperatureUnitLeftMargin.toInt()
        mTvTemperatureUnit = TextView(context)
        mTvTemperatureUnit!!.paint.isFakeBoldText = mTemperatureUnitTextBold
        mTvTemperatureUnit!!.textSize = mTemperatureUnitTextSize
        mTvTemperatureUnit!!.setTextColor(mTemperatureUnitTextColor)
        if (mTemperatureUnitResId != 0) {
            temperatureUnitLp.addRule(
                    RelativeLayout.ALIGN_TOP,
                    mTvTemperature!!.id
            )
            mTvTemperatureUnit!!.setBackgroundResource(mTemperatureUnitResId)
        } else {
            if (!isInEditMode && mTemperatureTextFontFamily != 0) {
                mTvTemperatureUnit!!.typeface = ResourcesCompat.getFont(
                        getContext(),
                        mTemperatureTextFontFamily
                )
            }
            mTvTemperatureUnit!!.text = "℃"
        }
        mRlTemperatureParent!!.addView(mTvTemperature, temperatureLp)
        mRlTemperatureParent!!.addView(mTvTemperatureUnit, temperatureUnitLp)
        val parentLp =
                LayoutParams(-2, -2)
        parentLp.gravity = Gravity.CENTER
        addView(mRlTemperatureParent, parentLp)

        setTemperatureText(beginTemperature, endTemperature)
    }

    private fun addTime(context: Context) {
        val timeLp =
                LayoutParams(-2, -2)
        timeLp.gravity = Gravity.CENTER
        mTvTime = TextView(context)
        addView(mTvTime, timeLp)
        mTvTime!!.paint.isFakeBoldText = mTimeTextBold
        mTvTime!!.setTextColor(mTimeTextColor)
        if (!isInEditMode && mTimeTextFontFamily != 0) {
            mTvTime!!.typeface = ResourcesCompat.getFont(
                    getContext(),
                    mTimeTextFontFamily
            )
        }

        setTimeText(mTime.toLong(), false)
    }

    fun setProgressBarBgColor(progressBarBgColor: Int) {
        mSmartProgressBar.setProgressBarBgColor(progressBarBgColor)
    }

    fun setProgressStartColor(progressStartColor: Int) {
        mSmartProgressBar.setProgressStartColor(progressStartColor)
    }

    fun setProgressCenterColor(progressCenterColor: Int) {
        mSmartProgressBar.setProgressCenterColor(progressCenterColor)
    }

    fun setProgressEndColor(progressEndColor: Int) {
        mSmartProgressBar.setProgressEndColor(progressEndColor)
    }

    fun setClockwise(clockwise: Boolean) {
        mSmartProgressBar.setClockwise(clockwise)
    }

    fun setRadius(radius: Float) {
        mSmartProgressBar.setRadius(radius)
    }

    fun setProgressWithNoAnimation(progress: Float) {
        mSmartProgressBar.setProgressWithNoAnimation(progress)
    }

    fun setProgress(progress: Float, duration: Long = 0) {
        mSmartProgressBar.setProgress(progress, duration)
    }

    fun addProgressAnimatorUpdateListener(progressAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener) {
        mSmartProgressBar.addProgressAnimatorUpdateListener(progressAnimatorUpdateListener)
    }

    fun addProgressAnimatorListener(mProgressAnimatorListener: Animator.AnimatorListener) {
        mSmartProgressBar.addProgressAnimatorListener(mProgressAnimatorListener)
    }

    var max: Float
        get() = mSmartProgressBar.getMax()
        set(max) {
            mSmartProgressBar.setMax(max)
        }

    /**
     * 暂停进度动画
     */
    fun pauseProgressAnimation() {
        mSmartProgressBar.pauseProgressAnimation()
    }

    /**
     * 恢复进度动画
     */
    fun resumeProgressAnimation() {
        mSmartProgressBar.resumeProgressAnimation()
    }

    /**
     * 取消进度动画
     */
    fun cancelProgressAnimation() {
        mSmartProgressBar.cancelProgressAnimation()
    }

    /**
     * 设置温度
     * @param beginTemperature 开始温度
     * @param endTemperature   结束温度
     */
    fun setTemperatureText(beginTemperature: Float, endTemperature: Float) {
        this.mShowTemperatureText = true
        this.mShowTimeText = false
        val isClockwise = endTemperature > beginTemperature
        this.max = when (isClockwise) {
            true -> endTemperature - beginTemperature
            else -> endTemperature
        }

        if (mTvTime != null) {
            mTvTime!!.visibility = View.GONE
        }
        if (mRlTemperatureParent != null) {
            mRlTemperatureParent!!.visibility = View.VISIBLE

            // 更新温度
            addProgressAnimatorUpdateListener(ValueAnimator.AnimatorUpdateListener {
                val animatedValue = it.animatedValue as Float
                Log.i("animatedValue", "setTemperatureText---${animatedValue}")
                when (isClockwise) {
                    true -> {
                        mTvTemperature!!.text = (beginTemperature + animatedValue).toInt().toString()
                    }
                    else -> {
                        mTvTemperature!!.text = beginTemperature.toInt().toString()
                    }
                }
            })

            // 更新圆环进度
            val progress = max
            when (isClockwise) {
                true -> {
                    setProgressWithNoAnimation(0f)
                    setProgress(progress, max.toLong() * 1000)
                }
                else -> {
                    setProgress(progress, 1000)
                }
            }
        } else {
            addTemperature(context, beginTemperature, endTemperature)
        }
    }

    /**
     * 设置时间,单位"秒"
     * @param secondTotalTime 总时长
     */
    fun setTimeText(secondTotalTime: Long, isFullReduction: Boolean = true) {
        this.mShowTemperatureText = false
        this.mShowTimeText = true

        if (mRlTemperatureParent != null) {
            mRlTemperatureParent!!.visibility = View.GONE
        }
        if (mTvTime != null) {
            mTvTime!!.visibility = View.VISIBLE
            // 更新时间
            addProgressAnimatorUpdateListener(ValueAnimator.AnimatorUpdateListener {
                val animatedValue = it.animatedValue as Float
                val time = animatedValue.toLong()
                try {
                    when {
                        time >= 10 * 60 * 60 -> {
                            // 大于十个小时
                            mTvTime!!.text = formatSecondValueTime("HH:mm:ss", time)
                            mTvTime!!.textSize = mTimeHhmmssTextSize
                        }
                        time >= 1 * 60 * 60 -> {
                            // 大于1个小时,小于十个小时
                            mTvTime!!.text = formatSecondValueTime("H:mm:ss", time)
                            mTvTime!!.textSize = mTimeHhmmssTextSize
                        }
                        time >= 10 * 60 -> {
                            // 大于十分钟,小于1个小时
                            mTvTime!!.text = formatSecondValueTime("mm:ss", time)
                            mTvTime!!.textSize = mTimeMmssTextSize
                        }
                        else -> {
                            // 小于十分钟
                            mTvTime!!.text = formatSecondValueTime("m:ss", time)
                            mTvTime!!.textSize = mTimeMmssTextSize
                        }
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            })

            // 更新圆环进度
            this.max = secondTotalTime.toFloat()
            val progress = run {
                when {
                    isFullReduction -> {
                        setProgressWithNoAnimation(max)
                        0f
                    }
                    else -> {
                        setProgressWithNoAnimation(0f)
                        max
                    }
                }
            }
            mSmartProgressBar.setIsAnimated(false)
            setProgress(progress, max.toLong() * 1000)
        } else {
            addTime(context)
        }
    }

    init {
        initAttributes(context, attrs)
        initLayout(context)
    }

    companion object {
        /**
         * @param pattern     时间格式
         * @param secondValue 秒值
         */
        private fun formatSecondValueTime(pattern: String, secondValue: Long): String {
            return formatMillisecondValueTime(pattern, secondValue * 1000)
        }

        /**
         * @param pattern          时间格式
         * @param millisecondValue 毫秒值
         */
        private fun formatMillisecondValueTime(pattern: String, millisecondValue: Long): String {
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            //设置时区，否则会有时差
            dateFormat.timeZone = TimeZone.getTimeZone("UT+08:00")
            return dateFormat.format(millisecondValue)
        }
    }
}