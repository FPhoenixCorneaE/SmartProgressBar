package com.wkz.smartprogressbar;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义的进度条
 * 样式风格有水平、竖直、圆环、扇形、......
 *
 * @author wkz
 * @date 2019/05/05 21:23
 */
public class SmartProgressBar extends View {

    private static final float DEFAULT_WIDTH = 100F;
    private static final float DEFAULT_HEIGHT = 100F;
    private static final int DEFAULT_PROGRESS_COLOR = Color.BLUE;
    private static final int DEFAULT_PROGRESS_BAR_BG_COLOR = Color.WHITE;
    private static final int DEFAULT_PERCENT_TEXT_COLOR = Color.BLACK;
    private static final float DEFAULT_PERCENT_TEXT_SIZE = 15F;
    private static final int DEFAULT_BORDER_COLOR = Color.RED;
    private static final int DEFAULT_MAX = 100;
    private static final int DEFAULT_ANIMATION_DURATION = 500;


    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ShapeStyle {
        /**
         * 水平样式
         */
        int HORIZONTAL = 0;
        /**
         * 竖直样式
         */
        int VERTICAL = 1;
        /**
         * 圆环样式
         */
        int RING = 2;
        /**
         * 扇形样式
         */
        int SECTOR = 3;
    }


    /**
     * 进度条背景颜色
     **/
    private int mProgressBarBgColor = DEFAULT_PROGRESS_BAR_BG_COLOR;
    /**
     * 进度颜色
     */
    private int mProgressStartColor = DEFAULT_PROGRESS_COLOR;
    private int mProgressCenterColor = DEFAULT_PROGRESS_COLOR;
    private int mProgressEndColor = DEFAULT_PROGRESS_COLOR;
    /**
     * 边框颜色
     */
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    /**
     * 边框宽度
     */
    private float mBorderWidth;
    /**
     * 进度提示文字大小
     **/
    private float mPercentTextSize = DEFAULT_PERCENT_TEXT_SIZE;
    /**
     * 进度提示文字颜色
     **/
    private int mPercentTextColor = DEFAULT_PERCENT_TEXT_COLOR;
    /**
     * 进度条中心X坐标
     **/
    private float mCenterX;
    /**
     * 进度条中心Y坐标
     **/
    private float mCenterY;
    /**
     * 进度条样式
     **/
    private int mShapeStyle = ShapeStyle.HORIZONTAL;
    /**
     * 水平、竖直进度条圆角半径；圆环/扇形内圆半径
     **/
    private float mRadius;
    /**
     * 圆环/扇形是否顺时针方向绘制
     */
    private boolean mClockwise = true;
    /**
     * 水平、竖直进度条圆角半径
     */
    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;
    private float[] mRadii;
    /**
     * 进度最大值
     **/
    private int mMax = DEFAULT_MAX;
    /**
     * 进度值
     **/
    private int mProgress;
    /**
     * 进度文字是否显示百分号
     **/
    private boolean mIsShowPercentSign;
    /**
     * 进度文字是否显示
     */
    private boolean mIsShowPercentText;
    /**
     * 进度画笔
     */
    private Paint mProgressPaint;
    /**
     * 进度条背景画笔
     */
    private Paint mProgressBarBgPaint;
    /**
     * 进度百分比字体画笔
     */
    private Paint mPercentTextPaint;
    /**
     * 边框画笔
     */
    private Paint mBorderPaint;
    /**
     * 绘制路径
     */
    private Path mPath;
    /**
     * 是否执行动画
     */
    private boolean mIsAnimated = true;
    private ValueAnimator mAnimator;
    private long mDuration = DEFAULT_ANIMATION_DURATION;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;

    public SmartProgressBar(Context context) {
        this(context, null);
    }

