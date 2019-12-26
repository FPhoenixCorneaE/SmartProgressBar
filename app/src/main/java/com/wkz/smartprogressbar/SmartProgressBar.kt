package com.wkz.smartprogressbar

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * 自定义的进度条
 * 样式风格有水平、竖直、圆环、扇形、......
 *
 * @author wkz
 * @date 2019/05/05 21:23
 */
class SmartProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    @Target(AnnotationTarget.FIELD)
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    annotation class ShapeStyle {
        companion object {
            /**
             * 水平样式
             */
            var HORIZONTAL = 0
            /**
             * 竖直样式
             */
            var VERTICAL = 1
            /**
             * 圆环样式
             */
            var RING = 2
            /**
             * 扇形样式
             */
            var SECTOR = 3
        }
    }

    /**
     * 进度条背景颜色
     */
    private var mProgressBarBgColor = DEFAULT_PROGRESS_BAR_BG_COLOR
    /**
     * 进度颜色
     */
    private var mProgressStartColor = DEFAULT_PROGRESS_COLOR
    private var mProgressCenterColor = DEFAULT_PROGRESS_COLOR
    private var mProgressEndColor = DEFAULT_PROGRESS_COLOR
    /**
     * 边框颜色
     */
    private var mBorderColor = DEFAULT_BORDER_COLOR
    /**
     * 边框宽度
     */
    private var mBorderWidth = 0F
    /**
     * 进度提示文字大小
     */
    private var mPercentTextSize = DEFAULT_PERCENT_TEXT_SIZE
    /**
     * 进度提示文字颜色
     */
    private var mPercentTextColor = DEFAULT_PERCENT_TEXT_COLOR
    /**
     * 进度条中心X坐标
     */
    private var mCenterX = 0F
    /**
     * 进度条中心Y坐标
     */
    private var mCenterY = 0F
    /**
     * 进度条样式
     */
    private var mShapeStyle = ShapeStyle.HORIZONTAL
    /**
     * 水平、竖直进度条圆角半径；圆环/扇形内圆半径
     */
    private var mRadius = 0F
    /**
     * 圆环/扇形是否顺时针方向绘制
     */
    private var mClockwise = true
    /**
     * 水平、竖直进度条圆角半径
     */
    private var mTopLeftRadius = 0F
    private var mTopRightRadius = 0F
    private var mBottomLeftRadius = 0F
    private var mBottomRightRadius = 0F
    private lateinit var mRadii: FloatArray
    /**
     * 进度最大值
     */
    var max: Float = DEFAULT_MAX
    /**
     * 进度值
     */
    var progress: Float = 0F
    /**
     * 进度文字是否显示百分号
     */
    private var mIsShowPercentSign = false
    /**
     * 进度文字是否显示
     */
    private var mIsShowPercentText = false
    /**
     * 进度画笔
     */
    private var mProgressPaint: Paint? = null
    private var mEndProgressPaint: Paint? = null
    /**
     * 进度条背景画笔
     */
    private var mProgressBarBgPaint: Paint? = null
    /**
     * 进度百分比字体画笔
     */
    private var mPercentTextPaint: Paint? = null
    /**
     * 边框画笔
     */
    private var mBorderPaint: Paint? = null
    /**
     * 绘制路径
     */
    private var mPath: Path? = null
    /**
     * 是否执行动画
     */
    private var mIsAnimated = true
    private var mAnimator: ValueAnimator? = null
    private var mProgressAnimator: ValueAnimator? = null
    /**
     * 动画持续时间,毫秒值
     */
    private var mDuration: Long = DEFAULT_ANIMATION_DURATION
    private var mAnimatorUpdateListener: AnimatorUpdateListener? = null
    /**
     * 初始化自定义属性
     */
    private fun initAttributes(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.SmartProgressBar, defStyleAttr, 0)
        try {
            max = attributes.getFloat(R.styleable.SmartProgressBar_spb_max, DEFAULT_MAX)
            progress = attributes.getFloat(R.styleable.SmartProgressBar_spb_progress, 0f)
            mProgressStartColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_start_color, DEFAULT_PROGRESS_COLOR)
            mProgressCenterColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_center_color, DEFAULT_PROGRESS_COLOR)
            mProgressEndColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_end_color, DEFAULT_PROGRESS_COLOR)
            mProgressBarBgColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_bar_bg_color, DEFAULT_PROGRESS_BAR_BG_COLOR)
            mIsShowPercentText = attributes.getBoolean(R.styleable.SmartProgressBar_spb_show_percent_text, false)
            mIsShowPercentSign = attributes.getBoolean(R.styleable.SmartProgressBar_spb_show_percent_sign, false)
            mPercentTextColor = attributes.getColor(R.styleable.SmartProgressBar_spb_percent_text_color, DEFAULT_PERCENT_TEXT_COLOR)
            mPercentTextSize = attributes.getDimension(R.styleable.SmartProgressBar_spb_percent_text_size, DEFAULT_PERCENT_TEXT_SIZE)
            mBorderColor = attributes.getColor(R.styleable.SmartProgressBar_spb_border_color, DEFAULT_BORDER_COLOR)
            mBorderWidth = attributes.getDimension(R.styleable.SmartProgressBar_spb_border_width, 0f)
            mRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_radius, 0f)
            mClockwise = attributes.getBoolean(R.styleable.SmartProgressBar_spb_clockwise, true)
            mTopLeftRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_top_left_radius, 0f)
            mTopRightRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_top_right_radius, 0f)
            mBottomLeftRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_bottom_left_radius, 0f)
            mBottomRightRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_bottom_right_radius, 0f)
            mShapeStyle = attributes.getInt(R.styleable.SmartProgressBar_spb_shape_style, 0)
            mIsAnimated = attributes.getBoolean(R.styleable.SmartProgressBar_spb_animated, true)
            mDuration = attributes.getInt(R.styleable.SmartProgressBar_spb_animated_duration, DEFAULT_ANIMATION_DURATION.toInt()).toLong()
            if (max <= 0) {
                max = DEFAULT_MAX
            }
            if (progress > max) {
                progress = max
            } else if (progress < 0) {
                progress = 0F
            }
        } finally {
            attributes.recycle()
        }
    }

    /**
     * 初始化
     */
    private fun init() {
        /*关闭硬件加速,否则没有阴影效果*/
        setLayerType(LAYER_TYPE_HARDWARE, null)
        /*进度画笔*/
        mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressPaint!!.style = Paint.Style.FILL
        mEndProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        /*进度条背景画笔*/
        mProgressBarBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressBarBgPaint!!.color = mProgressBarBgColor
        /*边框画笔*/
        mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBorderPaint!!.color = mBorderColor
        mBorderPaint!!.style = Paint.Style.STROKE
        mBorderPaint!!.strokeWidth = mBorderWidth
        /*进度百分比字体画笔*/
        mPercentTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPercentTextPaint!!.color = mPercentTextColor
        mPercentTextPaint!!.style = Paint.Style.FILL
        mPercentTextPaint!!.textSize = mPercentTextSize
        /*若是设置了radius属性，四个圆角属性值以radius属性值为准*/
        if (mRadius > 0) {
            mBottomRightRadius = mRadius
            mBottomLeftRadius = mBottomRightRadius
            mTopRightRadius = mBottomLeftRadius
            mTopLeftRadius = mTopRightRadius
        }
        /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
        mRadii = floatArrayOf(
                mTopLeftRadius, mTopLeftRadius,
                mTopRightRadius, mTopRightRadius,
                mBottomRightRadius, mBottomRightRadius,
                mBottomLeftRadius, mBottomLeftRadius
        )
        /*绘制路径*/
        mPath = Path()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 是否执行动画
        if (mIsAnimated) {
            // 开始动画
            startAnimating()
        }
    }

    /**
     * 开始动画
     */
    private fun startAnimating() {
        // 设置属性动画参数
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0f, progress)
            mAnimator!!.repeatCount = 0
            mAnimator!!.repeatMode = ValueAnimator.RESTART
            mAnimator!!.interpolator = DecelerateInterpolator()
            mAnimator!!.duration = mDuration
            // 设置动画的回调
            mAnimatorUpdateListener = AnimatorUpdateListener { animation ->
                progress = animation.animatedValue as Float
                postInvalidate()
            }
            mAnimator!!.addUpdateListener(mAnimatorUpdateListener)
        }
        post { mAnimator!!.start() }
    }

    /**
     * 停止动画
     */
    private fun pauseAnimating() {
        mAnimator?.pause()
    }

    /**
     * 取消动画
     */
    private fun cancelAnimating() {
        mAnimator?.cancel()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 取消动画
        cancelAnimating()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int
        width = when (widthSpecMode) {
            MeasureSpec.AT_MOST -> {
                context.dp2px(DEFAULT_WIDTH).toInt()
            }
            MeasureSpec.UNSPECIFIED -> {
                context.dp2px(DEFAULT_WIDTH).toInt()
            }
            else -> {
                widthSpecSize
            }
        }
        height = when (heightSpecMode) {
            MeasureSpec.AT_MOST -> {
                context.dp2px(DEFAULT_HEIGHT).toInt()
            }
            MeasureSpec.UNSPECIFIED -> {
                context.dp2px(DEFAULT_HEIGHT).toInt()
            }
            else -> {
                heightSpecSize
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = width.toFloat() / 2
        mCenterY = height.toFloat() / 2
        when (mShapeStyle) {
            ShapeStyle.HORIZONTAL -> {
                /*创建线性颜色渐变器*/
                val linearGradient: Shader = LinearGradient(
                        mBorderWidth,
                        (height - mBorderWidth * 2) / 2,
                        mBorderWidth + progress / max * (width - mBorderWidth * 2),
                        (height - mBorderWidth * 2) / 2, intArrayOf(mProgressStartColor, mProgressCenterColor, mProgressEndColor), floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.MIRROR
                )
                mProgressPaint!!.shader = linearGradient
            }
            ShapeStyle.VERTICAL -> {
                /*创建线性颜色渐变器*/
                val linearGradient: Shader = LinearGradient(
                        (width - mBorderWidth * 2) / 2,
                        height - mBorderWidth,
                        (width - mBorderWidth * 2) / 2,
                        height - progress / max * (height - mBorderWidth * 2) - mBorderWidth, intArrayOf(mProgressStartColor, mProgressCenterColor, mProgressEndColor), floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.MIRROR
                )
                mProgressPaint!!.shader = linearGradient
            }
            ShapeStyle.RING -> {
                /*创建扫描式渐变器*/
                val colors: IntArray
                val positions: FloatArray
                if (mClockwise) {
                    colors = intArrayOf(
                            mProgressStartColor,
                            mProgressCenterColor,
                            mProgressEndColor,
                            mProgressStartColor
                    )
                    positions = floatArrayOf(0f, 0.45f, 0.9f, 1f)
                } else {
                    colors = intArrayOf(
                            mProgressEndColor,
                            mProgressEndColor,
                            mProgressCenterColor,
                            mProgressStartColor
                    )
                    positions = floatArrayOf(0f, 0.3f, 0.6f, 1f)
                }
                val sweepGradient: Shader = SweepGradient(
                        0F,
                        0F,
                        colors,
                        positions
                )
                mProgressPaint!!.shader = sweepGradient
                val gradientMatrix = Matrix()
                gradientMatrix.setTranslate(mCenterX, mCenterY)
                sweepGradient.setLocalMatrix(gradientMatrix)
            }
            ShapeStyle.SECTOR -> {
                /*创建扫描式渐变器*/
                val colors: IntArray = if (mClockwise) {
                    intArrayOf(
                            mProgressStartColor,
                            mProgressCenterColor,
                            mProgressEndColor
                    )
                } else {
                    intArrayOf(
                            mProgressEndColor,
                            mProgressCenterColor,
                            mProgressStartColor
                    )
                }
                val sweepGradient: Shader = SweepGradient(
                        0F,
                        0F,
                        colors,
                        null
                )
                mProgressPaint!!.shader = sweepGradient
                val gradientMatrix = Matrix()
                gradientMatrix.setTranslate(mCenterX, mCenterY)
                sweepGradient.setLocalMatrix(gradientMatrix)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        when (mShapeStyle) {
            ShapeStyle.HORIZONTAL ->
                // 绘制水平进度条
                drawHorizontalProgressBar(canvas)
            ShapeStyle.VERTICAL ->
                // 绘制竖直进度条
                drawVerticalProgressBar(canvas)
            ShapeStyle.RING ->
                // 绘制圆环进度条
                drawRingProgressBar(canvas)
            ShapeStyle.SECTOR ->
                // 绘制扇形扫描式进度
                drawSectorProgressBar(canvas)
            else -> {
            }
        }
        canvas.restore()
        mPath!!.reset()
        super.onDraw(canvas)
    }

    /**
     * 绘制水平进度条
     *
     * @param canvas 画布
     */
    private fun drawHorizontalProgressBar(canvas: Canvas) {
        // 绘制进度条背景
        mPath!!.addRoundRect(RectF(
                mBorderWidth / 2,
                mBorderWidth / 2,
                width - mBorderWidth / 2,
                height - mBorderWidth / 2),
                mRadii, Path.Direction.CW)
        canvas.drawPath(mPath!!, mProgressBarBgPaint!!)
        // 绘制边框
        if (mBorderWidth > 0) {
            canvas.drawPath(mPath!!, mBorderPaint!!)
        }
        // 绘制进度
        mPath!!.reset()
        mPath!!.addRoundRect(
                RectF(
                        mBorderWidth,
                        mBorderWidth,
                        mBorderWidth + progress / max * (width - mBorderWidth * 2),
                        height - mBorderWidth
                ),
                mRadii,
                Path.Direction.CW
        )
        canvas.drawPath(mPath!!, mProgressPaint!!)
        if (mIsShowPercentText) {
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas)
        }
    }

    /**
     * 绘制竖直进度条
     *
     * @param canvas 画布
     */
    private fun drawVerticalProgressBar(canvas: Canvas) {
        // 绘制进度条背景
        mPath!!.addRoundRect(RectF(
                mBorderWidth / 2,
                mBorderWidth / 2,
                width - mBorderWidth / 2,
                height - mBorderWidth / 2),
                mRadii, Path.Direction.CW)
        canvas.drawPath(mPath!!, mProgressBarBgPaint!!)
        // 绘制边框
        if (mBorderWidth > 0) {
            canvas.drawPath(mPath!!, mBorderPaint!!)
        }
        // 绘制进度
        mPath!!.reset()
        mPath!!.addRoundRect(RectF(
                mBorderWidth,
                height - progress / max * (height - mBorderWidth * 2) - mBorderWidth,
                width - mBorderWidth,
                height - mBorderWidth),
                mRadii, Path.Direction.CW)
        canvas.drawPath(mPath!!, mProgressPaint!!)
        if (mIsShowPercentText) {
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas)
        }
    }

    /**
     * 绘制圆环进度条
     *
     * @param canvas 画布
     */
    private fun drawRingProgressBar(canvas: Canvas) {
        val strokeWidth = mCenterX - mRadius - mBorderWidth
        // 绘制进度条背景
        mProgressBarBgPaint!!.style = Paint.Style.STROKE
        mProgressBarBgPaint!!.strokeWidth = strokeWidth
        canvas.drawCircle(
                mCenterX,
                mCenterY,
                mCenterX - mBorderWidth - strokeWidth / 2,
                mProgressBarBgPaint!!
        )
        // 绘制边框
        if (mBorderWidth > 0) {
            mPath!!.addCircle(mCenterX, mCenterY, mCenterX - mBorderWidth / 2, Path.Direction.CW)
            canvas.drawPath(mPath!!, mBorderPaint!!)
        }
        // 绘制进度
        mPath!!.reset()
        mProgressPaint!!.style = Paint.Style.STROKE
        mProgressPaint!!.strokeCap = Paint.Cap.ROUND
        mProgressPaint!!.strokeJoin = Paint.Join.ROUND
        mProgressPaint!!.isDither = true
        mProgressPaint!!.strokeWidth = strokeWidth
        // 结束阶段进度画笔
        mEndProgressPaint!!.color = mProgressEndColor
        mEndProgressPaint!!.style = Paint.Style.STROKE
        mEndProgressPaint!!.strokeCap = Paint.Cap.ROUND
        mEndProgressPaint!!.strokeJoin = Paint.Join.ROUND
        mEndProgressPaint!!.isDither = true
        mEndProgressPaint!!.strokeWidth = strokeWidth
        // 逆时针旋转画布90度
        canvas.rotate(-90f, mCenterX, mCenterY)
        val oval = RectF(
                mBorderWidth + strokeWidth / 2,
                mBorderWidth + strokeWidth / 2,
                width - mBorderWidth - strokeWidth / 2,
                height - mBorderWidth - strokeWidth / 2
        )
        if (mClockwise) {
            mPath!!.addArc(oval, 0f, 360 * progress / max)
        } else {
            mPath!!.addArc(oval, -10f, -350 * progress / max)
        }
        canvas.drawPath(mPath!!, mProgressPaint!!)
        // 结束阶段进度
        if (progress / max > 0.9f) {
            // 添加尾部阴影效果
            if (progress / max >= 0.98f) {
                mPath!!.reset()
                if (mClockwise) {
                    mPath!!.addArc(oval, 360 * (progress / max), 1f)
                    mEndProgressPaint!!.setShadowLayer(5f, 0f, 10f, Color.BLACK)
                } else {
                    mPath!!.addArc(oval, -360 * (progress / max), -1f)
                    mEndProgressPaint!!.setShadowLayer(5f, 0f, -10f, Color.BLACK)
                }
                canvas.drawPath(mPath!!, mEndProgressPaint!!)
            }
            mPath!!.reset()
            if (mClockwise) {
                mPath!!.addArc(oval, 360 * 0.9f, 360 * (progress / max - 0.9f))
            } else {
                mPath!!.addArc(oval, -360 * 0.9f, -360 * (progress / max - 0.9f))
            }
            // 去掉阴影效果
            mEndProgressPaint!!.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
            canvas.drawPath(mPath!!, mEndProgressPaint!!)
        }
        if (mIsShowPercentText) {
            // 顺时针旋转画布90度
            canvas.rotate(90f, mCenterX, mCenterY)
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas)
        }
    }

    /**
     * 绘制扇形扫描式进度
     *
     * @param canvas 画布
     */
    private fun drawSectorProgressBar(canvas: Canvas) {
        // 绘制进度条背景
        canvas.drawCircle(mCenterX, mCenterY, mCenterX - mBorderWidth, mProgressBarBgPaint!!)
        // 绘制边框
        if (mBorderWidth > 0) {
            mPath!!.addCircle(mCenterX, mCenterY, mCenterX - mBorderWidth / 2, Path.Direction.CW)
            canvas.drawPath(mPath!!, mBorderPaint!!)
        }
        // 绘制进度
        // 逆时针旋转画布90度
        canvas.rotate(-90f, mCenterX, mCenterY)
        val oval = RectF(
                mBorderWidth,
                mBorderWidth,
                width - mBorderWidth,
                height - mBorderWidth
        )
        if (mClockwise) {
            canvas.drawArc(oval, 0f, 360 * progress / max, true, mProgressPaint!!)
        } else {
            canvas.drawArc(oval, 0f, -360 * progress / max, true, mProgressPaint!!)
        }
        if (mIsShowPercentText) {
            // 顺时针旋转画布90度
            canvas.rotate(90f, mCenterX, mCenterY)
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas)
        }
    }

    /**
     * 绘制进度文字和进度百分比符号
     *
     * @param canvas 画布
     */
    private fun drawPercentText(canvas: Canvas) {
        var percent: String = (progress * 100 / max).toInt().toString()
        if (mIsShowPercentSign) {
            percent = "$percent%"
        }
        val rect = Rect()
        // 获取字符串的宽高值
        mPercentTextPaint!!.getTextBounds(percent, 0, percent.length, rect)
        var textWidth = rect.width().toFloat()
        var textHeight = rect.height().toFloat()
        if (textWidth >= width) {
            textWidth = width.toFloat()
        }
        if (textHeight >= height) {
            textHeight = height.toFloat()
        }
        canvas.drawText(percent, mCenterX - textWidth / 2, mCenterY + textHeight / 2, mPercentTextPaint!!)
    }

    internal class SavedState : BaseSavedState {
        internal var progress = 0F

        internal constructor(superState: Parcelable) : super(superState) {}
        private constructor(`in`: Parcel) : super(`in`) {
            progress = `in`.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(progress)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    public override fun onSaveInstanceState(): Parcelable? {
        // Force our ancestor class to save its state
        val superState = super.onSaveInstanceState()
        val ss = superState?.let { SavedState(it) }
        ss?.progress = progress
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        setProgress(ss.progress)
    }

    fun setProgressBarBgColor(mProgressBarBgColor: Int): SmartProgressBar {
        this.mProgressBarBgColor = mProgressBarBgColor
        mProgressBarBgPaint!!.color = mProgressBarBgColor
        return this
    }

    fun setProgressStartColor(mProgressStartColor: Int): SmartProgressBar {
        this.mProgressStartColor = mProgressStartColor
        return this
    }

    fun setProgressCenterColor(mProgressCenterColor: Int): SmartProgressBar {
        this.mProgressCenterColor = mProgressCenterColor
        return this
    }

    fun setProgressEndColor(mProgressEndColor: Int): SmartProgressBar {
        this.mProgressEndColor = mProgressEndColor
        return this
    }

    fun setBorderColor(mBorderColor: Int): SmartProgressBar {
        this.mBorderColor = mBorderColor
        mBorderPaint!!.color = mBorderColor
        return this
    }

    fun setBorderWidth(mBorderWidth: Float): SmartProgressBar {
        this.mBorderWidth = mBorderWidth
        mBorderPaint!!.strokeWidth = mBorderWidth
        return this
    }

    fun setPercentTextSize(mPercentTextSize: Float): SmartProgressBar {
        this.mPercentTextSize = mPercentTextSize
        mPercentTextPaint!!.textSize = mPercentTextSize
        return this
    }

    fun setPercentTextColor(mPercentTextColor: Int): SmartProgressBar {
        this.mPercentTextColor = mPercentTextColor
        mPercentTextPaint!!.color = mPercentTextColor
        return this
    }

    fun setShapeStyle(mShapeStyle: Int): SmartProgressBar {
        this.mShapeStyle = mShapeStyle
        return this
    }

    fun setRadius(mRadius: Float): SmartProgressBar {
        this.mRadius = mRadius
        setRadius(mRadius, mRadius, mRadius, mRadius)
        return this
    }

    fun setClockwise(mClockwise: Boolean): SmartProgressBar {
        this.mClockwise = mClockwise
        return this
    }

    fun setRadius(mTopLeftRadius: Float, mTopRightRadius: Float, mBottomRightRadius: Float, mBottomLeftRadius: Float): SmartProgressBar {
        this.mTopLeftRadius = mTopLeftRadius
        this.mTopRightRadius = mTopRightRadius
        this.mBottomRightRadius = mBottomRightRadius
        this.mBottomLeftRadius = mBottomLeftRadius
        return this
    }

    fun setRadii(mRadii: FloatArray): SmartProgressBar {
        this.mRadii = mRadii
        return this
    }

    fun setIsShowPercentSign(mIsShowPercentSign: Boolean): SmartProgressBar {
        this.mIsShowPercentSign = mIsShowPercentSign
        return this
    }

    fun setIsShowPercentText(mIsShowPercentText: Boolean): SmartProgressBar {
        this.mIsShowPercentText = mIsShowPercentText
        return this
    }

    fun setIsAnimated(mIsAnimated: Boolean): SmartProgressBar {
        this.mIsAnimated = mIsAnimated
        return this
    }

    fun setDuration(mDuration: Long): SmartProgressBar {
        this.mDuration = mDuration
        return this
    }

    fun setAnimatorUpdateListener(mAnimatorUpdateListener: AnimatorUpdateListener?): SmartProgressBar {
        this.mAnimatorUpdateListener = mAnimatorUpdateListener
        return this
    }

    fun setMax(max: Float): SmartProgressBar {
        this.max = max
        return this
    }

    fun setProgress(progress: Float): SmartProgressBar {
        val lastProgress = progress
        if (lastProgress == 0F || lastProgress == max) {
            updateProgress(progress)
            return this
        }
        mProgressAnimator = ValueAnimator.ofFloat(lastProgress, progress)
        mProgressAnimator!!.interpolator = DecelerateInterpolator()
        mProgressAnimator!!.duration = 2000
        mProgressAnimator!!.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            updateProgress(animatedValue)
        }
        mProgressAnimator!!.start()
        return this
    }

    private fun updateProgress(progress: Float) {
        when {
            progress > max -> {
                this.progress = max
            }
            progress < 0 -> {
                this.progress = 0F
            }
            else -> {
                this.progress = progress
            }
        }
        postInvalidate()
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    fun Context.dp2px(dpValue: Float): Float {
        val scale = resources.displayMetrics.density
        return dpValue * scale + 0.5F
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    fun Context.px2dp(pxValue: Float): Float {
        val scale = resources.displayMetrics.density
        return pxValue / scale + 0.5F
    }

    companion object {
        private const val DEFAULT_WIDTH = 100F
        private const val DEFAULT_HEIGHT = 100F
        private const val DEFAULT_PROGRESS_COLOR = Color.BLUE
        private const val DEFAULT_PROGRESS_BAR_BG_COLOR = Color.WHITE
        private const val DEFAULT_PERCENT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_PERCENT_TEXT_SIZE = 15F
        private const val DEFAULT_BORDER_COLOR = Color.RED
        private const val DEFAULT_MAX = 100F
        private const val DEFAULT_ANIMATION_DURATION = 500L
    }

    init {
        attrs?.let { initAttributes(context, it, defStyleAttr) }
        init()
    }
}