    public SmartProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            initAttributes(context, attrs, defStyleAttr);
        }
        init();
    }

    /**
     * 初始化自定义属性
     */
    private void initAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SmartProgressBar, defStyleAttr, 0);
        try {
            mMax = attributes.getInteger(R.styleable.SmartProgressBar_spb_max, DEFAULT_MAX);
            mProgress = attributes.getInteger(R.styleable.SmartProgressBar_spb_progress, 0);
            mProgressStartColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_start_color, DEFAULT_PROGRESS_COLOR);
            mProgressCenterColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_center_color, DEFAULT_PROGRESS_COLOR);
            mProgressEndColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_end_color, DEFAULT_PROGRESS_COLOR);
            mProgressBarBgColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_bar_bg_color, DEFAULT_PROGRESS_BAR_BG_COLOR);
            mIsShowPercentText = attributes.getBoolean(R.styleable.SmartProgressBar_spb_show_percent_text, false);
            mIsShowPercentSign = attributes.getBoolean(R.styleable.SmartProgressBar_spb_show_percent_sign, false);
            mPercentTextColor = attributes.getColor(R.styleable.SmartProgressBar_spb_percent_text_color, DEFAULT_PERCENT_TEXT_COLOR);
            mPercentTextSize = attributes.getDimension(R.styleable.SmartProgressBar_spb_percent_text_size, DEFAULT_PERCENT_TEXT_SIZE);
            mBorderColor = attributes.getColor(R.styleable.SmartProgressBar_spb_border_color, DEFAULT_BORDER_COLOR);
            mBorderWidth = attributes.getDimension(R.styleable.SmartProgressBar_spb_border_width, 0);
            mRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_radius, 0);
            mClockwise = attributes.getBoolean(R.styleable.SmartProgressBar_spb_clockwise, true);
            mTopLeftRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_top_left_radius, 0);
            mTopRightRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_top_right_radius, 0);
            mBottomLeftRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_bottom_left_radius, 0);
            mBottomRightRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_bottom_right_radius, 0);
            mShapeStyle = attributes.getInt(R.styleable.SmartProgressBar_spb_shape_style, 0);
            mIsAnimated = attributes.getBoolean(R.styleable.SmartProgressBar_spb_animated, true);
            mDuration = attributes.getInt(R.styleable.SmartProgressBar_spb_animated_duration, DEFAULT_ANIMATION_DURATION);

            if (mMax <= 0) {
                mMax = DEFAULT_MAX;
            }
            if (mProgress > mMax) {
                mProgress = mMax;
            } else if (mProgress < 0) {
                mProgress = 0;
            }
        } finally {
            attributes.recycle();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        /*进度画笔*/
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.FILL);

        /*进度条背景画笔*/
        mProgressBarBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressBarBgPaint.setColor(mProgressBarBgColor);

        /*边框画笔*/
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        /*进度百分比字体画笔*/
        mPercentTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPercentTextPaint.setColor(mPercentTextColor);
        mPercentTextPaint.setStyle(Paint.Style.FILL);
        mPercentTextPaint.setTextSize(mPercentTextSize);

        /*若是设置了radius属性，四个圆角属性值以radius属性值为准*/
        if (mRadius > 0) {
            mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius = mRadius;
        }

        /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
        mRadii = new float[]{mTopLeftRadius, mTopLeftRadius, mTopRightRadius, mTopRightRadius,
                mBottomRightRadius, mBottomRightRadius, mBottomLeftRadius, mBottomLeftRadius};

        /*绘制路径*/
        mPath = new Path();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 是否执行动画
        if (mIsAnimated) {
            // 设置属性动画参数
            if (mAnimator == null) {
                mAnimator = new ValueAnimator();
                mAnimator = ValueAnimator.ofFloat(0F, mProgress);
                mAnimator.setRepeatCount(0);
                mAnimator.setRepeatMode(ValueAnimator.RESTART);
                mAnimator.setInterpolator(new LinearInterpolator());
                mAnimator.setDuration(mDuration);
                // 设置动画的回调
                mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        mProgress = (int) animatedValue;
                        invalidate();
                    }
                };
                mAnimator.addUpdateListener(mAnimatorUpdateListener);
            }
            post(new Runnable() {
                @Override
                public void run() {
                    mAnimator.start();
                }
            });
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIsAnimated && mAnimator != null) {
            mAnimator.cancel();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        if (widthSpecMode == MeasureSpec.AT_MOST
                || widthSpecMode == MeasureSpec.UNSPECIFIED) {
            width = dp2px(getContext(), DEFAULT_WIDTH);
        } else {
            width = widthSpecSize;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            height = dp2px(getContext(), DEFAULT_HEIGHT);
        } else {
            height = heightSpecSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = (float) getWidth() / 2;
        mCenterY = (float) getHeight() / 2;

        if (mShapeStyle == ShapeStyle.HORIZONTAL) {
            /*创建线性颜色渐变器*/
            Shader linearGradient = new LinearGradient(
                    mBorderWidth,
                    (getHeight() - mBorderWidth * 2) / 2,
                    mBorderWidth + (float) mProgress / mMax * (getWidth() - mBorderWidth * 2),
                    (getHeight() - mBorderWidth * 2) / 2,
                    new int[]{mProgressStartColor, mProgressCenterColor, mProgressEndColor},
                    new float[]{0, 0.5f, 1}, Shader.TileMode.MIRROR
            );

            mProgressPaint.setShader(linearGradient);
        } else if (mShapeStyle == ShapeStyle.VERTICAL) {
            /*创建线性颜色渐变器*/
            Shader linearGradient = new LinearGradient(
                    (getWidth() - mBorderWidth * 2) / 2,
                    getHeight() - mBorderWidth,
                    (getWidth() - mBorderWidth * 2) / 2,
                    getHeight() - (float) mProgress / mMax * (getHeight() - mBorderWidth * 2) - mBorderWidth,
                    new int[]{mProgressStartColor, mProgressCenterColor, mProgressEndColor},
                    new float[]{0, 0.5f, 1}, Shader.TileMode.MIRROR
            );
            mProgressPaint.setShader(linearGradient);
        } else if (mShapeStyle == ShapeStyle.RING) {
            /*创建扫描式渐变器*/
            int[] colors;
            if (mClockwise) {
                colors = new int[]{
                        mProgressStartColor,
                        mProgressCenterColor,
                        mProgressEndColor,
                        mProgressStartColor
                };
            } else {
                colors = new int[]{
                        mProgressEndColor,
                        mProgressCenterColor,
                        mProgressStartColor,
                        mProgressEndColor
                };
            }
            float[] positions = new float[]{0, 0.45F, 0.9F, 1F};
            Shader sweepGradient = new SweepGradient(
                    0,
                    0,
                    colors,
                    positions
            );

            mProgressPaint.setShader(sweepGradient);

            Matrix gradientMatrix = new Matrix();
            gradientMatrix.setTranslate(mCenterX, mCenterY);
            sweepGradient.setLocalMatrix(gradientMatrix);
        } else if (mShapeStyle == ShapeStyle.SECTOR) {
            /*创建扫描式渐变器*/
            int[] colors;
            if (mClockwise) {
                colors = new int[]{
                        mProgressStartColor,
                        mProgressCenterColor,
                        mProgressEndColor
                };
            } else {
                colors = new int[]{
                        mProgressEndColor,
                        mProgressCenterColor,
                        mProgressStartColor
                };
            }
            Shader sweepGradient = new SweepGradient(
                    0,
                    0,
                    colors,
                    null
            );

            mProgressPaint.setShader(sweepGradient);

            Matrix gradientMatrix = new Matrix();
            gradientMatrix.setTranslate(mCenterX, mCenterY);
            sweepGradient.setLocalMatrix(gradientMatrix);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if (mShapeStyle == ShapeStyle.HORIZONTAL) {
            drawHorizontalProgressBar(canvas);
        } else if (mShapeStyle == ShapeStyle.VERTICAL) {
            drawVerticalProgressBar(canvas);
        } else if (mShapeStyle == ShapeStyle.RING) {
            drawRingProgressBar(canvas);
        } else if (mShapeStyle == ShapeStyle.SECTOR) {
            drawSectorProgressBar(canvas);
        }
        canvas.restore();
        mPath.reset();
        super.onDraw(canvas);
    }

    /**
     * 绘制水平进度条
     *
     * @param canvas 画布
     */
    private void drawHorizontalProgressBar(Canvas canvas) {
        // 绘制进度条背景
        mPath.addRoundRect(new RectF(
                        mBorderWidth / 2,
                        mBorderWidth / 2,
                        getWidth() - mBorderWidth / 2,
                        getHeight() - mBorderWidth / 2),
                mRadii, Path.Direction.CW);
        canvas.drawPath(mPath, mProgressBarBgPaint);

        // 绘制边框
        if (mBorderWidth > 0) {
            canvas.drawPath(mPath, mBorderPaint);
        }


        // 绘制进度
        mPath.reset();
        mPath.addRoundRect(
                new RectF(
                        mBorderWidth,
                        mBorderWidth,
                        mBorderWidth + (float) mProgress / mMax * (getWidth() - mBorderWidth * 2),
                        getHeight() - mBorderWidth
                ),
                mRadii,
                Path.Direction.CW
        );
        canvas.drawPath(mPath, mProgressPaint);


        if (mIsShowPercentText) {
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas);
        }
    }

    /**
     * 绘制竖直进度条
     *
     * @param canvas 画布
     */
    private void drawVerticalProgressBar(Canvas canvas) {
        // 绘制进度条背景
        mPath.addRoundRect(new RectF(
                        mBorderWidth / 2,
                        mBorderWidth / 2,
                        getWidth() - mBorderWidth / 2,
                        getHeight() - mBorderWidth / 2),
                mRadii, Path.Direction.CW);
        canvas.drawPath(mPath, mProgressBarBgPaint);

        // 绘制边框
        if (mBorderWidth > 0) {
            canvas.drawPath(mPath, mBorderPaint);
        }

        // 绘制进度
        mPath.reset();
        mPath.addRoundRect(new RectF(
                        mBorderWidth,
                        getHeight() - (float) mProgress / mMax * (getHeight() - mBorderWidth * 2) - mBorderWidth,
                        getWidth() - mBorderWidth,
                        getHeight() - mBorderWidth),
                mRadii, Path.Direction.CW);
        canvas.drawPath(mPath, mProgressPaint);

        if (mIsShowPercentText) {
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas);
        }
    }

    /**
     * 绘制圆环进度条
     *
     * @param canvas 画布
     */
    private void drawRingProgressBar(Canvas canvas) {
        float strokeWidth = mCenterX - mRadius - mBorderWidth;
        // 绘制进度条背景
        mProgressBarBgPaint.setStyle(Paint.Style.STROKE);
        mProgressBarBgPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(
                mCenterX,
                mCenterY,
                mCenterX - mBorderWidth - strokeWidth / 2,
                mProgressBarBgPaint
        );

        // 绘制边框
        if (mBorderWidth > 0) {
            mPath.addCircle(mCenterX, mCenterY, mCenterX - mBorderWidth / 2, Path.Direction.CW);
            canvas.drawPath(mPath, mBorderPaint);
        }

        // 绘制进度
        mPath.reset();
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setStrokeJoin(Paint.Join.ROUND);
        mProgressPaint.setDither(true);
        mProgressPaint.setStrokeWidth(strokeWidth);
        // 逆时针旋转画布90度
        canvas.rotate(-90, mCenterX, mCenterY);
        RectF oval = new RectF(
                mBorderWidth + strokeWidth / 2,
                mBorderWidth + strokeWidth / 2,
                getWidth() - mBorderWidth - strokeWidth / 2,
                getHeight() - mBorderWidth - strokeWidth / 2
        );
        if (mClockwise) {
            mPath.addArc(oval, 0, 360 * mProgress / (float) mMax);
        } else {
            mPath.addArc(oval, 0, -360 * mProgress / (float) mMax);
        }
        canvas.drawPath(mPath, mProgressPaint);

        if (mIsShowPercentText) {
            // 顺时针旋转画布90度
            canvas.rotate(90, mCenterX, mCenterY);
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas);
        }
    }

    /**
     * 绘制扇形扫描式进度
     *
     * @param canvas 画布
     */
    private void drawSectorProgressBar(Canvas canvas) {
        // 绘制进度条背景
        canvas.drawCircle(mCenterX, mCenterY, mCenterX - mBorderWidth, mProgressBarBgPaint);

        // 绘制边框
        if (mBorderWidth > 0) {
            mPath.addCircle(mCenterX, mCenterY, mCenterX - mBorderWidth / 2, Path.Direction.CW);
            canvas.drawPath(mPath, mBorderPaint);
        }

        // 绘制进度
        // 逆时针旋转画布90度
        canvas.rotate(-90, mCenterX, mCenterY);
        RectF oval = new RectF(
                mBorderWidth,
                mBorderWidth,
                getWidth() - mBorderWidth,
                getHeight() - mBorderWidth
        );
        if (mClockwise) {
            canvas.drawArc(oval, 0, 360 * mProgress / (float) mMax, true, mProgressPaint);
        } else {
            canvas.drawArc(oval, 0, -360 * mProgress / (float) mMax, true, mProgressPaint);
        }

        if (mIsShowPercentText) {
            // 顺时针旋转画布90度
            canvas.rotate(90, mCenterX, mCenterY);
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas);
        }
    }

    /**
     * 绘制进度文字和进度百分比符号
     *
     * @param canvas 画布
     */
    private void drawPercentText(Canvas canvas) {
        String percent = String.valueOf(mProgress * 100 / mMax);
        if (mIsShowPercentSign) {
            percent = percent + "%";
        }

        Rect rect = new Rect();
        // 获取字符串的宽高值
        mPercentTextPaint.getTextBounds(percent, 0, percent.length(), rect);
        float textWidth = rect.width();
        float textHeight = rect.height();
        if (textWidth >= getWidth()) {
            textWidth = getWidth();
        }
        if (textHeight >= getHeight()) {
            textHeight = getHeight();
        }
        canvas.drawText(percent, mCenterX - textWidth / 2, mCenterY + textHeight / 2, mPercentTextPaint);
    }

    private static final class SavedState extends BaseSavedState {
        private int progress;

        private SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.progress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.progress = mProgress;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
    }

    public SmartProgressBar setProgressBarBgColor(int mProgressBarBgColor) {
        this.mProgressBarBgColor = mProgressBarBgColor;
        this.mProgressBarBgPaint.setColor(mProgressBarBgColor);
        return this;
    }

    public SmartProgressBar setProgressStartColor(int mProgressStartColor) {
        this.mProgressStartColor = mProgressStartColor;
        return this;
    }

    public SmartProgressBar setProgressCenterColor(int mProgressCenterColor) {
        this.mProgressCenterColor = mProgressCenterColor;
        return this;
    }

    public SmartProgressBar setProgressEndColor(int mProgressEndColor) {
        this.mProgressEndColor = mProgressEndColor;
        return this;
    }

    public SmartProgressBar setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        this.mBorderPaint.setColor(mBorderColor);
        return this;
    }

    public SmartProgressBar setBorderWidth(float mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        this.mBorderPaint.setStrokeWidth(mBorderWidth);
        return this;
    }

    public SmartProgressBar setPercentTextSize(float mPercentTextSize) {
        this.mPercentTextSize = mPercentTextSize;
        this.mPercentTextPaint.setTextSize(mPercentTextSize);
        return this;
    }

    public SmartProgressBar setPercentTextColor(int mPercentTextColor) {
        this.mPercentTextColor = mPercentTextColor;
        this.mPercentTextPaint.setColor(mPercentTextColor);
        return this;
    }

    public SmartProgressBar setShapeStyle(int mShapeStyle) {
        this.mShapeStyle = mShapeStyle;
        return this;
    }

    public SmartProgressBar setRadius(float mRadius) {
        this.mRadius = mRadius;
        setRadius(mRadius, mRadius, mRadius, mRadius);
        return this;
    }

    public SmartProgressBar setClockwise(boolean mClockwise) {
        this.mClockwise = mClockwise;
        return this;
    }

    public SmartProgressBar setRadius(float mTopLeftRadius, float mTopRightRadius, float mBottomRightRadius, float mBottomLeftRadius) {
        this.mTopLeftRadius = mTopLeftRadius;
        this.mTopRightRadius = mTopRightRadius;
        this.mBottomRightRadius = mBottomRightRadius;
        this.mBottomLeftRadius = mBottomLeftRadius;
        return this;
    }

    public SmartProgressBar setRadii(float[] mRadii) {
        this.mRadii = mRadii;
        return this;
    }

    public SmartProgressBar setIsShowPercentSign(boolean mIsShowPercentSign) {
        this.mIsShowPercentSign = mIsShowPercentSign;
        return this;
    }

    public SmartProgressBar setIsShowPercentText(boolean mIsShowPercentText) {
        this.mIsShowPercentText = mIsShowPercentText;
        return this;
    }

    public SmartProgressBar setIsAnimated(boolean mIsAnimated) {
        this.mIsAnimated = mIsAnimated;
        return this;
    }

    public SmartProgressBar setDuration(long mDuration) {
        this.mDuration = mDuration;
        return this;
    }

    public SmartProgressBar setAnimatorUpdateListener(ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener) {
        this.mAnimatorUpdateListener = mAnimatorUpdateListener;
        return this;
    }

    public SmartProgressBar setMax(int max) {
        this.mMax = max;
        return this;
    }

    public SmartProgressBar setProgress(int progress) {
        if (progress > mMax) {
            this.mProgress = mMax;
        } else if (progress < 0) {
            this.mProgress = 0;
        } else {
            this.mProgress = progress;
        }
        postInvalidate();
        return this;
    }

    public int getMax() {
        return mMax;
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    private int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
